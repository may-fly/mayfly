package mayfly.core.log.handler;

import mayfly.core.exception.BizException;
import mayfly.core.log.FieldValueChangeRecord;
import mayfly.core.log.InvokeLog;
import mayfly.core.log.LogContext;
import mayfly.core.permission.LoginAccount;
import mayfly.core.util.CollectionUtils;
import mayfly.core.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author meilin.huang
 * @date 2021-09-17 7:28 下午
 */
public class DefaultLogHandler implements LogHandler {

    private static final Logger LOG = LoggerFactory.getLogger(DefaultLogHandler.class);

    /**
     * 调用信息前缀
     */
    public static final String INVOKER_FLAG = "\n--> ";

    /**
     * 返回结果模板
     */
    public static final String RESULT_FLAG = "\n<-- ";

    /**
     * 执行时间模板
     */
    public static final String TIME_MSG_TEMP = " -> ";

    /**
     * 异常信息模板
     */
    public static final String EXCEPTION_FLAG = "\n<-e ";

    @Override
    public void handle(InvokeLog invokeLog) {
        String logMsg = getDefaultLogMsg(invokeLog);
        // 将默认日志信息内容放至日志上下文，方便其他日志处理器获取，避免重复获取类似内容
        LogContext.putVar(LogContext.DEFAULT_LOG_MSG, logMsg);

        if (invokeLog.getException() != null) {
            LOG.error(logMsg);
            return;
        }

        switch (invokeLog.getLevel()) {
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

    /**
     * 获取默认的日志内容
     *
     * @param invokeLog 调用日志信息
     * @return 日志内容
     */
    public static String getDefaultLogMsg(InvokeLog invokeLog) {
        Method m = invokeLog.getMethod();

        StringBuilder msg = new StringBuilder();
        Arrays.stream(getTags(invokeLog)).forEach(t -> msg.append("[").append(t).append("]"));
        // 日志调用信息
        msg.append(INVOKER_FLAG).append(m.getDeclaringClass().getName()).append("#").append(m.getName()).append("(");
        boolean first = true;
        for (InvokeLog.Arg arg : invokeLog.getArgs()) {
            if (!first) {
                msg.append(", ");
            } else {
                first = false;
            }
            msg.append(arg.getName()).append(": ").append(arg.getValue());
        }
        msg.append(")");

        // 如果存在异常，则直接返回异常日志信息
        Exception e = invokeLog.getException();
        if (e != null) {
            msg.append(EXCEPTION_FLAG);
            if (e instanceof BizException) {
                BizException bizE = (BizException) e;
                msg.append("[errCode:").append(bizE.getErrorCode()).append(", errMsg:").append(bizE.getMessage()).append("]");
            } else {
                msg.append("sysErr: ").append(e.getMessage());
            }
            return msg.toString();
        }

        // 执行时间
        msg.append(TIME_MSG_TEMP).append(invokeLog.getExecTime()).append("ms");
        // 设置返回结果日志信息
        if (invokeLog.isLogRes()) {
            msg.append(RESULT_FLAG).append(invokeLog.getRes());
        }
        // 获取字段变化记录
        List<FieldValueChangeRecord> changeRecords = invokeLog.getFieldChanges();
        if (CollectionUtils.isEmpty(changeRecords)) {
            return msg.toString();
        }
        msg.append("\n----------change----------");
        changeRecords.forEach(c -> {
            msg.append("\n").append(c.getFieldName()).append(": ").append(c.getOldValue()).append(" -> ").append(c.getNewValue());
        });
        msg.append("\n--------------------------");

        return msg.toString();
    }

    private static String[] getTags(InvokeLog lm) {
        List<String> tags = new ArrayList<>();
        LoginAccount la = LoginAccount.getFromContext();
        if (la != null) {
            tags.add(String.format("uid=%s, uname=%s", la.getId(), la.getUsername()));
        }
        String value = lm.getDescription();
        if (!StringUtils.isEmpty(value)) {
            tags.add(value);
        }
        return tags.toArray(new String[0]);
    }
}
