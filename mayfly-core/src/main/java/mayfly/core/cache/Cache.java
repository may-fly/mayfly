package mayfly.core.cache;

import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

/**
 * @author meilin.huang
 * @version 1.0
 * @date 2019-11-18 4:36 下午
 */
public interface Cache<K, V> {

    /**
     * 将对象加入到缓存，使用默认失效时长(即不失效)
     *
     * @param key    键
     * @param object 缓存的对象
     */
    void put(K key, V object);

    /**
     * 将对象加入到缓存，使用指定失效时长<br>
     *
     * @param key      键
     * @param object   缓存的对象
     * @param timeout  失效时长
     * @param timeUnit 时长单位
     */
    void put(K key, V object, long timeout, TimeUnit timeUnit);

    /**
     * 从缓存中获得对象，当对象不在缓存中或已经过期返回<code>null</code>
     * <p>
     * 调用此方法时，会检查上次调用时间，如果与当前时间差值大于超时时间返回<code>null</code>，否则返回值。
     * <p>
     * 每次调用此方法不会刷新最后访问时间，也就是说不会重新计算超时时间。
     *
     * @param key 键
     * @return 键对应的对象
     * @see #get(Object, boolean)
     */
    V get(K key);

    /**
     * 从缓存中获得对象，当对象不在缓存中或已经过期返回Supplier回调产生的value对象
     *
     * @param key      键
     * @param supplier 如果不存在回调方法，用于生产值对象
     * @return 值对象
     */
    V get(K key, Supplier<V> supplier);

    /**
     * 从缓存中获得对象，当对象不在缓存中或已经过期返回<code>null</code>
     * <p>
     * 调用此方法时，会检查上次调用时间，如果与当前时间差值大于超时时间返回<code>null</code>，否则返回值。
     *
     * @param key              键
     * @param updateLastAccess 是否更新最后访问时间，即重新计算超时时间。
     * @return 键对应的对象
     */
    V get(K key, boolean updateLastAccess);

    /**
     * 获取缓存对象
     *
     * @param key              key
     * @param supplier         key不存在或者超时时，用于生成value并put至map
     * @param updateLastAccess 是否更新最后调用时间（即是否重新计算超时时间）
     * @return value
     */
    V get(K key, Supplier<V> supplier, boolean updateLastAccess);

    /**
     * 是否存在指定key的值并且没有过期
     *
     * @param key key
     * @return 是否存在还没过期的key
     */
    boolean containsKey(K key);

    /**
     * 移除指定key的缓存对象(如果有移除对象的回调方法，则也会执行)
     *
     * @param key key
     */
    void remove(K key);

    /**
     * 移除指定value
     *
     * @param value value
     */
    void removeByValue(V value);
}
