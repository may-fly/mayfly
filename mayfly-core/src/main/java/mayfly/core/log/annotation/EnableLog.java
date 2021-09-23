package mayfly.core.log.annotation;

import mayfly.core.log.LogProxyAutoConfiguration;
import org.springframework.context.annotation.Import;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 启用日志切面
 *
 * @author meilin.huang
 * @version 1.0
 * @date 2018-11-06 10:41 AM
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import({LogProxyAutoConfiguration.class})
public @interface EnableLog {

    /**
     * 是否使用默认的日志处理器记录日志信息
     */
    boolean useDefaultHandler() default true;
}

