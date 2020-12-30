package mayfly.core.log;

import mayfly.core.exception.BizRuntimeException;
import mayfly.core.permission.LoginAccount;
import mayfly.core.thread.GlobalThreadPool;
import mayfly.core.util.ArrayUtils;
import mayfly.core.util.StringUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.function.BiConsumer;

/**
 * 日志切面
 *
 * @author hml
 * @version 1.
 * @date 2018-09-19 上午9:16
 */
@Aspect
public class LogAspect {

    private static final Logger LOG = LoggerFactory.getLogger(LogAspect.class);

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
     * 日志结果消费者（回调）,主要用于保存日志信息等
     */
    private BiConsumer<MethodLog.LogLevel, String> logConsumer;

    public LogAspect() {
    }

    public LogAspect(BiConsumer<MethodLog.LogLevel, String> logConsumer) {
        this.logConsumer = logConsumer;
    }

    /**
     * 拦截带有@MethodLog的方法或带有该注解的类
     */
    @Pointcut("@annotation(mayfly.core.log.MethodLog) || @within(mayfly.core.log.MethodLog)")
    private void logPointcut() {
    }

    @AfterThrowing(pointcut = "logPointcut()", throwing = "e")
    public void doException(JoinPoint jp, Exception e) {
        Method method = ((MethodSignature) jp.getSignature()).getMethod();
        MethodLog methodLog = getMethodLog(method);
        String logMsg = getInvokeInfo(method, methodLog, jp.getArgs()) + EXCEPTION_FLAG;
        if (e instanceof BizRuntimeException) {
            BizRuntimeException bizE = (BizRuntimeException) e;
            logMsg = logMsg + "[errCode:" + bizE.getErrorCode() + ", errMsg:" + bizE.getMessage() + "]";
        } else {
            logMsg = logMsg + "sysErr: " + e.getMessage();
        }
        // 执行回调
        execLogConsumer(MethodLog.LogLevel.ERROR, logMsg);
        LOG.error(logMsg);
    }

    @Around(value = "logPointcut()")
    private Object around(ProceedingJoinPoint pjp) throws Throwable {
        Method method = ((MethodSignature) pjp.getSignature()).getMethod();
        MethodLog methodLog = getMethodLog(method);
        int sysLevel = getSysLogLevel().order();
        MethodLog.LogLevel level = methodLog.level();
        // 方法的日志级别<系统日志级别直接返回
        if (level.order() < sysLevel) {
            return pjp.proceed();
        }

        long startTime = System.currentTimeMillis();
        Object result = pjp.proceed();
        String logMsg = getInvokeInfo(method, methodLog, pjp.getArgs()) + TIME_MSG_TEMP + (System.currentTimeMillis() - startTime) + "ms";
        if (method.getReturnType() != Void.TYPE && methodLog.resultLevel().order() >= sysLevel) {
            logMsg = logMsg + RESULT_FLAG + result;
        }
        switch (level) {
            case DEBUG:
                LOG.debug(logMsg);
                break;
            case WARN:
                LOG.warn(logMsg);
                break;
            case INFO:
                LOG.info(logMsg);
                break;
            case ERROR:
                LOG.error(logMsg);
                break;
            default:
                LOG.info(logMsg);
                break;
        }
        execLogConsumer(level, logMsg);
        return result;
    }

    private String getInvokeInfo(Method m, MethodLog ml, Object[] args) {
        StringBuilder invokeInfo = new StringBuilder();
        Arrays.stream(getTags(ml)).forEach(t -> invokeInfo.append("[").append(t).append("]"));
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
        return invokeInfo.append(")").toString();
    }

    private String[] getTags(MethodLog ml) {
        List<String> tags = new ArrayList<>();
        LoginAccount la = LoginAccount.getFromContext();
        if (la != null) {
            tags.add("uid=" + la.getId() + ", " + "uname=" + la.getUsername());
        }
        String value = ml.value();
        if (!StringUtils.isEmpty(value)) {
            tags.add(value);
        }
        return tags.toArray(new String[0]);
    }

    private MethodLog getMethodLog(Method m) {
        return Optional.ofNullable(m.getAnnotation(MethodLog.class))
                .orElse(m.getDeclaringClass().getAnnotation(MethodLog.class));
    }

    /**
     * 执行日志消息消费者
     *
     * @param level  日志级别
     * @param logMsg 日志内容
     */
    private void execLogConsumer(MethodLog.LogLevel level, String logMsg) {
        if (logConsumer != null) {
            GlobalThreadPool.execute(() -> {
                try {
                    logConsumer.accept(level, logMsg);
                } catch (Exception e) {
                    LOG.error("执行log consumer失败：", e);
                }
            });
        }
    }

    /**
     * 获取系统的日志级别
     *
     * @return 系统日志级别
     */
    private MethodLog.LogLevel getSysLogLevel() {
        if (LOG.isDebugEnabled()) {
            return MethodLog.LogLevel.DEBUG;
        }
        if (LOG.isInfoEnabled()) {
            return MethodLog.LogLevel.INFO;
        }
        if (LOG.isWarnEnabled()) {
            return MethodLog.LogLevel.WARN;
        }
        if (LOG.isErrorEnabled()) {
            return MethodLog.LogLevel.ERROR;
        }
        return MethodLog.LogLevel.NONE;
    }
}
