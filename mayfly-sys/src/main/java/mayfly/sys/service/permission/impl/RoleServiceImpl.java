package mayfly.sys.service.permission.impl;

import mayfly.common.exception.BusinessException;
import mayfly.common.util.CollectionUtils;
import mayfly.dao.RoleMapper;
import mayfly.dao.RoleResourceMapper;
import mayfly.entity.Role;
import mayfly.entity.RoleResource;
import mayfly.sys.common.enums.ResourceTypeEnum;
import mayfly.sys.service.base.impl.BaseServiceImpl;
import mayfly.sys.service.permission.MenuService;
import mayfly.sys.service.permission.PermissionService;
import mayfly.sys.service.permission.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author meilin.huang
 * @version 1.0
 * @date 2018-12-07 4:13 PM
 */
@Service
public class RoleServiceImpl extends BaseServiceImpl<RoleMapper, Role> implements RoleService {

    @Autowired
    private RoleMapper roleMapper;
    @Autowired
    private RoleResourceMapper roleResourceMapper;
    @Autowired
    private MenuService menuService;
    @Autowired
    private PermissionService permissionService;

    @Override
    public List<Integer> listResourceId(Integer roleId, ResourceTypeEnum type) {
        RoleResource condition = RoleResource.builder().roleId(roleId).type(type.getValue()).build();
        return roleResourceMapper.selectByCriteria(condition).stream().map(r -> r.getResourceId()).collect(Collectors.toList());
    }

    @Transactional
    @Override
    public Boolean saveResource(Integer roleId, List<Integer> resourceIds, ResourceTypeEnum type) throws BusinessException {
        List<Integer> oldIds = listResourceId(roleId, type);
        //和之前存的权限列表id比较，哪些是新增已经哪些是修改以及不变的
        CollectionUtils.CompareResult<Integer> compareResult = CollectionUtils
                .compare(resourceIds, oldIds, (Integer i1, Integer i2) -> i1.equals(i2) ? 0 : 1);

        Collection<Integer> delIds = compareResult.getDelValue();
        Collection<Integer> addIds = compareResult.getAddValue();

        delIds.forEach(id -> {
            roleResourceMapper.deleteByCriteria(RoleResource.builder()
                    .roleId(roleId).resourceId(id).type(type.getValue()).build());
        });

        LocalDateTime now = LocalDateTime.now();
        List<RoleResource> addValues = new ArrayList<>(addIds.size());
        for (Integer id : addIds) {
            if (type.getValue().equals(ResourceTypeEnum.PERMISSION.getValue())) {
                if (permissionService.getById(id) == null) {
                    throw new BusinessException("id : " + id + "的权限不存在！");
                }
            } else {
                if (menuService.getById(id) == null) {
                    throw new BusinessException("id : " + id + "的菜单不存在！");
                }
            }
            RoleResource rr = RoleResource.builder().roleId(roleId).resourceId(id).type(type.getValue()).createTime(now).build();
            roleResourceMapper.insert(rr);
        }

        return true;
    }
}
