package mayfly.sys.service.permission.impl;

import mayfly.common.util.BusinessAssert;
import mayfly.common.util.CollectionUtils;
import mayfly.dao.RoleUserMapper;
import mayfly.entity.RoleUser;
import mayfly.sys.service.base.impl.BaseServiceImpl;
import mayfly.sys.service.permission.RoleService;
import mayfly.sys.service.permission.RoleUserService;
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
public class RoleUserServiceImpl extends BaseServiceImpl<RoleUserMapper, RoleUser> implements RoleUserService {

    @Autowired
    private RoleService roleService;

    @Override
    public List<Integer> listRoleIdByUserId(Integer userId) {
        return listByCondition(RoleUser.builder().userId(userId).build()).stream().map(RoleUser::getRoleId).collect(Collectors.toList());
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void saveRoles(Integer userId, List<Integer> roleIds) {
        List<Integer> oldRoles = listRoleIdByUserId(userId);

        //和之前存的角色列表id比较，哪些是新增已经哪些是修改以及不变的
        CollectionUtils.CompareResult<Integer> compareResult = CollectionUtils
                .compare(roleIds, oldRoles, (Integer i1, Integer i2) -> i1.equals(i2) ? 0 : 1);

        Collection<Integer> delIds = compareResult.getDelValue();
        Collection<Integer> addIds = compareResult.getAddValue();

        delIds.forEach(r -> {
            deleteByCondition(RoleUser.builder().userId(userId).roleId(r).build());
        });

        LocalDateTime now = LocalDateTime.now();
        for (Integer id : addIds) {
            BusinessAssert.notNull(roleService.getById(id), "角色不存在");
            RoleUser ru = RoleUser.builder().roleId(id).userId(userId).createTime(now).build();
            save(ru);
        }
    }
}
