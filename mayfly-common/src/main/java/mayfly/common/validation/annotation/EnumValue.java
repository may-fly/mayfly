package mayfly.common.validation.annotation;

import mayfly.common.enums.BaseEnum;

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
     * 枚举值类型，必须继承{@link BaseEnum}
     */
    Class<? extends Enum<? extends BaseEnum>> value();
}
