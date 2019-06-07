package mayfly.common.utils.annotation;

import java.lang.annotation.*;

/**
 * @author meilin.huang
 * @version 1.0
 * @date 2019-05-09 14:36
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Documented
public @interface Alias {

    String value() default "";

    /**
     * 别名的属性名称(即别名的方法名)
     * 为空则为作用的注解方法元素的方法名
     */
    String attribute() default "";

    /**
     * 声明要覆盖的目标注解的别名属性的注解类型<br/>
     */
    Class<? extends Annotation> annotation();
}
