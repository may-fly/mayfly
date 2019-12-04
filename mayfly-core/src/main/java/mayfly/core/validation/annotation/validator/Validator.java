package mayfly.core.validation.annotation.validator;

import java.lang.annotation.Annotation;

;

/**
 * 校验器接口
 * @author hml
 * @version 1.0
 * @date 2018-10-30 8:08 PM
 */
public interface Validator<A extends Annotation, V> {

    /**
     * 校验规则
     * @param annotation 校验规则注解
     * @param value  校验值对象
     * @return  是否符合校验规则
     */
    boolean validation(A annotation, V value);
}
