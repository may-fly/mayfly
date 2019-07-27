package mayfly.common.validation.annotation;

import mayfly.common.util.StringUtils;
import mayfly.common.validation.annotation.validator.Validator;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * 字符串非空校验
 * @author hml
 * @version 1.0
 * @date 2018-10-27 5:15 PM
 */
@Target({ FIELD, PARAMETER })
@Retention(RUNTIME)
@Documented
@ValidateBy({NotBlank.NotBlankValidator.class})
public @interface NotBlank {

    String message() default "{fieldName}字段值不能为空！";


    class NotBlankValidator implements Validator<NotBlank, String> {
        @Override
        public boolean validation(NotBlank annotation, String value) {
            return !StringUtils.isEmpty(value);
        }
    }
}
