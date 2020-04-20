package mayfly.core.cache;

import mayfly.core.thread.ScheduleUtils;

import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author meilin.huang
 * @version 1.0
 * @date 2019-11-18 2:14 下午
 */
public class TimedCache<K, V> extends AbstractCache<K, V> {

    private static final String ID_PREFIX = "removeExpireCacheKey-";

    private final long timeout;

    private final TimeUnit timeUnit;


    public TimedCache(long timeout, TimeUnit timeUnit) {
        this.timeout = timeout;
        this.timeUnit = timeUnit;
    }

    /**
     * 由于默认都需要超时时间，所以覆盖抽象类的该方法，强制使用带有过期时间的参数
     *
     * @param key    key
     * @param object object
     */
    @Override
    protected void putWithoutCheck(K key, V object) {
        // 默认都使用设定的过期时间
        super.putWithoutCheck(key, object, timeout, timeUnit);
        if (!ScheduleUtils.containSchedule(getScheduleId())) {
            this.scheduleRemoveKey();
        }
    }

    @Override
    public void ifFullRemoval() {
        long minCount = Long.MAX_VALUE;
        K rmKey = null;
        // 移除最少访问的对象
        for (Map.Entry<K, CacheValue<V>> entry : cacheMap.entrySet()) {
            CacheValue<V> cv = entry.getValue();
            // 如果存在过期的则直接返回过期的key，进行移除即可
            if (cv.isExpired()) {
                rmKey = entry.getKey();
                break;
            }
            long accessCount = cv.getAccessCount();
            if (accessCount < minCount) {
                minCount = accessCount;
                rmKey = entry.getKey();
            }
        }
        remove(rmKey);
    }

    @Override
    public void remove(K key) {
        super.remove(key);
        // 如果cacheMap为空则移除该定时任务
        if (cacheMap.isEmpty()) {
            ScheduleUtils.cancel(getScheduleId());
        }
    }

    /**
     * 定时检查map，并清除过期的key
     */
    private void scheduleRemoveKey() {
        // 定期检查cache中是否有过期的key,有则移除
        ScheduleUtils.scheduleAtFixedRate(getScheduleId(), () -> {
            cacheMap.forEach((k, v) -> {
                if (v.isExpired()) {
                    remove(k);
                }
            });
        }, 10, 10, TimeUnit.SECONDS);
    }

    /**
     *  防止多个TimedCache对象，相同的key会导致定时任务失效
     *
     * @return key
     */
    private String getScheduleId() {
        return ID_PREFIX + this.hashCode();
    }

}
