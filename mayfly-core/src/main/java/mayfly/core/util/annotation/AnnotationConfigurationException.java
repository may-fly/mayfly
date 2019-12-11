package mayfly.core.util.annotation;

/**
 * 注解配置错误异常
 *
 * @author meilin.huang
 * @version 1.0
 * @date 2019-05-09 17:03
 */
public class AnnotationConfigurationException extends RuntimeException {

    public AnnotationConfigurationException(String msg) {
        super(msg);
    }

    public AnnotationConfigurationException(String msg, Exception e) {
        super(msg, e);
    }
}
