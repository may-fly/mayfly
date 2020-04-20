package mayfly.core.util.enums;

import mayfly.core.util.StringUtils;

/**
 * 枚举常用工具类。<br/>
 * 使用该枚举工具类需要指定的枚举实现{@link ValueEnum} OR {@link NameValueEnum}接口
 *
 * @author meilin.huang
 * @version 1.0
 * @date 2019-03-22 11:12 AM
 */
public final class EnumUtils {

    /**
     * 判断枚举值是否存在于指定枚举数组中
     *
     * @param enums 枚举数组
     * @param value 枚举值
     * @return 是否存在
     */
    public static <T> boolean isExist(ValueEnum<T>[] enums, T value) {
        if (value == null) {
            return false;
        }
        for (ValueEnum<T> e : enums) {
            if (value.equals(e.getValue())) {
                return true;
            }
        }
        return false;
    }

    /**
     * 判断枚举值是否存与指定枚举类中（尽量显示调用Enum.values()方法获取所有枚举类，此方法内部反射调用values方法）
     *
     * @param enumClass 枚举类
     * @param value     枚举值
     * @param <E>       枚举类型
     * @param <V>       值类型
     * @return true：存在
     */
    @SuppressWarnings("unchecked")
    public static <E extends Enum<? extends ValueEnum<V>>, V> boolean isExist(Class<E> enumClass, V value) {
        for (Enum<? extends ValueEnum<V>> e : enumClass.getEnumConstants()) {
            if (((ValueEnum<V>) e).getValue().equals(value)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 根据枚举值获取其对应的名字
     *
     * @param enums 枚举列表
     * @param value 枚举值
     * @return 枚举名称
     */
    public static <T> String getNameByValue(NameValueEnum<T>[] enums, T value) {
        if (value == null) {
            return null;
        }
        for (NameValueEnum<T> e : enums) {
            if (value.equals(e.getValue())) {
                return e.getName();
            }
        }
        return null;
    }

    /**
     * 根据枚举名称获取对应的枚举值
     *
     * @param enums 枚举列表
     * @param name  枚举名
     * @return 枚举值
     */
    public static <T> T getValueByName(NameValueEnum<T>[] enums, String name) {
        if (StringUtils.isEmpty(name)) {
            return null;
        }
        for (NameValueEnum<T> e : enums) {
            if (name.equals(e.getName())) {
                return e.getValue();
            }
        }
        return null;
    }

    /**
     * 根据枚举值获取对应的枚举对象
     *
     * @param enums 枚举列表
     * @return 枚举对象
     */
    @SuppressWarnings("unchecked")
    public static <E extends Enum<? extends ValueEnum<V>>, V> E getEnumByValue(E[] enums, V value) {
        for (Enum<? extends ValueEnum<V>> e : enums) {
            if (((ValueEnum<V>) e).getValue().equals(value)) {
                return (E) e;
            }
        }
        return null;
    }

    /**
     * 根据枚举值获取对应的枚举对象
     *
     * @param enumClass 枚举class
     * @return 枚举对象
     */
    public static <E extends Enum<? extends ValueEnum<V>>, V> E getEnumByValue(Class<E> enumClass, V value) {
        return getEnumByValue(enumClass.getEnumConstants(), value);
    }
}
