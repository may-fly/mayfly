package mayfly.common.validation.annotation.validator;


import mayfly.common.utils.AnnotationUtils;
import mayfly.common.validation.annotation.NotNull;

import java.lang.reflect.Field;

/**
 * @author hml
 * @version 1.0
 * @description: 非空校验器
 * @date 2018-10-30 10:41 PM
 */
public class NotNullValidator implements Validator {
    @Override
    public ValidResult validation(Field field, Object fieldValue) {
        NotNull notNull = AnnotationUtils.getAnnotation(field, NotNull.class);
        if (notNull != null) {
            if (fieldValue != null) {
                return ValidResult.right();
            }
            String message = "".equals(notNull.message()) ? field.getName() + "值不能为空！" : notNull.message();
            return ValidResult.error(message);
        }
        return ValidResult.right();
    }
}
