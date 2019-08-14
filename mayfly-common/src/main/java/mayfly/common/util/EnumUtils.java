package mayfly.common.util;

import mayfly.common.enums.NameValueEnum;
import mayfly.common.enums.ValueEnum;

/**
 * 枚举常用工具类。<br/>
 * 使用该枚举工具类需要指定的枚举实现{@link ValueEnum} OR {@link NameValueEnum}接口
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
     * @param enums  枚举列表
     * @param value  枚举值
     * @return       枚举名称
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

    /**
     * 根据枚举名称获取对应的枚举值
     * @param enums  枚举列表
     * @param name   枚举名
     * @return       枚举值
     */
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
