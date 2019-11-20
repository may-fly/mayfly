package mayfly.core.util.cache;

import mayfly.core.util.Assert;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * @author meilin.huang
 * @version 1.0
 * @date 2019-11-18 4:49 下午
 */
public abstract class AbstractCache<K, V> implements Cache<K, V> {

    /**
     * 缓存容量
     */
    private int capacity;

    /**
     * 真实存储缓存对象的容器
     */
    Map<K, CacheValue<V>> cacheMap = new ConcurrentHashMap<>(capacity);

    /**
     * 移除key时的回调（如用于关闭一些session连接等）
     */
    private Consumer<V> removeCallback;


    /**
     * 如果缓存达到指定capacity，则执行该方法进行移除，以便有空间放入新缓存对象
     */
    public abstract void ifFullRemoval();


    @Override
    public void put(K key, V object) {
        putCheck(key, object);
        putWithoutCheck(key, object);
    }

    @Override
    public void put(K key, V object, long timeout, TimeUnit timeUnit) {
        putCheck(key, object);
        putWithoutCheck(key, object, timeout, timeUnit);
    }

    @Override
    public V get(K key) {
        return get(key, null);
    }

    @Override
    public V get(K key, Supplier<V> supplier) {
        return get(key, supplier, false);
    }

    @Override
    public V get(K key, boolean updateLastAccess) {
        return get(key, null, updateLastAccess);
    }

    @Override
    public synchronized V get(K key, Supplier<V> supplier, boolean updateLastAccess) {
        Assert.notNull(key, "key must not be null");

        CacheValue<V> cacheValue = cacheMap.get(key);
        if (cacheValue != null && !cacheValue.isExpired()) {
            return cacheValue.get(updateLastAccess);
        }
        if (supplier == null) {
            return null;
        }

        V value = supplier.get();
        if (cacheValue == null) {
            putWithoutCheck(key, value);
            return value;
        }
        // cache value过期情况
        onRemove(cacheValue.get());
        putWithoutCheck(key, value);
        return value;
    }

    @Override
    public boolean containsKey(K key) {
        return get(key) != null;
    }

    @Override
    public void remove(K key) {
        onRemove(cacheMap.remove(key).get());
    }


    /**
     * 移除key时调用
     * @param value value
     */
    private void onRemove(V value) {
        if (removeCallback != null) {
            removeCallback.accept(value);
        }
    }

    /**
     * 判断该缓存是否已满
     * @return  缓存个数是否超过capacity
     */
    private boolean isFull() {
        return capacity > 0 && cacheMap.size() >= capacity;
    }


    /**
     * 如果有移除回调函数，则说明覆盖或者删除对象时需要对缓存对象进行一些处理（如关闭连接等）
     * @param key  要put的key
     */
    private void putCheck(K key, V object) {
        Assert.notNull(object, "object must not be null");
        Assert.notNull(key, "key mush not be null");
        if (removeCallback != null) {
            CacheValue<V> cv = cacheMap.get(key);
            if (cv != null) {
                onRemove(cv.get());
            }
        }
    }

    /**
     * 没有校验是否需要执行移除回调函数的put
     * @param key       key
     * @param object    object
     */
    protected void putWithoutCheck(K key, V object) {
        if (isFull()) {
            ifFullRemoval();
        }
        cacheMap.put(key, CacheValue.create(key, object));
    }

    /**
     * 没有校验是否需要执行移除回调函数的put
     * @param key       key
     * @param object    object
     * @param timeout   过期时间
     * @param timeUnit  时间单位
     */
    void putWithoutCheck(K key, V object, long timeout, TimeUnit timeUnit) {
        if (isFull()) {
            ifFullRemoval();
        }
        cacheMap.put(key, CacheValue.create(object, timeUnit.toMillis(timeout)));
    }


    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public void setRemoveCallback(Consumer<V> removeCallback) {
        this.removeCallback = removeCallback;
    }
}
