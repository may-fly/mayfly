package mayfly.core.log.annotation;

import java.lang.annotation.*;

/**
 * @author hml
 * @version 1.0
 * @description: 如果不需要记录日志的方法参数需要有该注解
 * @date 2018-09-19 下午1:29
 */
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface NoNeedLogParam {
}
