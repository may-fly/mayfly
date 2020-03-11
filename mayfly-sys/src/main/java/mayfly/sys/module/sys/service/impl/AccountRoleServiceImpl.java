package mayfly.sys.module.sys.service.impl;

import mayfly.core.base.service.impl.BaseServiceImpl;
import mayfly.core.exception.BusinessAssert;
import mayfly.core.util.CollectionUtils;
import mayfly.sys.module.sys.entity.AccountRoleDO;
import mayfly.sys.module.sys.entity.RoleDO;
import mayfly.sys.module.sys.mapper.AccountRoleMapper;
import mayfly.sys.module.sys.service.AccountRoleService;
import mayfly.sys.module.sys.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author meilin.huang
 * @version 1.0
 * @date 2019-08-19 20:13
 */
@Service
public class AccountRoleServiceImpl extends BaseServiceImpl<AccountRoleMapper, AccountRoleDO> implements AccountRoleService {

    @Autowired
    private RoleService roleService;

    @Override
    public List<RoleDO> listRoleByAccountId(Integer accountId) {
        return mapper.selectRoleByAccountId(accountId);
    }

    @Override
    public List<Integer> listRoleIdByAccountId(Integer accountId) {
        return listByCondition(new AccountRoleDO().setAccountId(accountId)).stream().map(AccountRoleDO::getRoleId)
                .collect(Collectors.toList());
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void saveRoles(Integer accountId, List<Integer> roleIds) {
        List<Integer> oldRoles = listRoleIdByAccountId(accountId);

        //和之前存的角色列表id比较，哪些是新增，哪些是修改以及不变的
        CollectionUtils.CompareResult<Integer> compareResult = CollectionUtils
                .compare(roleIds, oldRoles, (Integer i1, Integer i2) -> i1.equals(i2) ? 0 : 1);

        Collection<Integer> delIds = compareResult.getDelValue();
        Collection<Integer> addIds = compareResult.getAddValue();

        delIds.forEach(r -> {
            deleteByCondition(new AccountRoleDO().setAccountId(accountId).setRoleId(r));
        });

        if (CollectionUtils.isEmpty(addIds)) {
            return;
        }
        List<AccountRoleDO> ars = new ArrayList<>(addIds.size());
        for (Integer id : addIds) {
            BusinessAssert.notNull(roleService.getById(id), "角色不存在");
            AccountRoleDO ru = new AccountRoleDO().setRoleId(id).setAccountId(accountId);
            ars.add(ru);
        }
        batchInsert(ars);
    }
}
