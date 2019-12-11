package mayfly.core.validation.annotation;

import mayfly.core.validation.annotation.validator.Validator;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * 字符串正则表达式校验
 *
 * @author hml
 * @version 1.0
 * @date 2018-10-28 5:21 PM
 */
@Target({FIELD, PARAMETER, ANNOTATION_TYPE})
@Retention(RUNTIME)
@Documented
@ValidateBy(Pattern.PatternValidator.class)
public @interface Pattern {

    String message() default "{fieldName}字段值不满足指定正则！";

    String regexp();


    class PatternValidator implements Validator<Pattern, String> {
        @Override
        public boolean validation(Pattern pattern, String value) {
            return value == null || value.matches(pattern.regexp());
        }
    }
}
