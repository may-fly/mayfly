package mayfly.core.log;

import mayfly.core.log.annotation.Log;
import mayfly.core.log.annotation.LogChange;
import mayfly.core.log.annotation.NoNeedLogParam;
import mayfly.core.log.expression.LogEvaluationContext;
import mayfly.core.log.handler.LogHandler;
import mayfly.core.util.ReflectionUtils;
import mayfly.core.util.StringUtils;
import mayfly.core.util.annotation.AnnotationUtils;
import mayfly.core.util.enums.EnumUtils;
import mayfly.core.util.enums.NameValueEnum;
import mayfly.core.validation.annotation.EnumValue;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.expression.Expression;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 日志切面
 *
 * @author hml
 * @version 1.
 * @date 2018-09-19 上午9:16
 */
@Aspect
public class LogAspect implements ApplicationContextAware {

    private static final Logger LOG = LoggerFactory.getLogger(LogAspect.class);

    private static final Map<Method, LogInfo> LOGS = new ConcurrentHashMap<>(128);

    private ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    /**
     * 日志处理器列表
     */
    private final List<LogHandler> logHandlers = new ArrayList<>(4);

    /**
     * 是否已加载容器里所有日志处理器
     */
    private boolean loadAllHandler;

    public LogAspect() {
        loadAllHandler = false;
    }

    public LogAspect(LogHandler lh) {
        loadAllHandler = false;
        addLogHandler(lh);
    }

    /**
     * 拦截带有Log的方法或带有该注解的类
     */
    @Pointcut("@annotation(mayfly.core.log.annotation.Log) || @within(mayfly.core.log.annotation.Log)")
    private void logPointcut() {
    }

    @Around(value = "logPointcut()")
    private Object around(ProceedingJoinPoint pjp) throws Throwable {
        Method method = ((MethodSignature) pjp.getSignature()).getMethod();
        LogInfo logInfo = LOGS.computeIfAbsent(method, LogInfo::from);
        int sysLevel = getSysLogLevel().order();
        Log.Level level = logInfo.getLevel();
        // 方法的日志级别<系统日志级别直接返回
        if (level.order() < sysLevel) {
            return pjp.proceed();
        }

        InvokeLog invokeLog = InvokeLog.newInstance()
                .method(method)
                // 过滤掉不需要记录的参数
                .args(getArgs(method, pjp.getArgs()));

        long startTime = System.currentTimeMillis();
        try {
            Object result = pjp.proceed();
            invokeLog.res(result)
                    // 执行时间
                    .execTime(System.currentTimeMillis() - startTime)
                    .level(level)
                    // 是否需要记录执行结果
                    .logRes(method.getReturnType() != Void.TYPE && logInfo.getResLevel().order() >= sysLevel)
                    // 字段值变化列表
                    .fieldChanges(getChange(invokeLog.getArgs()));
            return result;
        } catch (Exception e) {
            invokeLog.exception(e);
            throw e;
        } finally {
            String content = logInfo.getContent();
            Expression expression = logInfo.getExpression();
            if (expression != null) {
                try {
                    content = expression.getValue(LogEvaluationContext.from(invokeLog), String.class);
                } catch (Exception e) {
                    LOG.error("获取spel日志描述失败", e);
                }
            }
            invokeLog.description(content);
            invokeHandler(invokeLog);
            LogContext.clear();
        }
    }

    /**
     * 获取参数信息，去除不需要记录的参数信息
     *
     * @param method 方法
     * @param args   参数列表
     * @return 去除不需要记录的参数
     */
    private InvokeLog.Arg[] getArgs(Method method, Object[] args) {
        List<InvokeLog.Arg> argList = new ArrayList<>(4);
        Parameter[] parameters = method.getParameters();
        for (int i = 0; i < args.length; i++) {
            if (parameters[i].isAnnotationPresent(NoNeedLogParam.class)) {
                continue;
            }
            argList.add(new InvokeLog.Arg(parameters[i].getName(), args[i]));
        }
        return argList.toArray(new InvokeLog.Arg[0]);
    }

    /**
     * 获取实体字段变化记录信息
     *
     * @param args 调用参数信息
     * @return 字段值变化记录
     */
    private List<FieldValueChangeRecord> getChange(InvokeLog.Arg[] args) {
        // 日志上下文存在旧对象，则对比新旧对象字段值变更，并记录
        Object oldObj = LogContext.getVar(LogContext.OLD_OBJ_KEY);
        if (oldObj == null) {
            return Collections.emptyList();
        }

        Object newObj = LogContext.getVar(LogContext.NEW_OBJ_KEY);
        if (newObj == null) {
            // 默认去参数值列表第一个
            newObj = Optional.ofNullable(args[0]).map(InvokeLog.Arg::getValue).orElse(null);
            if (newObj == null) {
                return Collections.emptyList();
            }
        }
        return getFieldValueChangeRecords(newObj, oldObj);
    }

    /**
     * 获取字段值变更记录
     *
     * @param newObj 新对象
     * @param old    旧对象
     * @return 变更记录列表
     */
    @SuppressWarnings("all")
    private List<FieldValueChangeRecord> getFieldValueChangeRecords(Object newObj, Object old) {
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

    /**
     * 添加日志处理器
     *
     * @param logHandler 日志处理器
     */
    public void addLogHandler(LogHandler logHandler) {
        this.logHandlers.add(logHandler);
    }

    private void invokeHandler(InvokeLog invokeLog) {
        if (!loadAllHandler) {
            String[] lhs = applicationContext.getBeanNamesForType(LogHandler.class);
            for (String hn : lhs) {
                addLogHandler(applicationContext.getBean(hn, LogHandler.class));
            }
            loadAllHandler = true;
        }
        logHandlers.forEach(lh -> {
            try {
                lh.handle(invokeLog);
            } catch (Exception e) {
                LOG.error("执行日志处理器失败", e);
            }
        });
    }

    /**
     * 获取系统的日志级别
     *
     * @return 系统日志级别
     */
    private Log.Level getSysLogLevel() {
        if (LOG.isDebugEnabled()) {
            return Log.Level.DEBUG;
        }
        if (LOG.isInfoEnabled()) {
            return Log.Level.INFO;
        }
        if (LOG.isWarnEnabled()) {
            return Log.Level.WARN;
        }
        if (LOG.isErrorEnabled()) {
            return Log.Level.ERROR;
        }
        return Log.Level.NONE;
    }
}
