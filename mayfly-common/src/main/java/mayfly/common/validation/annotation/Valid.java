package mayfly.common.validation.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * @author hml
 * @version 1.0
 * @description:
 * @date 2018-10-28 12:07 PM
 */
@Target({ PARAMETER })
@Retention(RUNTIME)
public @interface Valid {
}
