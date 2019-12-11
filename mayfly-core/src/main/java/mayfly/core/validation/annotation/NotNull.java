package mayfly.core.validation.annotation;

import mayfly.core.validation.annotation.validator.Validator;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * 对象非null校验
 *
 * @author hml
 * @version 1.0
 * @date 2018-10-27 5:10 PM
 */
@Target({FIELD, PARAMETER})
@Retention(RUNTIME)
@Documented
@ValidateBy({NotNull.NotNullValidator.class})
public @interface NotNull {

    String message() default "{fieldName}字段值不能为空！";


    class NotNullValidator implements Validator<NotNull, Object> {
        @Override
        public boolean validation(NotNull annotation, Object value) {
            return value != null;
        }
    }
}
