package mayfly.common.validation.annotation.validator;


import mayfly.common.utils.AnnotationUtils;
import mayfly.common.validation.annotation.Size;

import java.lang.reflect.Field;

/**
 * @author hml
 * @version 1.0
 * @description: 长度校验器
 * @date 2018-10-31 9:22 AM
 */
public class SizeValidator implements Validator {
    @Override
    public ValidResult validation(Field field, Object fieldValue) {
        Size size = AnnotationUtils.getAnnotation(field, Size.class);
        if (size != null && fieldValue != null) {
            int min = size.min();
            int max = size.max();

            if (fieldValue instanceof String) {
                String value = (String)fieldValue;
                if (value.length() >= min && value.length() <= max) {
                    return ValidResult.right();
                }
                String message = "".equals(size.message()) ? field.getName() + "取值范围错误！" : size.message();
                return ValidResult.error(message);
            }

            if (fieldValue instanceof Integer) {
                Integer value = (Integer)fieldValue;
                if (value >= min && value <= max) {
                    return ValidResult.right();
                }
                String message = "".equals(size.message()) ? field.getName() + "取值范围错误！" : size.message();
                return ValidResult.error(message);
            }
        }
        return ValidResult.right();
    }
}
