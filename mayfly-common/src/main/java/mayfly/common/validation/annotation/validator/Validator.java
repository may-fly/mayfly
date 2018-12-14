package mayfly.common.validation.annotation.validator;;

import java.lang.reflect.Field;

/**
 * @author hml
 * @version 1.0
 * @description: 校验器接口
 * @date 2018-10-30 8:08 PM
 */
public interface Validator {

    /**
     * 校验规则
     * @param field 字段对象
     * @param fieldValue  字段值
     * @return  是否符合校验规则
     */
    ValidResult validation(Field field, Object fieldValue);
}
