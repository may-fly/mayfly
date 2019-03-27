package mayfly.common.log;

import java.lang.annotation.*;

/**
 * @author meilin.huang
 * @version 1.0
 * @description:
 * @date 2018-11-06 10:41 AM
 */
@Target({ElementType.PARAMETER, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface MethodLog {
    String value() default "";

    /**
     * 是否记录返回结果，默认true
     */
    boolean result() default false;

    /**
     * 是否记录执行时间
     */
    boolean time() default false;
}

