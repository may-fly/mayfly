package mayfly.sys.service.permission;

import mayfly.entity.RoleUser;
import mayfly.sys.service.base.BaseService;

import java.util.List;

/**
 * @author meilin.huang
 * @version 1.0
 * @date 2019-08-19 20:12
 */
public interface RoleUserService extends BaseService<RoleUser> {

    /**
     * 获取指定账号的角色id列表
     *
     * @param userId 用户id
     * @return
     */
    List<Integer> listRoleIdByUserId(Integer userId);

    /**
     * 保存账号角色
     *
     * @param userId  用户id
     * @param roleIds 角色id列表
     */
    void saveRoles(Integer userId, List<Integer> roleIds);
}
