package mayfly.common.validation.annotation.validator;


import mayfly.common.utils.ReflectionUtils;
import mayfly.common.validation.annotation.NotBlank;

import java.lang.reflect.Field;

/**
 * @author hml
 * @version 1.0
 * @description: 字符串非空校验
 * @date 2018-10-30 10:43 PM
 */
public class NotBlankValidator implements Validator {

    @Override
    public ValidResult validation(Field field, Object fieldValue) {
        NotBlank notBlank = ReflectionUtils.getFieldAnnotation(field, NotBlank.class);
        if (notBlank != null) {
            if (fieldValue == null) {
                String message = "".equals(notBlank.message()) ? field.getName() + "值不能为空！" : notBlank.message();
                return ValidResult.error(message);
            }

            if (!(fieldValue instanceof String)) {
                throw new RuntimeException("@NotBlank注解必须作用于String类型");
            }
            String value = (String)fieldValue;
            if (!"".equals(value.trim())) {
                return ValidResult.right();
            }

            String message = "".equals(notBlank.message()) ? field.getName() + "值不能为空！" : notBlank.message();
            return ValidResult.error(message);
        }
        return ValidResult.right();
    }
}
