package mayfly.common.permission.registry;

import mayfly.common.utils.ScheduleUtils;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

/**
 * @author meilin.huang
 * @version 1.0
 * @date 2019-03-23 8:25 PM
 */
public class DefaultUserPermissionCodeRegistry implements UserPermissionCodeRegistry {

    private static DefaultUserPermissionCodeRegistry defaultUserPermissionCodeRegistry = new DefaultUserPermissionCodeRegistry();

    public static DefaultUserPermissionCodeRegistry getInstance() {
        return defaultUserPermissionCodeRegistry;
    }

    /**
     * 权限缓存
     */
    private static Map<Integer, Collection<String>> permissionCache = new ConcurrentHashMap<>(255);

    private DefaultUserPermissionCodeRegistry(){}

    @Override
    public void save(Integer userId, Collection<String> permissionCodes, long time, TimeUnit timeUnit) {
        if (permissionCache.containsKey(userId)) {
            delete(userId);
        }
        permissionCache.put(userId, permissionCodes);
        ScheduleUtils.schedule(String.valueOf(userId), () -> {
            permissionCache.remove(userId);
        }, time, timeUnit);
    }

    @Override
    public void delete(Integer userId) {
        permissionCache.remove(userId);
        ScheduleUtils.cancel(String.valueOf(userId));
    }

    @Override
    public boolean has(Integer userId, String permissionCode) {
        Collection<String> codes;
        return (codes = permissionCache.get(userId)) != null && codes.contains(permissionCode);
    }
}
