package mayfly.core.validation;

import mayfly.core.result.Result;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;

import java.lang.reflect.Method;

/**
 * 参数校验切面
 *
 * @author hml
 * @version 1.0
 * @date 2018-10-28 1:17 PM
 */
@Aspect
public class ParamValidAspect {

    @Pointcut(value = "execution(* *..*Controller.*(..))")
    private void controller() {
    }

    @Around("controller()")
    public Object validateAround(ProceedingJoinPoint joinPoint) throws Throwable {
        MethodSignature ms = (MethodSignature) joinPoint.getSignature();
        Method method = ms.getMethod();
        Object[] args = joinPoint.getArgs();

        try {
            AopParamValidator.getInstance().validate(method, args);
        } catch (ParamValidErrorException e) {
            return Result.paramError(e.getMessage());
        }
        return joinPoint.proceed();
    }
}
