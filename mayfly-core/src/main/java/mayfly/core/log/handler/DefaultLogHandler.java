package mayfly.core.log.handler;

import mayfly.core.exception.BizException;
import mayfly.core.log.InvokeInfo;
import mayfly.core.log.LogChange;
import mayfly.core.log.LogContext;
import mayfly.core.log.LogInfo;
import mayfly.core.log.NoNeedLogParam;
import mayfly.core.permission.LoginAccount;
import mayfly.core.util.CollectionUtils;
import mayfly.core.util.ReflectionUtils;
import mayfly.core.util.StringUtils;
import mayfly.core.util.annotation.AnnotationUtils;
import mayfly.core.util.bean.FieldValueChangeRecord;
import mayfly.core.util.enums.EnumUtils;
import mayfly.core.util.enums.NameValueEnum;
import mayfly.core.validation.annotation.EnumValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * @author meilin.huang
 * @date 2021-09-17 7:28 下午
 */
public class DefaultLogHandler implements LogHandler {

    private static final Logger LOG = LoggerFactory.getLogger(DefaultLogHandler.class);

    /**
     * 调用信息前缀
     */
    public static final String INVOKER_FLAG = "\n==> ";

    /**
     * 返回结果模板
     */
    public static final String RESULT_FLAG = "\n<== ";

    /**
     * 执行时间模板
     */
    public static final String TIME_MSG_TEMP = " -> ";

    /**
     * 异常信息模板
     */
    public static final String EXCEPTION_FLAG = "\n<-e ";

    /**
     * 字段变更模板
     */
    public static final String OBJ_CHANGE = "\nc==> ";

    @Override
    public void handle(LogInfo metadata, InvokeInfo invokeInfo) {
        if (invokeInfo.getException() != null) {
            String errMsg = getErrMsg(metadata, invokeInfo);
            LOG.error(errMsg);
            LogContext.putVar(LogContext.DEFAULT_LOG_MSG, errMsg);
            return;
        }

        String logMsg = getDefaultLogMsg(metadata, invokeInfo);
        // 将默认错误日志信息内容放至日志上下文，方便其他日志处理器获取，避免重复获取类似内容
        LogContext.putVar(LogContext.DEFAULT_LOG_MSG, logMsg);
        switch (metadata.getLevel()) {
            case DEBUG:
                LOG.debug(logMsg);
                break;
            case WARN:
                LOG.warn(logMsg);
                break;
            case ERROR:
                LOG.error(logMsg);
                break;
            default:
                LOG.info(logMsg);
                break;
        }
    }

    private String getErrMsg(LogInfo metadata, InvokeInfo invokeInfo) {
        String logMsg = getInvokeInfo(metadata, invokeInfo) + EXCEPTION_FLAG;
        Exception e = invokeInfo.getException();
        if (e instanceof BizException) {
            BizException bizE = (BizException) e;
            logMsg = logMsg + "[errCode:" + bizE.getErrorCode() + ", errMsg:" + bizE.getMessage() + "]";
        } else {
            logMsg = logMsg + "sysErr: " + e.getMessage();
        }
        return logMsg;
    }

    /**
     * 获取默认的日志内容
     *
     * @param metadata   日志元信息
     * @param invokeInfo 日志信息
     * @return 日志内容
     */
    private String getDefaultLogMsg(LogInfo metadata, InvokeInfo invokeInfo) {
        String logMsg = getInvokeInfo(metadata, invokeInfo);

        // 日志上下文存在旧对象，则对比新旧对象字段值变更，并记录
        Object oldObj = LogContext.getVar(LogContext.OLD_OBJ_KEY);
        if (oldObj != null) {
            Object newObj = LogContext.getVar(LogContext.NEW_OBJ_KEY);
            if (newObj == null) {
                // 默认去参数值列表第一个
                newObj = invokeInfo.getArgs()[0];
            }
            List<FieldValueChangeRecord> changeRecords = getFieldValueChangeRecords(newObj, oldObj);
            if (CollectionUtils.isEmpty(changeRecords)) {
                return logMsg;
            }

            StringBuilder sb = new StringBuilder("\n----------change----------");
            changeRecords.forEach(c -> {
                sb.append("\n").append(c.getFieldName()).append(": ").append(c.getOldValue()).append(" -> ").append(c.getNewValue());
            });
            sb.append("\n--------------------------");
            logMsg += sb.toString();
        }

        return logMsg;
    }

