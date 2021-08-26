package mayfly.core.util;

import java.util.HashMap;
import java.util.Map;

/**
 * @author meilin.huang
 * @version 1.0
 * @date 2019-12-04 5:29 下午
 */
public final class MapUtils {

    /**
     * 判断map是否为空
     *
     * @param map map
     * @return 是否为空map
     */
    public static boolean isEmpty(Map<?, ?> map) {
        return map == null || map.isEmpty();
    }

    /**
     * HashMap Builder
     *
     * @param size 元素个数(实际需要存储的元素个数，并非hashmap中的capacity)
     * @param <K>  key类型
     * @param <V>  value类型
     * @return map builder
     */
    public static <K, V> MapBuilder<K, V> hashMapBuilder(int size) {
        return new MapBuilder<K, V>(new HashMap<>(Math.max((int) (size / .75f) + 1, 16)));
    }

    /**
     * HashMap Builder
     *
     * @param size       元素个数(实际需要存储的元素个数，并非hashmap中的capacity)
     * @param firstKey   key值
     * @param firstValue value值
     * @param <K>        key类型
     * @param <V>        value类型
     * @return map builder
     */
    public static <K, V> MapBuilder<K, V> hashMapBuilder(int size, K firstKey, V firstValue) {
        return MapUtils.<K, V>hashMapBuilder(size).put(firstKey, firstValue);
    }

    /**
     * HashMap Builder
     *
     * @param firstKey   key值
     * @param firstValue value值
     * @param <K>        key类型
     * @param <V>        value类型
     * @return map builder
     */
    public static <K, V> MapBuilder<K, V> hashMapBuilder(K firstKey, V firstValue) {
        return MapUtils.<K, V>hashMapBuilder(8).put(firstKey, firstValue);
    }

    /**
     * 从map中获取指定key的String值，若map == null || 该key不存在 || value非String类型，返回null
     *
     * @param map map
     * @param key key
     * @return boolean
     */
    public static <K, V> String getString(Map<K, V> map, K key) {
        return getString(map, key, null);
    }

    /**
     * 从map中获取指定key的String值，若map == null || 该key不存在 || value非String类型，返回defaultValue
     *
     * @param map map
     * @param key key
     * @return boolean
     */
    public static <K, V> String getString(Map<K, V> map, K key, String defaultValue) {
        if (map == null) {
            return defaultValue;
        }
        Object o = map.get(key);
        if (o instanceof String) {
            return (String) o;
        }
        return defaultValue;
    }

    /**
     * 从map中获取指定key的boolean值，若map == null || 该key不存在 || value非boolean类型，返回false
     *
     * @param map map
     * @param key key
     * @return boolean
     */
    public static <K, V> Boolean getBoolean(Map<K, V> map, K key) {
        return getBoolean(map, key, null);
    }

    /**
     * 从map中获取指定key的boolean值，若map == null || 该key不存在 || value非boolean类型，返回defaultValue
     *
     * @param map map
     * @param key key
     * @return boolean
     */
    public static <K, V> Boolean getBoolean(Map<K, V> map, K key, Boolean defaultValue) {
        if (map == null) {
            return defaultValue;
        }
        Object o = map.get(key);
        if (o instanceof Boolean) {
            return (Boolean) o;
        }
        return defaultValue;
    }

    /**
     * 从map中获取指定key的Long值，若map == null || 该key不存在 || value非Long类型，返回null
     *
     * @param map map
     * @param key key
     * @return Long
     */
    public static <K, V> Long getLong(Map<K, V> map, K key) {
        return getLong(map, key, null);
    }

    /**
     * 从map中获取指定key的Long值，若map == null || 该key不存在 || value非Long类型，返回defaultValue
     *
     * @param map map
     * @param key key
     * @return Long
     */
    public static <K, V> Long getLong(Map<K, V> map, K key, Long defaultValue) {
        return (Long) getNumber(map, key, defaultValue);
    }

    /**
     * 从map中获取指定key的Integer值，若map == null || 该key不存在 || value非Integer类型，返回null
     *
     * @param map map
     * @param key key
     * @return Long
     */
    public static <K, V> Integer getInteger(Map<K, V> map, K key) {
        return getInteger(map, key, null);
    }

    /**
     * 从map中获取指定key的Integer值，若map == null || 该key不存在 || value非Integer类型，返回defaultValue
     *
     * @param map map
     * @param key key
     * @return Long
     */
    public static <K, V> Integer getInteger(Map<K, V> map, K key, Long defaultValue) {
        return (Integer) getNumber(map, key, defaultValue);
    }

    /**
     * 从map中获取指定key的Number类型值，若map == null || 该key不存在 || value非Number类型，返回null
     *
     * @param map map
     * @param key key
     * @return Long
     */
    public static <K, V> Number getNumber(Map<K, V> map, K key) {
        return getNumber(map, key, null);
    }

    /**
     * 从map中获取指定key的Number类型值，若map == null || 该key不存在 || value非Number类型，返回defaultValue
     *
     * @param map map
     * @param key key
     * @return Long
     */
    public static <K, V> Number getNumber(Map<K, V> map, K key, Number defaultValue) {
        if (map == null) {
            return defaultValue;
        }
        Object o = map.get(key);
        if (o instanceof Number) {
            return (Number) o;
        }
        return defaultValue;
    }

    static int capacity(int expectedSize) {
        if (expectedSize <3) {
            return expectedSize + 1;
        }
        return expectedSize < 1073741824 ? (int)((float)expectedSize / 0.75F + 1.0F) :  2147483647;
    }


    public static class MapBuilder<K, V> {

        private final Map<K, V> map;

        public MapBuilder(Map<K, V> map) {
            this.map = map;
        }

        /**
         * 链式Map创建
         *
         * @param k Key类型
         * @param v Value类型
         * @return 当前类
         */
        public MapBuilder<K, V> put(K k, V v) {
            map.put(k, v);
            return this;
        }

        /**
         * 创建后的map
         *
         * @return 创建后的map
         */
        public Map<K, V> build() {
            return map;
        }
    }
}
