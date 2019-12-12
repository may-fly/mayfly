package mayfly.sys.module.permission.service;

import mayfly.sys.module.base.service.BaseService;
import mayfly.sys.module.permission.entity.AccountRole;
import mayfly.sys.module.permission.entity.Role;

import java.util.List;

/**
 * @author meilin.huang
 * @version 1.0
 * @date 2019-08-19 20:12
 */
public interface AccountRoleService extends BaseService<AccountRole> {

    /**
     * 获取账号角色列表
     *
     * @param accountId 账号id
     * @return  角色列表
     */
    List<Role> listRoleByAccountId(Integer accountId);

    /**
     * 获取指定账号的角色id列表
     *
     * @param accountId 账号id
     * @return
     */
    List<Integer> listRoleIdByAccountId(Integer accountId);

    /**
     * 保存账号角色
     *
     * @param userId  用户id
     * @param roleIds 角色id列表
     */
    void saveRoles(Integer userId, List<Integer> roleIds);
}
