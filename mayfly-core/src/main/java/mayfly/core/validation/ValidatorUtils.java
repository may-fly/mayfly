package mayfly.core.validation;

import mayfly.core.constant.PatternConst;
import mayfly.core.util.CollectionUtils;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.util.Set;
import java.util.regex.Pattern;

/**
 * @author meilin.huang
 * @date 2020-04-14 10:22 上午
 */
public class ValidatorUtils {


    public static Pattern mobilePattern = Pattern.compile(PatternConst.MOBILE);

    public static Pattern emailPattern = Pattern.compile("^\\w+([-+./]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*$");

    public static Pattern chinesePattern = Pattern.compile(PatternConst.CHINESE);

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

    /**
     * 判断字符串是否属于手机格式
     *
     * @param str 字符串
     * @return true or false
     */
    public static boolean isMobile(String str) {
        return mobilePattern.matcher(str).matches();
    }

    /**
     * 邮件
     *
     * @param input 字符串
     * @return true:是|false:否
     */
    public static boolean isEmail(String input) {
        return emailPattern.matcher(input).matches();
    }

    /**
     * 纯中文
     *
     * @param input 字符串
     * @return true:是|false:否
     */
    public static boolean isChinese(String input) {
        return chinesePattern.matcher(input).matches();
    }
}
