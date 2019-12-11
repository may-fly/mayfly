package mayfly.core.validation.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * 用于标识对象需要进行参数校验
 *
 * @author hml
 * @version 1.0
 * @date 2018-10-28 12:07 PM
 */
@Target({PARAMETER, FIELD})
@Retention(RUNTIME)
public @interface Valid {
}
