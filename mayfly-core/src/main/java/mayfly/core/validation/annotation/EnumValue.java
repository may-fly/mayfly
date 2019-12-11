package mayfly.core.validation.annotation;

import mayfly.core.util.enums.ValueEnum;
import mayfly.core.util.Assert;
import mayfly.core.util.enums.EnumUtils;
import mayfly.core.util.ObjectUtils;
import mayfly.core.validation.annotation.validator.Validator;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * 枚举值校验
 *
 * @author meilin.huang
 * @version 1.0
 * @date 2019-03-28 5:10 PM
 */
@Target({FIELD, PARAMETER})
@Retention(RUNTIME)
@Documented
@ValidateBy(EnumValue.EnumValueValidator.class)
public @interface EnumValue {
    /**
     * 枚举值类型，枚举必须继承{@link ValueEnum}
     */
    Class<? extends Enum<? extends ValueEnum<?>>> value();

    String message() default "{fieldName}字段枚举值错误！";


    class EnumValueValidator implements Validator<EnumValue, Object> {
        @SuppressWarnings("all")
        @Override
        public boolean validation(EnumValue enumValue, Object value) {
            if (value == null) {
                return true;
            }
            Class<? extends Enum> enumClass = enumValue.value();
            Assert.assertState(ValueEnum.class.isAssignableFrom(enumClass),
                    "@EnumValue注解中的枚举类必须继承ValueEnum接口！");
            // 判断字段值是否存在指定的枚举类中
            return EnumUtils.isExist(ObjectUtils.cast(enumClass.getEnumConstants(), ValueEnum.class), value);
        }
    }
}