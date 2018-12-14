package mayfly.common.validation.annotation.validator;

import mayfly.common.validation.annotation.ValueIn;

import java.lang.reflect.Field;
import java.util.Arrays;

/**
 * @author hml
 * @version 1.0
 * @description: 取值校验器
 * @date 2018-10-31 9:11 AM
 */
public class ValueInValidator implements Validator {
    @Override
    public ValidResult validation(Field field, Object fieldValue) {
        ValueIn valueIn = field.getAnnotation(ValueIn.class);
        if (valueIn != null && fieldValue != null) {
            String[] values = valueIn.values();
            //校验字符串
            if (fieldValue instanceof String) {
                String fv = (String)fieldValue;
                for (String value : values) {
                    if (value.equals(fv)) {
                        return ValidResult.right();
                    }
                }
                return ValidResult.error(generateErrorMessage(field.getName(), valueIn));
            }
            //校验Integer类型
            if (fieldValue instanceof Integer) {
                Integer fv = (Integer)fieldValue;
                for (String value : values) {
                    if (fv.equals(Integer.valueOf(value))) {
                        return ValidResult.right();
                    }
                }
                return ValidResult.error(generateErrorMessage(field.getName(), valueIn));
            }

            throw new RuntimeException("@ValueIn只可作用于Integer和String类型");
        }
        return ValidResult.right();
    }

    private String generateErrorMessage(String fieldName, ValueIn valueIn) {
        return "".equals(valueIn.message())
                ? fieldName + "取值错误，可选值为（" + Arrays.toString(valueIn.values()) + "）！"
                : valueIn.message();
    }
}
