package mayfly.common.validation.annotation.validator;


import mayfly.common.utils.AnnotationUtils;
import mayfly.common.validation.annotation.Size;

import java.lang.reflect.Field;
import java.util.Collection;

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
                int len = value.length();
                if (len >= min && len <= max) {
                    return ValidResult.right();
                }
                return errorResult(field, size);
            }

            if (fieldValue instanceof Number) {
                Number value = (Number) fieldValue;
                int val = value.intValue();
                if (val >= min && val <= max) {
                    return ValidResult.right();
                }
               return errorResult(field, size);
            }

            if (fieldValue instanceof Collection) {
                Collection value = (Collection)fieldValue;
                int len = value.size();
                if (len >= min && len <= max) {
                    return ValidResult.right();
                }
                return errorResult(field, size);
            }
        }
        return ValidResult.right();
    }

    private ValidResult errorResult(Field field, Size size) {
        String message = "".equals(size.message()) ? field.getName() + "范围错误！" : size.message();
        return ValidResult.error(message);
    }
}
