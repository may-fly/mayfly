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
 * 字符串正则表达式校验
 * @author hml
 * @version 1.0
 * @date 2018-10-28 5:21 PM
 */
@Target({ FIELD, PARAMETER })
@Retention(RUNTIME)
@Documented
@ValidateBy(Pattern.PatternValidator.class)
public @interface Pattern {
    String message() default "";

    String regexp();

    class PatternValidator implements Validator<Pattern, String> {
        @Override
        public ValidResult validation(Pattern pattern, Value<String> value) {
            if (value.getValue() == null) {
                return ValidResult.right();
            }
            if (value.getValue().matches(pattern.regexp())) {
                return ValidResult.right();
            }
            String message = !"".equals(pattern.message()) ? pattern.message() : value.getName() + "参数不符合指定正则！";
            return ValidResult.error(message);
        }
    }
}
