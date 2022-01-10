package mayfly.core.constant;

/**
 * @author meilin.huang
 * @date 2021-12-29 5:16 PM
 */
public class PatternConst {
    /**
     * 汉字，包含〇以及（）
     */
    public static final String CHINESE = "^[\\u4e00-\\u9fa5〇（）]+$";

    /**
     * 手机号正则
     */
    public static final String MOBILE = "^1[3-9]\\d{9}$";

    /**
     * 整数
     */
    public static final String NUMBER = "^\\d+$";
}
