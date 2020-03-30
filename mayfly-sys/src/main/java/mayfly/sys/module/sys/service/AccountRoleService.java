package mayfly.sys.module.sys.service;

import mayfly.core.base.service.BaseService;
import mayfly.sys.module.sys.entity.AccountRoleDO;
import mayfly.sys.module.sys.entity.RoleDO;

import java.util.List;

/**
 * @author meilin.huang
 * @version 1.0
 * @date 2019-08-19 20:12
 */
public interface AccountRoleService extends BaseService<Long, AccountRoleDO> {

    /**
     * 获取账号角色列表
     *
     * @param accountId 账号id
     * @return  角色列表
     */
    List<RoleDO> listRoleByAccountId(Long accountId);

    /**
     * 获取指定账号的角色id列表
     *
     * @param accountId 账号id
     * @return
     */
    List<Long> listRoleIdByAccountId(Long accountId);

    /**
     * 保存账号角色
     *
     * @param userId  用户id
     * @param roleIds 角色id列表
     */
    void saveRoles(Long userId, List<Long> roleIds);
}
