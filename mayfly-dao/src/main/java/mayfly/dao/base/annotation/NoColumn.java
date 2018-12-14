package mayfly.dao.base.annotation;

import java.lang.annotation.*;

/**
 * 数据库对象不映射注解
 * Created by jaseeka
 * Date 2015/11/18
 * Time 14:35
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface NoColumn {

}
