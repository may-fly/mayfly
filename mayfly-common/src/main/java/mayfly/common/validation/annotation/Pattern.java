package mayfly.common.validation.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * @author hml
 * @version 1.0
 * @description: 正则表达式校验
 * @date 2018-10-28 5:21 PM
 */
@Target({ FIELD, PARAMETER })
@Retention(RUNTIME)
public @interface Pattern {
    String message() default "";

    String regexp();
}
