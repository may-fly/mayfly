package mayfly.core.permission.registry;

import mayfly.core.util.enums.BoolEnum;

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
    private UserPermissionCodeRegistry<I> userCodeRegistry;


    private PermissionCacheHandler(UserPermissionCodeRegistry<I> userCodeRegistry) {
        this.userCodeRegistry = userCodeRegistry;
    }

    /**
     * 权限缓存工厂方法
     *
     * @param userCodeRegistry 用户权限缓存器(null则使用默认注册器 {@link DefaultUserPermissionCodeRegistry})
     * @return
     */
    public static <T> PermissionCacheHandler<T> of(UserPermissionCodeRegistry<T> userCodeRegistry) {
        if (userCodeRegistry == null) {
            userCodeRegistry = DefaultUserPermissionCodeRegistry.<T>getInstance();
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
     * 获取权限code对应的禁用code,即code + ":" + 0
     *
     * @param code
     * @return
     */
    public static String getDisablePermissionCode(String code) {
        return code + CODE_STATUS_SEPARATOR + BoolEnum.FALSE.getValue();
    }
}
