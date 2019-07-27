package mayfly.sys.service.permission.impl;

import mayfly.common.enums.BoolEnum;
import mayfly.common.util.BusinessAssert;
import mayfly.common.util.EnumUtils;
import mayfly.dao.ResourceMapper;
import mayfly.entity.Resource;
import mayfly.sys.common.enums.ResourceTypeEnum;
import mayfly.sys.service.base.impl.BaseServiceImpl;
import mayfly.sys.service.permission.PermissionService;
import mayfly.sys.service.permission.ResourceService;
import mayfly.sys.service.permission.RoleResourceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

/**
 * 菜单实现类
 * @author: hml
 * @date: 2018/6/27 下午4:09
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
    public List<Resource> listByUserId(Integer userId) {
        return genTreesByResources(resourceMapper.selectByUserId(userId));
    }

    @Override
    public List<Resource> listResource(Resource condition) {
        return genTreesByResources(resourceMapper.selectAll("pid ASC, weight DESC"));
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
            // 将权限code添加进redis set中
            permissionService.addPermission(resource.getCode());
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
        resource.setUpdateTime(LocalDateTime.now());
        if (Objects.equals(old.getType(), ResourceTypeEnum.MENU.getValue())) {
            BusinessAssert.notEmpty(resource.getPath(), "菜单路径不能为空");
            return updateById(resource);
        }

        BusinessAssert.notEmpty(resource.getCode(), "权限code不能为空");
        updateById(resource);
        permissionService.reloadPermission();
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
        // 处理权限资源
        if (Objects.equals(resource.getType(), ResourceTypeEnum.PERMISSION.getValue())) {
            // 重命名redis key,是禁用则将key改为 code:0形式，否则将code:0改为code
            permissionService.reloadPermission();
        }
        return resource;
    }

    @Override
    public void deleteResource(Integer id) {
        List<Integer> deleteIds = getChildrenIdByPid(id);
        for (Integer i : deleteIds) {
           BusinessAssert.state(deleteById(i), "删除菜单失败！");
           // 删除角色资源表中该菜单所关联的所有信息
           roleResourceService.deleteByResourceIdAndType(id, ResourceTypeEnum.MENU);
        }
        // 重新加载权限code
        permissionService.reloadPermission();
    }

    /**
     * 获取该菜单及其所有子菜单
     * @param id
     * @return
     */
    private List<Integer> getChildrenIdByPid(Integer id) {
        List<Integer> ids = new ArrayList<>();
        ids.add(id);
        Integer pid = id;
        a: for (Iterator<Resource> ite = listAll().iterator(); ite.hasNext(); ) {
            Resource resource = ite.next();
            if (resource.getPid().equals(pid)) {
                Integer menuId = resource.getId();
                ids.add(menuId);
                pid = menuId;
                ite.remove();
                break a;
            }
        }

        return ids;
    }

    /**
     * 生成菜单树
     * @param resources
     * @return
     */
    private List<Resource> genTreesByResources(List<Resource> resources) {
        //获取所有父节点
        List<Resource> roots = new ArrayList<>();
        for (Iterator<Resource> ite = resources.iterator(); ite.hasNext();) {
            Resource resource = ite.next();
            if (resource.getPid().equals(0)) {
                roots.add(resource);
                ite.remove();
            }
        }
        roots.forEach(r -> {
            setChildren(r, resources);
        });
        return roots;
    }

    /**
     * 从所有菜单列表中查找并设置parent的所有子节点
     * @param parent  父节点
     * @param resources   所有菜单列表
     */
    private void setChildren(Resource parent, List<Resource> resources) {
        List<Resource> children = new ArrayList<>();
        for (Iterator<Resource> ite = resources.iterator(); ite.hasNext();) {
            Resource resource = ite.next();
            if (resource.getPid().equals(parent.getId())) {
                children.add(resource);
                ite.remove();
            }
        }
        // 如果孩子为空，则直接返回,否则继续递归设置孩子的孩子
        if (children.isEmpty()) {
            return;
        }
        parent.setChildren(children);
        children.forEach(m -> {
            setChildren(m, resources);
        });
    }
}
