package mayfly.core.log;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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

    private final LogHandler handler = LogHandler.getInstance();

    /**
     * 日志结果消费者（回调）,主要用于保存日志信息等
     */
    private BiConsumer<LogTypeEnum, String> saveLogConsumer;

    public LogAspect() {
    }

    public LogAspect(BiConsumer<LogTypeEnum, String> saveLogConsumer) {
        this.saveLogConsumer = saveLogConsumer;
    }

    /**
     * 拦截带有@MethodLog的方法或带有该注解的类
     */
    @Pointcut("@annotation(mayfly.core.log.MethodLog) || @within(mayfly.core.log.MethodLog)")
    private void logPointcut() {
    }

    @AfterThrowing(pointcut = "logPointcut()", throwing = "e")
    public void doException(JoinPoint jp, Exception e) {
        LogHandler.LogInfo logInfo = handler.getLogInfo(((MethodSignature) jp.getSignature()).getMethod());
        String errMsg = logInfo.getExceptionLogMsg(LogHandler.LogResult.exception(jp.getArgs(), e));
        // 执行回调
        if (saveLogConsumer != null) {
            try {
                saveLogConsumer.accept(LogTypeEnum.EXCEPTION, errMsg);
            } catch (Exception ex) {
                LOG.error("执行log consumer失败：", ex);
            }
        }
        LOG.error(errMsg);
    }

    @Around(value = "logPointcut()")
    private Object afterReturning(ProceedingJoinPoint pjp) throws Throwable {
        LogHandler.LogInfo logInfo = handler.getLogInfo(((MethodSignature) pjp.getSignature()).getMethod());

        long startTime = System.currentTimeMillis();
        Object result = pjp.proceed();

        String logMsg = logInfo.fillLogMsg(getSysLogLevel(), new LogHandler.LogResult(pjp.getArgs(), result,
                System.currentTimeMillis() - startTime));
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
        if (saveLogConsumer != null) {
            try {
                saveLogConsumer.accept(LogTypeEnum.NORMAN, logMsg);
            } catch (Exception e) {
                LOG.error("执行log consumer失败：", e);
            }
        }
        return result;
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
