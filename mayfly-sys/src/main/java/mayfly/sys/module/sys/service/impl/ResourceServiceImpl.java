package mayfly.sys.module.sys.service.impl;

import mayfly.core.exception.BusinessAssert;
import mayfly.core.util.TreeUtils;
import mayfly.core.util.enums.EnumUtils;
import mayfly.sys.common.base.service.impl.BaseServiceImpl;
import mayfly.sys.common.enums.EnableDisableEnum;
import mayfly.sys.common.utils.BeanUtils;
import mayfly.sys.module.sys.controller.vo.ResourceListVO;
import mayfly.sys.module.sys.entity.Resource;
import mayfly.sys.module.sys.entity.RoleResource;
import mayfly.sys.module.sys.enums.ResourceTypeEnum;
import mayfly.sys.module.sys.mapper.ResourceMapper;
import mayfly.sys.module.sys.service.PermissionService;
import mayfly.sys.module.sys.service.ResourceService;
import mayfly.sys.module.sys.service.RoleResourceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

/**
 * 菜单实现类
 *
 * @author hml
 * @date 2018/6/27 下午4:09
 */
@Service
public class ResourceServiceImpl extends BaseServiceImpl<ResourceMapper, Resource> implements ResourceService {

    @Autowired
    private ResourceMapper resourceMapper;
    @Autowired
    private RoleResourceService roleResourceService;
    @Autowired
    private PermissionService permissionService;

    @Override
    public List<ResourceListVO> listByUserId(Integer userId) {
        return TreeUtils.generateTrees(BeanUtils.copyProperties(resourceMapper.selectByUserId(userId), ResourceListVO.class));
    }

    @Override
    public List<ResourceListVO> listResource(Resource condition) {
        List<Resource> resources = listAll("pid ASC, weight ASC");
        return TreeUtils.generateTrees(BeanUtils.copyProperties(resources, ResourceListVO.class));
    }

    @Override
    public Resource saveResource(Resource resource) {
        if (resource.getPid() == null || resource.getPid().equals(0)) {
            resource.setPid(0);
            BusinessAssert.equals(resource.getType(), ResourceTypeEnum.MENU.getValue(), "权限资源不能为根节点");
        } else {
            Resource pResource = getById(resource.getPid());
            BusinessAssert.notNull(pResource, "pid不存在！");
            BusinessAssert.equals(pResource.getType(), ResourceTypeEnum.MENU.getValue(), "权限资源不能添加子节点");
        }
        // 如果是添加菜单，则该父节点不能存在有权限节点
        if (resource.getType().equals(ResourceTypeEnum.MENU.getValue())) {
            // 查询指定pid节点下是否有权限节点
            Resource condition = Resource.builder().pid(resource.getPid()).type(ResourceTypeEnum.PERMISSION.getValue()).build();
            BusinessAssert.state(countByCondition(condition) == 0, "该菜单已有权限资源子节点，不能再添加菜单");
        } else {
            BusinessAssert.notEmpty(resource.getCode(), "权限code不能为空");
        }
        //默认启用
        resource.setStatus(EnableDisableEnum.ENABLE.getValue());
        insert(resource);
        return resource;
    }

    @Override
    public Resource updateResource(Resource resource) {
        Resource old = getById(resource.getId());
        BusinessAssert.notNull(old, "资源不存在");
        BusinessAssert.equals(resource.getType(), old.getType(), "资源类型不可变更");
        // 禁止误传修改其父节点
        resource.setPid(null);

        if (Objects.equals(old.getType(), ResourceTypeEnum.MENU.getValue())) {
            updateByIdSelective(resource);
            return resource;
        }
        // 权限类型需要校验code不能为空
        BusinessAssert.notEmpty(resource.getCode(), "权限code不能为空");
        updateByIdSelective(resource);
        return resource;
    }

    @Override
    public Resource changeStatus(Integer id, Integer status) {
        BusinessAssert.state(EnumUtils.isExist(EnableDisableEnum.values(), status), "状态值错误");
        Resource resource = getById(id);
        BusinessAssert.notNull(resource, "该资源不存在");
        // 状态不变直接返回
        if (Objects.equals(status, resource.getStatus())) {
            return resource;
        }
        resource.setStatus(status);
        // 更新数据库状态
        updateByIdSelective(resource);
        return resource;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void deleteResource(Integer id) {
        BusinessAssert.empty(listByCondition(Resource.builder().pid(id).build()), "请先删除该资源的子资源");
        BusinessAssert.state(deleteById(id) == 1, "删除菜单失败！");
        // 删除角色资源表中该菜单所关联的所有信息
        roleResourceService.deleteByCondition(RoleResource.builder().resourceId(id).build());
    }
}
