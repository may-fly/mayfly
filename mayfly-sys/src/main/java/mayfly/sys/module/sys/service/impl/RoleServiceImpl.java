package mayfly.sys.module.sys.service.impl;

import mayfly.core.exception.BusinessAssert;
import mayfly.sys.module.sys.mapper.RoleMapper;
import mayfly.sys.module.sys.entity.Role;
import mayfly.sys.module.sys.entity.RoleResource;
import mayfly.sys.module.sys.entity.AccountRole;
import mayfly.sys.module.base.service.impl.BaseServiceImpl;
import mayfly.sys.module.sys.service.RoleResourceService;
import mayfly.sys.module.sys.service.RoleService;
import mayfly.sys.module.sys.service.AccountRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author meilin.huang
 * @version 1.0
 * @date 2018-12-07 4:13 PM
 */
@Service
public class RoleServiceImpl extends BaseServiceImpl<RoleMapper, Role> implements RoleService {

    @Autowired
    private RoleResourceService roleResourceService;
    @Autowired
    private AccountRoleService accountRoleService;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void deleteRole(Integer id) {
        Role role = getById(id);
        BusinessAssert.notNull(role, "角色不存在");
        // 删除角色关联的用户角色信息
        accountRoleService.deleteByCondition(AccountRole.builder().roleId(id).build());
        // 删除角色关联的资源信息
        roleResourceService.deleteByCondition(RoleResource.builder().roleId(id).build());
        deleteById(id);
    }
}
