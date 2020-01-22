package mayfly.core.permission.registry;

import java.util.Collection;
import java.util.concurrent.TimeUnit;

/**
 * @author meilin.huang
 * @version 1.0
 * @date 2018-11-26 11:02 AM
 */
public final class PermissionCacheHandler<I> {

    /**
     * 权限码与状态分割符号
     */
    public static final String CODE_STATUS_SEPARATOR = ":";

    /**
     * 用户权限码注册
     */
    private UserPermissionRegistry<I> userCodeRegistry;


    private PermissionCacheHandler(UserPermissionRegistry<I> userCodeRegistry) {
        this.userCodeRegistry = userCodeRegistry;
    }

    /**
     * 权限缓存工厂方法
     *
     * @param userCodeRegistry 用户权限缓存器(null则使用默认注册器 {@link DefaultUserPermissionRegistry})
     * @return
     */
    public static <T> PermissionCacheHandler<T> of(UserPermissionRegistry<T> userCodeRegistry) {
        if (userCodeRegistry == null) {
            userCodeRegistry = DefaultUserPermissionRegistry.<T>getInstance();
        }
        return new PermissionCacheHandler<T>(userCodeRegistry);
    }

    /**
     * 保存用户权限列表
     *
     * @param userId          用户id
     * @param permissionCodes 权限列表
     * @param time            时间
     * @param timeUnit        时间单位
     */
    public void savePermission(I userId, Collection<String> permissionCodes, long time, TimeUnit timeUnit) {
        userCodeRegistry.save(userId, permissionCodes, time, timeUnit);
    }

    /**
     * 退出登录删除用户权限
     *
     * @param userId user id
     */
    public void deletePermissions(I userId) {
        userCodeRegistry.delete(userId);
    }

    /**
     * 获取权限code对应的禁用code,即code + ":" + 0
     *
     * @param code
     * @return
     */
    public static String getDisablePermissionCode(String code) {
        return code + CODE_STATUS_SEPARATOR + 0;
    }
}
