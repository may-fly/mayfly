package mayfly.sys.service.permission.impl;

import mayfly.common.enums.BoolEnum;
import mayfly.common.util.BusinessAssert;
import mayfly.common.util.EnumUtils;
import mayfly.common.util.TreeUtils;
import mayfly.dao.ResourceMapper;
import mayfly.entity.Resource;
import mayfly.entity.RoleResource;
import mayfly.sys.common.enums.ResourceTypeEnum;
import mayfly.sys.common.utils.BeanUtils;
import mayfly.sys.service.base.impl.BaseServiceImpl;
import mayfly.sys.service.permission.PermissionService;
import mayfly.sys.service.permission.ResourceService;
import mayfly.sys.service.permission.RoleResourceService;
import mayfly.sys.web.permission.vo.ResourceListVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
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
        List<Resource> resources = resourceMapper.selectAll("pid ASC, weight DESC");
        return TreeUtils.generateTrees(BeanUtils.copyProperties(resources, ResourceListVO.class));
    }

    @Override
    public Resource saveResource(Resource resource) {
        if (resource.getPid() == null || resource.getPid().equals(0)) {
            resource.setPid(0);
            BusinessAssert.state(Objects.equals(resource.getType(), ResourceTypeEnum.MENU.getValue())
                    , "权限资源不能为根节点");
        } else {
            Resource pResource = getById(resource.getPid());
            BusinessAssert.notNull(pResource, "pid不存在！");
            BusinessAssert.state(Objects.equals(pResource.getType(), ResourceTypeEnum.MENU.getValue())
                    , "权限资源不能添加子节点");
        }
        // 如果是添加菜单，则该父节点不能存在有权限节点
        if (resource.getType().equals(ResourceTypeEnum.MENU.getValue())) {
            BusinessAssert.notEmpty(resource.getPath(), "菜单路径不能为空");
            // 查询指定pid节点下是否有权限节点
            Resource condition = Resource.builder().pid(resource.getPid()).type(ResourceTypeEnum.PERMISSION.getValue()).build();
            BusinessAssert.state(countByCondition(condition) == 0, "该菜单已有权限资源子节点，不能再添加菜单");
        } else {
            BusinessAssert.notEmpty(resource.getCode(), "权限code不能为空");
        }
        //默认启用
        resource.setStatus(BoolEnum.TRUE.getValue());
        LocalDateTime now = LocalDateTime.now();
        resource.setCreateTime(now);
        resource.setUpdateTime(now);
        return save(resource);
    }

    @Override
    public Resource updateResource(Resource resource) {
        Resource old = getById(resource.getId());
        BusinessAssert.notNull(old, "资源不存在");
        BusinessAssert.state(Objects.equals(resource.getType(), old.getType()), "资源类型不可变更");

        resource.setUpdateTime(LocalDateTime.now());

        if (Objects.equals(old.getType(), ResourceTypeEnum.MENU.getValue())) {
            return updateById(resource);
        }

        BusinessAssert.notEmpty(resource.getCode(), "权限code不能为空");
        updateById(resource);
        return resource;
    }

    @Override
    public Resource changeStatus(Integer id, Integer status) {
        BusinessAssert.state(EnumUtils.isExist(BoolEnum.values(), status), "状态值错误");
        Resource resource = getById(id);
        BusinessAssert.notNull(resource, "该资源不存在");
        // 状态不变直接返回
        if (Objects.equals(status, resource.getStatus())) {
            return resource;
        }
        resource.setStatus(status);
        resource.setUpdateTime(LocalDateTime.now());
        // 更新数据库状态
        updateById(resource);
        return resource;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void deleteResource(Integer id) {
        BusinessAssert.empty(listByCondition(Resource.builder().pid(id).build()), "请先删除该资源的子资源");
        BusinessAssert.state(deleteById(id), "删除菜单失败！");
        // 删除角色资源表中该菜单所关联的所有信息
        roleResourceService.deleteByCondition(RoleResource.builder().resourceId(id).build());
    }
}
