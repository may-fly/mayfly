package mayfly.sys.module.sys.service.impl;

import mayfly.core.exception.BusinessAssert;
import mayfly.core.util.CollectionUtils;
import mayfly.sys.module.sys.entity.Role;
import mayfly.sys.module.sys.mapper.AccountRoleMapper;
import mayfly.sys.module.sys.entity.AccountRole;
import mayfly.sys.common.base.service.impl.BaseServiceImpl;
import mayfly.sys.module.sys.service.RoleService;
import mayfly.sys.module.sys.service.AccountRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author meilin.huang
 * @version 1.0
 * @date 2019-08-19 20:13
 */
@Service
public class AccountRoleServiceImpl extends BaseServiceImpl<AccountRoleMapper, AccountRole> implements AccountRoleService {

    @Autowired
    private RoleService roleService;
    @Autowired
    private AccountRoleMapper accountRoleMapper;

    @Override
    public List<Role> listRoleByAccountId(Integer accountId) {
        return accountRoleMapper.selectRoleByAccountId(accountId);
    }

    @Override
    public List<Integer> listRoleIdByAccountId(Integer accountId) {
        return listByCondition(AccountRole.builder().accountId(accountId).build()).stream().map(AccountRole::getRoleId).collect(Collectors.toList());
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void saveRoles(Integer accountId, List<Integer> roleIds) {
        List<Integer> oldRoles = listRoleIdByAccountId(accountId);

        //和之前存的角色列表id比较，哪些是新增已经哪些是修改以及不变的
        CollectionUtils.CompareResult<Integer> compareResult = CollectionUtils
                .compare(roleIds, oldRoles, (Integer i1, Integer i2) -> i1.equals(i2) ? 0 : 1);

        Collection<Integer> delIds = compareResult.getDelValue();
        Collection<Integer> addIds = compareResult.getAddValue();

        delIds.forEach(r -> {
            deleteByCondition(AccountRole.builder().accountId(accountId).roleId(r).build());
        });

        LocalDateTime now = LocalDateTime.now();
        for (Integer id : addIds) {
            BusinessAssert.notNull(roleService.getById(id), "角色不存在");
            AccountRole ru = AccountRole.builder().roleId(id).accountId(accountId).createTime(now).build();
            insert(ru);
        }
    }
}
