package mayfly.core.permission.registry;

import mayfly.core.util.CollectionUtils;
import mayfly.core.util.thread.ScheduleUtils;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

/**
 * @author meilin.huang
 * @version 1.0
 * @date 2019-03-23 8:25 PM
 */
public class DefaultUserPermissionCodeRegistry<I> implements UserPermissionCodeRegistry<I> {

    private static DefaultUserPermissionCodeRegistry defaultUserPermissionCodeRegistry = new DefaultUserPermissionCodeRegistry();

    public static <T> DefaultUserPermissionCodeRegistry<T> getInstance() {
        return defaultUserPermissionCodeRegistry;
    }

    /**
     * 权限缓存
     */
    private Map<I, Collection<String>> permissionCache = new ConcurrentHashMap<>(255);

    private DefaultUserPermissionCodeRegistry() {
    }

    @Override
    public void save(I userId, Collection<String> permissionCodes, long time, TimeUnit timeUnit) {
        if (permissionCache.containsKey(userId)) {
            delete(userId);
        }
        permissionCache.put(userId, permissionCodes);
        ScheduleUtils.schedule(String.valueOf(userId), () -> {
            permissionCache.remove(userId);
        }, time, timeUnit);
    }

    @Override
    public void delete(I userId) {
        permissionCache.remove(userId);
        ScheduleUtils.cancel(String.valueOf(userId));
    }

    @Override
    public boolean has(I userId, String permissionCode) {
        return CollectionUtils.contains(permissionCache.get(userId), permissionCode);
    }
}
