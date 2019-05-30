package mayfly.common.validation.annotation.validator;;

/**
 * @author hml
 * @version 1.0
 * @description: 校验器接口
 * @date 2018-10-30 8:08 PM
 */
public interface Validator<A, R> {

    /**
     * 校验规则
     * @param annotation 校验规则注解
     * @param value  校验值对象
     * @return  是否符合校验规则
     */
    ValidResult validation(A annotation, Value<R> value);
}
