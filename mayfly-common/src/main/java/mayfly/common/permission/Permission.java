package mayfly.common.permission;

import java.lang.annotation.*;

/**
 * 权限注解，作用于方法上，含有该注解的方法需要权限验证
 * @author: meilin.huang
 * @date: 2018/6/28 下午3:27
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Permission {
    /**
     * 默认需要登录
     */
    boolean login() default true;

    String code();
}
