package mayfly.sys.service.permission.impl;

import mayfly.core.exception.BusinessException;
import mayfly.core.util.BusinessAssert;
import mayfly.core.util.CollectionUtils;
import mayfly.dao.RoleResourceMapper;
import mayfly.entity.Resource;
import mayfly.entity.RoleResource;
import mayfly.sys.service.base.impl.BaseServiceImpl;
import mayfly.sys.service.permission.PermissionService;
import mayfly.sys.service.permission.ResourceService;
import mayfly.sys.service.permission.RoleResourceService;
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
 * @date 2019-06-30 11:59
 */
@Service
public class RoleResourceServiceImpl extends BaseServiceImpl<RoleResourceMapper, RoleResource> implements RoleResourceService {

    @Autowired
    private PermissionService permissionService;
    @Autowired
    private ResourceService resourceService;

    @Override
    public List<Integer> listResourceId(Integer roleId) {
        RoleResource condition = RoleResource.builder().roleId(roleId).build();
        return listByCondition(condition).stream().map(RoleResource::getResourceId).collect(Collectors.toList());
    }

    @Transactional
    @Override
    public Boolean saveResource(Integer roleId, List<Integer> resourceIds) throws BusinessException {
        List<Integer> oldIds = listResourceId(roleId);
        //和之前存的权限列表id比较，哪些是新增已经哪些是修改以及不变的
        CollectionUtils.CompareResult<Integer> compareResult = CollectionUtils
                .compare(resourceIds, oldIds, (Integer i1, Integer i2) -> i1.equals(i2) ? 0 : 1);

        Collection<Integer> delIds = compareResult.getDelValue();
        Collection<Integer> addIds = compareResult.getAddValue();

        delIds.forEach(id -> {
            deleteByCondition(RoleResource.builder()
                    .roleId(roleId).resourceId(id).build());
        });

        LocalDateTime now = LocalDateTime.now();
        List<RoleResource> addValues = new ArrayList<>(addIds.size());
        for (Integer id : addIds) {
            Resource r = resourceService.getById(id);
            BusinessAssert.notNull(r, "id : " + id + "的资源不存在！");
            RoleResource rr = RoleResource.builder().roleId(roleId).resourceId(id).createTime(now).build();
            save(rr);
        }

        return true;
    }
}
