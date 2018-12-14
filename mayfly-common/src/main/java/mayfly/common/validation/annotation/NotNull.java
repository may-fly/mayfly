package mayfly.common.validation.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * @author hml
 * @version 1.0
 * @description:
 * @date 2018-10-27 5:10 PM
 */
@Target({ FIELD, PARAMETER })
@Retention(RUNTIME)
public @interface NotNull {
    String message() default "";
}
