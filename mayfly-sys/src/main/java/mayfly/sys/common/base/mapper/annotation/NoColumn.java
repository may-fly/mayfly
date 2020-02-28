package mayfly.sys.common.base.mapper.annotation;

import java.lang.annotation.*;

/**
 * 对象属性不映射到数据库注解（可作用于字段上，或类型指定哪些字段不映射）
 *
 * @author hml
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface NoColumn {
    /**
     * 作用于类型指定不需要映射的字段名数组
     */
    String[] fields() default {};
}
