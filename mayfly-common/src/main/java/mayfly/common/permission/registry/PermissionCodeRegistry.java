package mayfly.common.permission.registry;

import java.util.Collection;
import java.util.concurrent.TimeUnit;

/**
 * 权限码注册器（保存读取权限码列表）
 * @author meilin.huang
 * @version 1.0
 * @date 2019-03-23 8:17 PM
 */
public interface PermissionCodeRegistry {

    /**
     * 保存用户权限码列表
     * @param userId  用户id
     * @param permissionCodes  权限码列表
     */
    void save(Integer userId, Collection<String> permissionCodes, long time, TimeUnit timeUnit);

//    /**
//     * 删除权限code
//     * @param permissionCode
//     */
//    void delete(String permissionCode);
//
//    /**
//     * 重命名权限code
//     * @param oldCode  旧权限code
//     * @param newCode  新权限code
//     */
//    void rename(String oldCode, String newCode);

    void delete(Integer userId);

    /**
     * 判断用户是否具有该权限码（即是否有该权限）
     * @param userId   用户id
     * @param permissionCode  权限code
     * @return
     */
    boolean has(Integer userId, String permissionCode);
}