    /**
     * 获取调用信息
     *
     * @param lm      日志元信息
     * @param logInfo 日志信息
     * @return 调用信息
     */
    private String getInvokeInfo(LogInfo lm, InvokeInfo logInfo) {
        Method m = lm.getMethod();
        Object[] args = logInfo.getArgs();

        StringBuilder invokeInfo = new StringBuilder();
        Arrays.stream(getTags(lm)).forEach(t -> invokeInfo.append("[").append(t).append("]"));
        invokeInfo.append(INVOKER_FLAG).append(m.getDeclaringClass().getName()).append("#").append(m.getName()).append("(");
        Parameter[] parameters = m.getParameters();
        boolean first = true;
        for (int i = 0; i < args.length; i++) {
            if (parameters[i].isAnnotationPresent(NoNeedLogParam.class)) {
                continue;
            }
            if (!first) {
                invokeInfo.append(", ");
            } else {
                first = false;
            }
            invokeInfo.append("arg").append(i).append(": ").append(args[i]);
        }

        invokeInfo.append(")").append(TIME_MSG_TEMP).append(logInfo.getExecTime()).append("ms");
        // 设置返回结果日志信息
        if (m.getReturnType() != Void.TYPE && lm.getResLevel().order() >= logInfo.getSysLevel().order()) {
            invokeInfo.append(RESULT_FLAG).append(logInfo.getRes());
        }
        return invokeInfo.toString();
    }

    private String[] getTags(LogInfo lm) {
        List<String> tags = new ArrayList<>();
        LoginAccount la = LoginAccount.getFromContext();
        if (la != null) {
            tags.add(String.format("uid=%s, uname=%s", la.getId(), la.getUsername()));
        }
        String value = lm.getContent();
        if (!StringUtils.isEmpty(value)) {
            tags.add(value);
        }
        return tags.toArray(new String[0]);
    }

    /**
     * 获取字段值变更记录
     *
     * @param newObj 新对象
     * @param old    旧对象
     * @return 变更记录列表
     */
    @SuppressWarnings("all")
    public static List<FieldValueChangeRecord> getFieldValueChangeRecords(Object newObj, Object old) {
        Class<?> oldObjClass = old.getClass();
        // 是否记录实体全部字段变化
        boolean allFieldLogChange = AnnotationUtils.isAnnotationPresent(newObj.getClass(), LogChange.class);

        List<FieldValueChangeRecord> changeRecords = new ArrayList<>();
        // 遍历所有标注@LogChange注解的字段
        for (Field nf : ReflectionUtils.getFields(newObj.getClass())) {
            LogChange lc = AnnotationUtils.getAnnotation(nf, LogChange.class);
            if (!allFieldLogChange && lc == null) {
                continue;
            }
            String fieldName = nf.getName();
            // 旧值不存在指定字段，直接跳过
            Field oldObjFiled = ReflectionUtils.getField(oldObjClass, fieldName, nf.getType());
            if (oldObjFiled == null || Modifier.isStatic(oldObjFiled.getModifiers())) {
                continue;
            }
            Object newValue = ReflectionUtils.getFieldValue(nf, newObj);
            Object oldValue = ReflectionUtils.getFieldValue(oldObjFiled, old);
            if (!Objects.equals(oldValue, newValue)) {
                // 如果字段上的注解不为空，则进行补充字段说明等
                if (lc != null) {
                    String name = lc.name();
                    if (!StringUtils.isEmpty(name)) {
                        fieldName = fieldName + "(" + name + ")";
                    }
                    // 枚举值说明补充
                    Class<? extends Enum> enumClass = lc.enumValue();
                    if (enumClass != EnumValue.DefaultNameValueEnum.class) {
                        oldValue = oldValue + "[" + EnumUtils.getNameByValue((NameValueEnum[]) enumClass.getEnumConstants(), oldValue) + "]";
                        newValue = newValue + "[" + EnumUtils.getNameByValue((NameValueEnum[]) enumClass.getEnumConstants(), newValue) + "]";
                    }
                }
                changeRecords.add(new FieldValueChangeRecord(fieldName, oldValue, newValue));
            }
        }
        return changeRecords;
    }
}
