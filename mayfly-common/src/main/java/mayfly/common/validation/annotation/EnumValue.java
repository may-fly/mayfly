package mayfly.common.validation.annotation;

import mayfly.common.enums.ValueEnum;

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
public @interface EnumValue {
    /**
     * 枚举值类型，枚举必须继承{@link ValueEnum}
     */
    Class<? extends Enum<? extends ValueEnum>> value();
}
