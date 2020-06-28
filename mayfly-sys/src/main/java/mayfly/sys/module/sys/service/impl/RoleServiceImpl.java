package mayfly.sys.module.sys.service.impl;

import mayfly.core.base.service.impl.BaseServiceImpl;
import mayfly.core.exception.BizAssert;
import mayfly.core.log.MethodLog;
import mayfly.sys.module.sys.entity.AccountRoleDO;
import mayfly.sys.module.sys.entity.RoleDO;
import mayfly.sys.module.sys.entity.RoleResourceDO;
import mayfly.sys.module.sys.mapper.RoleMapper;
import mayfly.sys.module.sys.service.AccountRoleService;
import mayfly.sys.module.sys.service.OperationLogService;
import mayfly.sys.module.sys.service.RoleResourceService;
import mayfly.sys.module.sys.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author meilin.huang
 * @version 1.0
 * @date 2018-12-07 4:13 PM
 */
@MethodLog("角色管理:")
@Service
public class RoleServiceImpl extends BaseServiceImpl<RoleMapper, Long, RoleDO> implements RoleService {

    @Autowired
    private RoleResourceService roleResourceService;
    @Autowired
    private AccountRoleService accountRoleService;
    @Autowired
    private OperationLogService logService;


    @Override
    public void update(RoleDO role) {
        RoleDO old = getById(role.getId());
        BizAssert.notNull(old, "角色不存在");
        updateByIdSelective(role);
        logService.asyncUpdateLog("修改角色", role, old);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void delete(Long id) {
        RoleDO role = getById(id);
        BizAssert.notNull(role, "角色不存在");
        // 删除角色关联的用户角色信息
        accountRoleService.deleteByCondition(new AccountRoleDO().setRoleId(id));
        // 删除角色关联的资源信息
        roleResourceService.deleteByCondition(new RoleResourceDO().setRoleId(id));
        deleteById(id);
        logService.asyncDeleteLog("删除角色", role);
    }
}
