package mayfly.common.validation.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * @author hml
 * @version 1.0
 * @description: 字符串非空校验
 * @date 2018-10-27 5:15 PM
 */
@Target({ FIELD, PARAMETER })
@Retention(RUNTIME)
public @interface NotBlank {
    String message() default "";
}
