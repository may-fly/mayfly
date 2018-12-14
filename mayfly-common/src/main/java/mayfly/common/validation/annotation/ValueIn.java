package mayfly.common.validation.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * @author hml
 * @version 1.0
 * @description: 值范围
 * @date 2018-10-28 9:23 AM
 */
@Target({ FIELD, PARAMETER })
@Retention(RUNTIME)
public @interface ValueIn {
    /**
     * @return 错误消息
     */
    String message() default "";

    /**
     * @return 字段取值
     */
    String[] values();
}
