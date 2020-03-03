package mayfly.sys.module.sys.service;

import mayfly.core.base.service.BaseService;
import mayfly.sys.module.sys.entity.RoleDO;

/**
 * @author meilin.huang
 * @version 1.0
 * @date 2018-12-07 4:13 PM
 */
public interface RoleService extends BaseService<RoleDO> {

    /**
     * 删除角色
     *
     * @param id 角色id
     */
    void deleteRole(Integer id);
}
