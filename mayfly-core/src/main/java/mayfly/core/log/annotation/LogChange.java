package mayfly.core.log.annotation;

import mayfly.core.util.enums.NameValueEnum;
import mayfly.core.validation.annotation.EnumValue;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 记录字段改变
 *
 * @author hml
 * @version 1.0
 * @date 2018-09-19 下午1:29
 */
@Target({ElementType.FIELD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface LogChange {
    /**
     * 字段名称
     */
    String name() default "";

    /**
     * 枚举值
     */
    Class<? extends Enum<? extends NameValueEnum>> enumValue() default EnumValue.DefaultNameValueEnum.class;
}
