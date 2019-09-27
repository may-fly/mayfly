package mayfly.core.permission.registry;

import mayfly.core.permission.checker.UserPermissionChecker;

import java.util.Collection;
import java.util.concurrent.TimeUnit;

/**
 * 用户权限码注册器（保存读取权限码列表）
 * @author meilin.huang
 * @version 1.0
 * @date 2019-03-23 8:17 PM
 */
public interface UserPermissionCodeRegistry extends UserPermissionChecker {

    /**
     * 保存用户权限码列表
     * @param userId  用户id
     * @param permissionCodes  权限码列表
     * @param time  权限码缓存时间
     * @param timeUnit 时间单位
     */
    void save(Integer userId, Collection<String> permissionCodes, long time, TimeUnit timeUnit);

    /**
     * 删除指定用户的缓存权限信息
     * @param userId
     */
    void delete(Integer userId);

}
