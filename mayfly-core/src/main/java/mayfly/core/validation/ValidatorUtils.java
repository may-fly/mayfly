package mayfly.core.validation;

import mayfly.core.util.CollectionUtils;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.util.Set;

/**
 * @author meilin.huang
 * @date 2020-04-14 10:22 上午
 */
public class ValidatorUtils {

    /**
     * 校验器
     */
    private static final Validator VALIDATOR = Validation.buildDefaultValidatorFactory().getValidator();

    private ValidatorUtils() {
    }
    
    /**
     * 校验对象是否符合指定校验规则
     *
     * @param obj        对象
     * @param groupClass 校验组
     * @return 校验结果字符串数组，为null则为符合约束校验
     */
    public static String[] validate(Object obj, Class<?>... groupClass) {
        Set<ConstraintViolation<Object>> result = VALIDATOR.validate(obj, groupClass);
        if (CollectionUtils.isEmpty(result)) {
            return null;
        }

        return result.stream().map(ConstraintViolation::getMessage).toArray(String[]::new);
    }
}
