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
     * @param map   map
     * @return      是否为空map
     */
    public static boolean isEmpty(Map<?, ?> map) {
        return map == null || map.isEmpty();
    }

    /**
     * HashMap Builder
     *
     * @param capacity  容量
     * @param <K>       key类型
     * @param <V>       value类型
     * @return          map builder
     */
    public static <K, V> MapBuilder<K, V> hashMapBuilder(int capacity) {
        return new MapBuilder<K, V>(new HashMap<>(capacity));
    }

    /**
     * HashMap Builder
     *
     * @param capacity      容量
     * @param firstKey       key值
     * @param firstValue     value值
     * @param <K>           key类型
     * @param <V>           value类型
     * @return              map builder
     */
    public static <K, V> MapBuilder<K, V> hashMapBuilder(int capacity, K firstKey, V firstValue) {
        return new MapBuilder<K, V>(new HashMap<>(capacity))
                .put(firstKey, firstValue);
    }



    public static class MapBuilder<K, V> {

        private Map<K, V> map;

        public MapBuilder(Map<K, V> map) {
            this.map = map;
        }

        /**
         * 链式Map创建
         *
         * @param k Key类型
         * @param v Value类型
         * @return  当前类
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
