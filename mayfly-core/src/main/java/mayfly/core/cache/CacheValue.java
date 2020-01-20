package mayfly.core.cache;

/**
 * 缓存值（即缓存对象）
 *
 * @author meilin.huang
 * @version 1.0
 * @date 2019-11-18 2:15 下午
 */
public class CacheValue<V> {

    /**
     * ttl == PERMANENT_TTL，表示该value没有设置过期时间
     */
    private static final long PERMANENT_TTL = 0;

    private final V value;

    /**
     * 过期时间，0表示永久存在
     */
    private final long ttl;

    /**
     * 上次访问的时间
     */
    private long lastAccess;

    /**
     * 访问次数
     */
    private long accessCount;


    private CacheValue(V value, long ttl) {
        this.value = value;
        this.ttl = ttl;
        this.lastAccess = System.currentTimeMillis();
    }


    /**
     * 创建含有过期时间的value
     *
     * @param value value
     * @param ttl   过期时间
     * @param <V>   value实际类型
     * @return cacheValue
     */
    public static <V> CacheValue<V> create(V value, long ttl) {
        return new CacheValue<>(value, ttl);
    }

    /**
     * 创建没有过期时间的value
     *
     * @param key   key
     * @param value value
     * @param <K>   key实际类型
     * @param <V>   value实际类型
     * @return cacheValue
     */
    public static <K, V> CacheValue<V> create(K key, V value) {
        return new CacheValue<>(value, PERMANENT_TTL);
    }


    /**
     * 判断对象是否过期
     *
     * @return 是否过期
     */
    public boolean isExpired() {
        if (ttl == PERMANENT_TTL) {
            return false;
        }
        // 当当前时间超过过期时间，表示过期
        return (this.lastAccess + this.ttl) < System.currentTimeMillis();
    }

    /**
     * 获取value
     *
     * @param updateLastAccess 是否更新最后访问时间
     * @return value
     */
    public V get(boolean updateLastAccess) {
        if (updateLastAccess) {
            this.lastAccess = System.currentTimeMillis();
        }
        accessCount++;
        return this.value;
    }

    /**
     * 获取value值，不更新最后访问时间
     *
     * @return value
     */
    public V get() {
        return get(false);
    }

    /**
     * 获取该值的总访问次数
     *
     * @return 访问次数
     */
    public long getAccessCount() {
        return accessCount;
    }
}
