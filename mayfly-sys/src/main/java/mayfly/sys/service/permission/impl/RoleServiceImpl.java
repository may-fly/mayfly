package mayfly.sys.service.permission.impl;

import mayfly.core.exception.BusinessAssert;
import mayfly.dao.RoleMapper;
import mayfly.entity.Role;
import mayfly.entity.RoleResource;
import mayfly.entity.RoleUser;
import mayfly.sys.service.base.impl.BaseServiceImpl;
import mayfly.sys.service.permission.RoleResourceService;
import mayfly.sys.service.permission.RoleService;
import mayfly.sys.service.permission.RoleUserService;
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
    private RoleUserService roleUserService;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void deleteRole(Integer id) {
        Role role = getById(id);
        BusinessAssert.notNull(role, "角色不存在");
        // 删除角色关联的用户角色信息
        roleUserService.deleteByCondition(RoleUser.builder().roleId(id).build());
        // 删除角色关联的资源信息
        roleResourceService.deleteByCondition(RoleResource.builder().roleId(id).build());
        deleteById(id);
    }
}
