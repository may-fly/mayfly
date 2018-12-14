package mayfly.common.validation.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * @author hml
 * @version 1.0
 * @description: 如果字段为字符串类型，则比较其长度，如果为Integer则比较范围
 * @date 2018-10-28 3:49 PM
 */
@Target({ FIELD, PARAMETER })
@Retention(RUNTIME)
public @interface Size {

    String message() default "";

    int min() default 0;

    int max() default Integer.MAX_VALUE;
}
