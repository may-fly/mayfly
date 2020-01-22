package mayfly.core.permission.registry;

import mayfly.core.permission.checker.UserPermissionChecker;

import java.util.Collection;
import java.util.concurrent.TimeUnit;

/**
 * 用户权限码注册器（保存读取权限码列表）泛型<I>表示用户id类型
 *
 * @author meilin.huang
 * @version 1.0
 * @date 2019-03-23 8:17 PM
 */
public interface UserPermissionRegistry<I> extends UserPermissionChecker<I> {

    /**
     * 保存用户权限码列表
     *
     * @param userId          用户id
     * @param permissionCodes 权限码列表
     * @param time            权限码缓存时间
     * @param timeUnit        时间单位
     */
    void save(I userId, Collection<String> permissionCodes, long time, TimeUnit timeUnit);

    /**
     * 删除指定用户的权限信息
     *
     * @param userId 用户id
     */
    void delete(I userId);

}
