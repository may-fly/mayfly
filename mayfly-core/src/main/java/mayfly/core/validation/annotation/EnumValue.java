package mayfly.core.validation.annotation;

import mayfly.core.util.enums.EnumUtils;
import mayfly.core.util.enums.NameValueEnum;
import mayfly.core.util.enums.ValueEnum;
import org.hibernate.validator.constraintvalidation.HibernateConstraintValidatorContext;

import javax.validation.Constraint;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.Arrays;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 枚举值校验，即值只能是指定枚举类中的value值
 *
 * @author meilin.huang
 * @version 1.0
 * @date 2020-04-14 10:22 上午
 */
@Target({ElementType.METHOD, ElementType.FIELD, ElementType.ANNOTATION_TYPE, ElementType.CONSTRUCTOR, ElementType.PARAMETER, ElementType.TYPE_USE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = EnumValue.EnumValueValidator.class)
public @interface EnumValue {

    /**
     * 枚举类(必须实现{@link ValueEnum}接口的枚举)
     */
    @SuppressWarnings("rawtypes")
    Class<? extends Enum<? extends ValueEnum>> value();

    /**
     * 字段名
     */
    String name() default "";

    /**
     * 错误提示
     */
    String message() default "{name}枚举值错误，可选值为{enums}";

    /**
     * 用于分组校验
     */
    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};


    class EnumValueValidator implements ConstraintValidator<EnumValue, Object> {

        @SuppressWarnings("rawtypes")
        private Class<? extends Enum<? extends ValueEnum>> enumClass;

        @Override
        public void initialize(EnumValue enumValue) {
            this.enumClass = enumValue.value();
        }

        @SuppressWarnings({"rawtypes", "unchecked"})
        @Override
        public boolean isValid(Object value, ConstraintValidatorContext context) {
            if (value == null) {
                return true;
            }

            Enum<? extends ValueEnum>[] enums = enumClass.getEnumConstants();
            ValueEnum[] valueEnums = (ValueEnum[]) enums;
            if (EnumUtils.isExist(valueEnums, value)) {
                return true;
            }

            String enumsPlaceholderValue;
            // 如果是NameValueEnum类型，则返回的错误信息带有name属性值
            if (NameValueEnum.class.isAssignableFrom(enumClass)) {
                enumsPlaceholderValue = Arrays.stream((NameValueEnum[]) enums).map(nv -> nv.getValue() + ":" + nv.getName())
                        .collect(Collectors.joining(", "));
            } else {
                enumsPlaceholderValue = Arrays.stream(valueEnums).map(nv -> Objects.toString(nv.getValue()))
                        .collect(Collectors.joining(", "));
            }

            // 添加枚举值占位符值参数，校验失败的时候可用
            HibernateConstraintValidatorContext hibernateContext = context.unwrap(HibernateConstraintValidatorContext.class);
            hibernateContext.addMessageParameter("enums", "[" + enumsPlaceholderValue + "]");
            return false;
        }
    }
}