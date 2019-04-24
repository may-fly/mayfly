package mayfly.common.utils;

import mayfly.common.enums.NameValueEnum;
import mayfly.common.enums.ValueEnum;

/**
 * @author meilin.huang
 * @version 1.0
 * @date 2019-03-22 11:12 AM
 */
public final class EnumUtils {

    /**
     * 判断枚举值是否存在于指定枚举数组中
     * @param enums  枚举数组
     * @param value  枚举值
     * @return
     */
    public static boolean isExist(ValueEnum[] enums, Integer value) {
        if (value == null) {
            return false;
        }
        for (ValueEnum e : enums) {
            if (value.equals(e.getValue())) {
                return true;
            }
        }
        return false;
    }

    /**
     * 根据枚举值获取其对应的名字
     * @param enums
     * @param value
     * @return
     */
    public static String getNameByValue(NameValueEnum[] enums, Integer value) {
        if (value == null) {
            return "";
        }
        for (NameValueEnum e : enums) {
            if (value.equals(e.getValue())) {
                return e.getName();
            }
        }
        return "";
    }

    public static Integer getValueByName(NameValueEnum[] enums, String name) {
        if (StringUtils.isEmpty(name)) {
            return null;
        }
        for (NameValueEnum e : enums) {
            if (name.equals(e.getName())) {
                return e.getValue();
            }
        }
        return  null;
    }
}
