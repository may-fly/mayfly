package mayfly.core.log;

import mayfly.core.log.handler.DefaultLogHandler;
import mayfly.core.log.handler.LogHandler;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

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

    private static final Map<Method, LogInfo> LOGS = new ConcurrentHashMap<>(128);

    /**
     * 日志处理器列表
     */
    private List<LogHandler> logHandlers;

    public LogAspect() {
        addLogHandler(new DefaultLogHandler());
    }

    /**
     * 拦截带有@MethodLog的方法或带有该注解的类
     */
    @Pointcut("@annotation(mayfly.core.log.Log) || @within(mayfly.core.log.Log)")
    private void logPointcut() {
    }

    @Around(value = "logPointcut()")
    private Object around(ProceedingJoinPoint pjp) throws Throwable {
        Method method = ((MethodSignature) pjp.getSignature()).getMethod();
        LogInfo lm = LOGS.computeIfAbsent(method, LogInfo::from);
        Log.LogLevel sysLevel = getSysLogLevel();
        Log.LogLevel level = lm.getLevel();
        // 方法的日志级别<系统日志级别直接返回
        if (level.order() < sysLevel.order()) {
            return pjp.proceed();
        }

        InvokeInfo invokeInfo = InvokeInfo.newInstance()
                .sysLevel(getSysLogLevel())
                .args(pjp.getArgs());

        long startTime = System.currentTimeMillis();
        try {
            Object result = pjp.proceed();
            invokeInfo.res(result).execTime(System.currentTimeMillis() - startTime);
            invokeHandler(lm, invokeInfo);
            return result;
        } catch (Exception e) {
            invokeInfo.exception(e);
            invokeHandler(lm, invokeInfo);
            throw e;
        } finally {
            LogContext.clear();
        }
    }

    /**
     * 添加日志处理器
     *
     * @param logHandler 日志处理器
     */
    public void addLogHandler(LogHandler logHandler) {
        if (this.logHandlers == null) {
            this.logHandlers = new ArrayList<>();
        }
        this.logHandlers.add(logHandler);
    }

    private void invokeHandler(LogInfo metadata, InvokeInfo invokeInfo) {
        try {
            logHandlers.forEach(lh -> lh.handle(metadata, invokeInfo));
        } catch (Exception e) {
            LOG.error("执行日志处理器失败", e);
        }
    }

    /**
     * 获取系统的日志级别
     *
     * @return 系统日志级别
     */
    private Log.LogLevel getSysLogLevel() {
        if (LOG.isDebugEnabled()) {
            return Log.LogLevel.DEBUG;
        }
        if (LOG.isInfoEnabled()) {
            return Log.LogLevel.INFO;
        }
        if (LOG.isWarnEnabled()) {
            return Log.LogLevel.WARN;
        }
        if (LOG.isErrorEnabled()) {
            return Log.LogLevel.ERROR;
        }
        return Log.LogLevel.NONE;
    }
}
