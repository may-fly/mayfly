package mayfly.common.validation.annotation;

import mayfly.common.validation.annotation.validator.ValidResult;
import mayfly.common.validation.annotation.validator.Validator;
import mayfly.common.validation.annotation.validator.Value;

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

    String message() default "";


    class NotBlankValidator implements Validator<NotBlank, String> {
        @Override
        public ValidResult validation(NotBlank annotation, Value<String> value) {
            if (value.getValue() == null) {
                String message = "".equals(annotation.message()) ? value.getName() + "值不能为空！" : annotation.message();
                return ValidResult.error(message);
            }
            if (!"".equals(value.getValue().trim())) {
                return ValidResult.right();
            }

            String message = "".equals(annotation.message()) ? value.getName() + "值不能为空！" : annotation.message();
            return ValidResult.error(message);
        }
    }
}
