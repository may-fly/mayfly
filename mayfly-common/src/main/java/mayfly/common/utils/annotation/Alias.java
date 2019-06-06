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
     * 别名的属性名称。(即别名的方法名)
     */
    String attribute() default "";

    /**
     * 声明别名属性的注解类型。默认为Annotation，表示别名属性与此属性是在相同的注解中声明。<br/>
     */
    Class<? extends Annotation> annotation() default Annotation.class;
}
