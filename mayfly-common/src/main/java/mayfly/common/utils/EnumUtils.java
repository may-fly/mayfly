package mayfly.common.utils;

import mayfly.common.enums.BaseEnum;

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
    public static boolean isExist(BaseEnum[] enums, Integer value) {
        if (value == null) {
            return false;
        }
        for (BaseEnum e : enums) {
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
    public static String getNameByValue(BaseEnum[] enums, Integer value) {
        if (value == null) {
            return "";
        }
        for (BaseEnum e : enums) {
            if (value.equals(e.getValue())) {
                return e.getName();
            }
        }
        return "";
    }

    public static Integer getValueByName(BaseEnum[] enums, String name) {
        if (StringUtils.isEmpty(name)) {
            return null;
        }
        for (BaseEnum e : enums) {
            if (name.equals(e.getName())) {
                return e.getValue();
            }
        }
        return  null;
    }
}
