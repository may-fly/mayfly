package mayfly.common.validation.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * @author hml
 * @version 1.0
 * @date 2018-10-28 12:07 PM
 */
@Target({ PARAMETER, FIELD })
@Retention(RUNTIME)
public @interface Valid {
}
