package mayfly.core.validation.annotation;

import mayfly.core.exception.BizAssert;
import mayfly.core.util.ArrayUtils;
import mayfly.core.util.Assert;
import mayfly.core.util.StringUtils;
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
    Class<? extends Enum<? extends ValueEnum>> value() default DefaultEnum.class;

    /**
     * 可选枚举值
     */
    String[] values() default {};

    /**
     * 字段名,如果字段名称为空，默认为枚举类去掉Enum标志且首字母小写（如：TestTypeEnum -> testType）
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

        private EnumValue enumValue;

        @Override
        public void initialize(EnumValue enumValue) {
            this.enumValue = enumValue;
        }

        @SuppressWarnings({"rawtypes", "unchecked"})
        @Override
        public boolean isValid(Object value, ConstraintValidatorContext context) {
            if (value == null) {
                return true;
            }

            String[] values = enumValue.values();
            Class<? extends Enum<? extends ValueEnum>> enumClass = enumValue.value();
            Assert.isTrue(!ArrayUtils.isEmpty(values) || enumClass != DefaultEnum.class, "@EnumValue注解的values和value不能同时为默认值");
            // 如果限制了可选枚举值，则使用可选枚举值判断
            if (!ArrayUtils.isEmpty(values)) {
                if (value instanceof Integer) {
                    for (String v : values) {
                        if (value.equals(Integer.parseInt(v))) {
                            return true;
                        }
                    }
                    setErrorEnumPlaceholderValue(Arrays.stream(values).map(Integer::parseInt).toArray(), context);
                    return false;
                } else if (value instanceof String) {
                    if (ArrayUtils.contains(values, value)) {
                        return true;
                    }
                    setErrorEnumPlaceholderValue(values, context);
                    return false;
                }
                throw BizAssert.newException("@EnumValue只支持Integer和String类型的枚举值参数，暂不支持其他类型！");
            }

            if (EnumUtils.isExist((ValueEnum[]) enumValue.value().getEnumConstants(), value)) {
                return true;
            }
            // 添加枚举值占位符值参数，校验失败的时候可用
            setErrorEnumPlaceholderValue(null, context);
            return false;
        }

        /**
         * 设置错误提示消息中的enum和name占位符值
         *
         * @param values  可选值数组
         * @param context context
         */
        @SuppressWarnings({"rawtypes"})
        private void setErrorEnumPlaceholderValue(Object[] values, ConstraintValidatorContext context) {
            String message = enumValue.message();
            // message如果不包含name和enums占位符，则直接返回
            if (!message.contains("{name}") && !message.contains("{enums}")) {
                return;
            }
            Class<? extends Enum<? extends ValueEnum>> enumClass = enumValue.value();

            String enumsPlaceholderValue;
            // 如果是NameValueEnum类型，则返回的错误信息带有name属性值
            if (enumClass == DefaultEnum.class) {
                enumsPlaceholderValue = Arrays.stream(values).map(Object::toString).collect(Collectors.joining(", "));
            } else {
                Enum<? extends ValueEnum>[] enums = enumClass.getEnumConstants();
                ValueEnum[] valueEnums = (ValueEnum[]) enums;
                if (NameValueEnum.class.isAssignableFrom(enumClass)) {
                    if (ArrayUtils.isEmpty(values)) {
                        enumsPlaceholderValue = Arrays.stream((NameValueEnum[]) enums).map(nv -> nv.getValue() + ":" + nv.getName())
                                .collect(Collectors.joining(", "));
                    } else {
                        enumsPlaceholderValue = Arrays.stream((NameValueEnum[]) enums)
                                .filter(x -> ArrayUtils.contains(values, x.getValue()))
                                .map(nv -> nv.getValue() + ":" + nv.getName())
                                .collect(Collectors.joining(", "));
                    }
                } else {
                    if (ArrayUtils.isEmpty(values)) {
                        enumsPlaceholderValue = Arrays.stream(valueEnums).map(nv -> Objects.toString(nv.getValue()))
                                .collect(Collectors.joining(", "));
                    } else {
                        enumsPlaceholderValue = Arrays.stream(valueEnums)
                                .filter(x -> ArrayUtils.contains(values, x.getValue()))
                                .map(nv -> Objects.toString(nv.getValue()))
                                .collect(Collectors.joining(", "));
                    }
                }
            }
            // 添加枚举值占位符值参数，校验失败的时候可用
            HibernateConstraintValidatorContext hibernateContext = context.unwrap(HibernateConstraintValidatorContext.class);
            hibernateContext.addMessageParameter("enums", "[" + enumsPlaceholderValue + "]");
            // 如果字段名称为空，默认为枚举类去掉Enum标志且首字母小写（如：TestTypeEnum -> testType）
            if (StringUtils.isEmpty(enumValue.name()) && enumClass != DefaultEnum.class) {
                String defaultName = enumClass.getSimpleName();
                if (defaultName.endsWith("Enum")) {
                    defaultName = Character.toLowerCase(defaultName.charAt(0)) + defaultName.substring(1, defaultName.length() - 4);
                } else {
                    defaultName = Character.toLowerCase(defaultName.charAt(0)) + defaultName.substring(1);
                }
                hibernateContext.addMessageParameter("name", defaultName);
            }
        }
    }


    enum DefaultEnum implements ValueEnum<Integer> {
        ;

        @Override
        public Integer getValue() {
            return 0;
        }
    }

    enum DefaultNameValueEnum implements NameValueEnum<Integer> {
        ;

        @Override
        public Integer getValue() {
            return 0;
        }

        @Override
        public String getName() {
            return null;
        }
    }

}