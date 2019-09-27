package mayfly.sys.aop.log;

import mayfly.core.log.LogHandler;
import mayfly.core.log.LogInfo;
import mayfly.core.log.LogResult;
import mayfly.core.log.MethodLog;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

/**
 * @author hml
 * @version 1.0
 * @description: 日志切面
 * @date 2018-09-19 上午9:16
 */
@Aspect
@Component
public class LogAspect {

    private static final Logger LOG = LoggerFactory.getLogger(LogAspect.class);

    private LogHandler handler = LogHandler.getInstance();

    /**
     * 拦截带有@MethodLog的方法或带有该注解的类
     */
    @Pointcut("@annotation(mayfly.common.log.MethodLog) || @within(mayfly.common.log.MethodLog)")
    private void logPointcut() {}

    @AfterThrowing(pointcut = "logPointcut()", throwing="e")
    public void doException(JoinPoint jp, Exception e){
        Object[] args = jp.getArgs();
        Method method = ((MethodSignature)jp.getSignature()).getMethod();
        LogInfo logInfo = handler.getLogInfo(method);
        LOG.error(logInfo.getExceptionLogMsg(LogResult.exception(args, e)));
    }

    @Around(value = "logPointcut()")
    private Object afterReturning(ProceedingJoinPoint pjp) throws Throwable {
        Object[] args = pjp.getArgs();
        Method method = ((MethodSignature)pjp.getSignature()).getMethod();
        LogInfo logInfo = handler.getLogInfo(method);

        long startTime = System.currentTimeMillis();
        Object result = pjp.proceed();
        long endTime = System.currentTimeMillis();

        MethodLog.LogLevel sysLogLevel = getSysLogLevel();
        String logMsg = logInfo.fillLogMsg(sysLogLevel, new LogResult(args, result, endTime - startTime));
        if (logMsg == null) {
            return result;
        }

        switch (logInfo.getLevel()) {
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

        return result;
    }

    /**
     * 获取系统的日志级别
     * @return
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
