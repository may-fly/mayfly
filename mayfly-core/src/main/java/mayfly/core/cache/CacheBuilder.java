package mayfly.core.cache;

import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

/**
 * @author meilin.huang
 * @version 1.0
 * @date 2019-11-19 2:35 下午
 */
public final class CacheBuilder<K, V> {

    private final AbstractCache<K, V> abstractCache;

    private CacheBuilder(AbstractCache<K, V> abstractCache) {
        this.abstractCache = abstractCache;
    }

    /**
     * 创建一个具有过期时间的缓存对象builder
     *
     * @param timeout  过期时间
     * @param timeUnit 时间单位
     * @param <K>      key类型
     * @param <V>      value类型
     * @return builder
     */
    public static <K, V> CacheBuilder<K, V> newTimedBuilder(long timeout, TimeUnit timeUnit) {
        return new CacheBuilder<K, V>(new TimedCache<K, V>(timeout, timeUnit));
    }

    /**
     * 缓存容量
     *
     * @param capacity 容量
     * @return builder
     */
    public CacheBuilder<K, V> capacity(int capacity) {
        abstractCache.setCapacity(capacity);
        return this;
    }

    /**
     * 移除对象时的回调函数
     *
     * @param removeCallback 回调函数
     * @return builder
     */
    public CacheBuilder<K, V> removeCallback(Consumer<V> removeCallback) {
        abstractCache.setRemovalCallback(removeCallback);
        return this;
    }

    /**
     * 返回具体的cache对象
     *
     * @return abstract cache
     */
    public AbstractCache<K, V> build() {
        return this.abstractCache;
    }
}
