package mayfly.core.validation.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * @author meilin.huang
 * @version 1.0
 * @date 2019-06-02 15:09
 */
@Documented
@Target({ ANNOTATION_TYPE, FIELD })
@Retention(RUNTIME)
public @interface DatePattern {

    /**
     * 日期格式类型
     */
    String pattern() default "yyyy-MM-dd";
}
