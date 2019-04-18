package mayfly.common.validation.annotation.validator;

import mayfly.common.enums.BaseEnum;
import mayfly.common.utils.AnnotationUtils;
import mayfly.common.utils.EnumUtils;
import mayfly.common.utils.ObjectUtils;
import mayfly.common.validation.annotation.EnumValue;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.EnumSet;

/**
 * EnumValue注解校验器
 * @author meilin.huang
 * @version 1.0
 * @date 2019-03-28 5:11 PM
 */
public class EnumValueValidator implements Validator {

    @Override
    public ValidResult validation(Field field, Object fieldValue) {
        EnumValue enumValue = AnnotationUtils.getAnnotation(field, EnumValue.class);
        if (enumValue != null && fieldValue != null) {
            if (fieldValue instanceof Integer) {
                Class<? extends Enum> enumClass = enumValue.value();
                if (!BaseEnum.class.isAssignableFrom(enumClass)) {
                    throw new IllegalArgumentException("@EnumValue注解中的枚举类必须继承BaseEnum接口！");
                }
                Collection<? extends Enum> es = EnumSet.allOf(enumClass);
                if (EnumUtils.isExist(ObjectUtils.castArray(es.toArray(), BaseEnum.class), (Integer)fieldValue)) {
                    return ValidResult.right();
                }
                return ValidResult.error(field.getName() + "字段值错误！");
            }
        }
        return ValidResult.right();
    }
}
