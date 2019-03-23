package mayfly.sys.aop.valid;

import mayfly.common.result.Result;
import mayfly.common.validation.ParamValidErrorException;
import mayfly.common.validation.aop.AopParamValidator;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

/**
 * @author hml
 * @version 1.0
 * @description: 参数校验
 * @date 2018-10-28 1:17 PM
 */
@Aspect
@Component
public class ParamValid {

    @Pointcut(value = "execution(* mayfly.sys.web..*Controller.*(..))")
    private void controller() {}

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
