package mayfly.core.util;

import java.math.BigDecimal;

/**
 * @author meilin.huang
 * @version 1.0
 * @date 2019-09-03 2:35 下午
 */
public class BigDecimalUtils {

    /**
     * 判断value是否在[最小值, 最小值]范围之内。<br/>
     * 如果最大值为null，则比较value是否大于等于最小值 <br/>
     * 反之最小值为null，则比较values是否小于等于最大值
     *
     * @param value 参与比较的值
     * @param min   最小值
     * @param max   最大值
     * @return 在区间内则返回true，否则false
     */
    public static boolean between(BigDecimal value, BigDecimal min, BigDecimal max) {
        if (value == null) {
            return false;
        }
        if (min == null && max == null) {
            return true;
        }
        // 如果最小值为空，则value小于等于最大值返回true
        if (min == null) {
            return value.compareTo(max) < 1;
        }
        // 如果最大值为空，则value大于等于最小值返回true
        if (max == null) {
            return value.compareTo(min) > -1;
        }

        Assert.isTrue(min.compareTo(max) != 1, "最小值不能大于最大值");

        return value.compareTo(min) > -1 && value.compareTo(max) < 1;
    }
}
