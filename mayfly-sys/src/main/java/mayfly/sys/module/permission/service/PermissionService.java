package mayfly.sys.module.permission.service;

import mayfly.sys.module.permission.entity.Account;
import mayfly.sys.module.permission.controller.vo.LoginSuccessVO;

/**
 * 权限服务
 *
 * @author: meilin.huang
 * @date: 2018/6/26 上午9:48
 */
public interface PermissionService {
    /**
     * 保存id以及对应的权限
     *
     * @param account 管理员信息
     * @return token
     */
    LoginSuccessVO saveIdAndPermission(Account account);

    /**
     * 根据token获取用户id
     *
     * @param token
     * @return userId
     */
    Integer getIdByToken(String token);
}
