package mayfly.common.validation.annotation.validator;

import mayfly.common.validation.annotation.Pattern;

import java.lang.reflect.Field;

/**
 * @author hml
 * @version 1.0
 * @description: 正则校验器
 * @date 2018-10-31 9:49 AM
 */
public class PatternValidator implements Validator {
    @Override
    public ValidResult validation(Field field, Object fieldValue) {
        Pattern pattern = field.getAnnotation(Pattern.class);
        if (pattern != null && fieldValue != null) {
            if (fieldValue instanceof String) {
                String value = (String)fieldValue;
                if (value.matches(pattern.regexp())) {
                    return ValidResult.right();
                }
                String message = !"".equals(pattern.message()) ? pattern.message() : field.getName() + "参数不符合指定正则！";
                return ValidResult.error(message);
            }

            throw new RuntimeException("@Pattern必须作用于String类型");
        }
        return ValidResult.right();
    }
}
