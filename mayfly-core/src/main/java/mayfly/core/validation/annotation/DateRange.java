package mayfly.core.validation.annotation;

import mayfly.core.util.annotation.OverrideFor;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * 日期范围校验注解，可作用于 {@link java.util.Date} 以及 {@link java.time.LocalDate}
 * @author meilin.huang
 * @version 1.0
 * @date 2019-05-31 22:47
 */
@Target({ FIELD, PARAMETER, ElementType.TYPE})
@Retention(RUNTIME)
@Documented
@DatePattern
public @interface DateRange {

    String value() default "";

    @OverrideFor(attribute = "pattern", annotation = DatePattern.class)
    String pattern() default "";

//    @OverrideFor(attribute = "value")
    String before() default "";
}
