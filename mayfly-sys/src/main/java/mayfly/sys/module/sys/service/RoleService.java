package mayfly.sys.module.sys.service;

import mayfly.sys.module.sys.entity.Role;
import mayfly.sys.common.base.service.BaseService;

/**
 * @author meilin.huang
 * @version 1.0
 * @date 2018-12-07 4:13 PM
 */
public interface RoleService extends BaseService<Role> {

    /**
     * 删除角色
     *
     * @param id 角色id
     */
    void deleteRole(Integer id);
}
