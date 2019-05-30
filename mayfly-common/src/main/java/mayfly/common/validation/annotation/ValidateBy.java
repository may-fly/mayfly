package mayfly.common.validation.annotation;

import mayfly.common.validation.annotation.validator.Validator;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * 作用于校验规则注解类上，指明用何种校验器（实现了Validator接口）校验参数值
 * @author meilin.huang
 * @version 1.0
 * @date 2019-05-30 13:36
 */
@Documented
@Target({ ANNOTATION_TYPE })
@Retention(RUNTIME)
public @interface ValidateBy {

    /**
     * 参数校验器
     */
    Class<? extends Validator>[] value();
}
