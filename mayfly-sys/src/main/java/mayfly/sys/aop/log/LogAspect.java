package mayfly.sys.aop.log;

import mayfly.common.log.LogHandler;
import mayfly.common.log.LogInfo;
import mayfly.common.log.LogResult;
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

    @Pointcut(value = "@annotation(mayfly.common.log.MethodLog)")
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

        LOG.info(logInfo.fillLogMsg(new LogResult(args, result, endTime - startTime)));
        return result;
    }
}
