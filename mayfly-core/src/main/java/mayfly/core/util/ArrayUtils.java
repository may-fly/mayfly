package mayfly.core.util;

import java.util.Objects;

/**
 * @author meilin.huang
 * @version 1.0
 * @date 2020-01-13 5:38 下午
 */
public class ArrayUtils {

    /**
     * 是否为空
     *
     * @param array 数组类型
     * @param <T>   泛型
     * @return true:为空
     */
    public static <T> boolean isEmpty(T[] array) {
        return array == null || array.length == 0;
    }

    /**
     * 判断数组中是否存在指定元素
     *
     * @param arrays 数组
     * @param val    校验的元素
     * @param <T>    数组原始类型
     * @return 是否存在
     */
    public static <T> boolean contains(T[] arrays, T val) {
        if (arrays == null) {
            return false;
        }
        for (T t : arrays) {
            if (Objects.equals(t, val)) {
                return true;
            }
        }
        return false;
    }
}
