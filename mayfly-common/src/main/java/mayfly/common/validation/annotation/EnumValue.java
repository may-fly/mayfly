package mayfly.common.validation.annotation;

import mayfly.common.enums.ValueEnum;
import mayfly.common.util.EnumUtils;
import mayfly.common.util.ObjectUtils;
import mayfly.common.validation.annotation.validator.Validator;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * 枚举值校验
 * @author meilin.huang
 * @version 1.0
 * @date 2019-03-28 5:10 PM
 */
@Target({ FIELD, PARAMETER })
@Retention(RUNTIME)
@Documented
@ValidateBy(EnumValue.EnumValueValidator.class)
public @interface EnumValue {
    /**
     * 枚举值类型，枚举必须继承{@link ValueEnum}
     */
    Class<? extends Enum<? extends ValueEnum>> value();

    String message() default "{fieldName}字段枚举值错误！";


    class EnumValueValidator implements Validator<EnumValue, Integer> {
        @Override
        public boolean validation(EnumValue enumValue, Integer value) {
            if (value == null) {
                return true;
            }
            Class<? extends Enum> enumClass = enumValue.value();
            if (!ValueEnum.class.isAssignableFrom(enumClass)) {
                throw new IllegalArgumentException("@EnumValue注解中的枚举类必须继承ValueEnum接口！");
            }
            //判断字段值是否存在指定的枚举类中
            return EnumUtils.isExist(ObjectUtils.cast(enumClass.getEnumConstants(), ValueEnum.class), value);
        }
    }
}
