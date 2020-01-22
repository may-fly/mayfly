package mayfly.sys.module.sys.service;

import mayfly.core.permission.registry.UserPermissionRegistry;
import mayfly.sys.module.sys.controller.vo.LoginSuccessVO;
import mayfly.sys.module.sys.entity.Account;

/**
 * 权限服务
 *
 * @author: meilin.huang
 * @date: 2018/6/26 上午9:48
 */
public interface PermissionService extends UserPermissionRegistry<Integer> {
    /**
     * 保存id以及对应的权限
     *
     * @param account 管理员信息
     * @return token
     */
    LoginSuccessVO saveIdAndPermission(Account account);

    /**
     * 移除token
     *
     * @param token token
     */
    void removeToken(String token);

    /**
     * 退出登录移除权限
     *
     * @param userId  user id
     */
    void removePermissions(Integer userId);

}
