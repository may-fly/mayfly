package mayfly.sys.service.permission;

import mayfly.common.util.CollectionUtils;
import mayfly.entity.Resource;
import mayfly.sys.service.base.BaseService;

import java.util.List;

/**
 * 资源服务接口
 * @author hml
 * @date 2018/6/27 下午3:14
 */
public interface ResourceService extends BaseService<Resource> {

    /**
     * 根据用户id获取用户的所有菜单权限
     * @param userId 用户id
     * @return   菜单列表
     */
    List<Resource> listByUserId(Integer userId);

    /**
     * 获取所有资源树
     * @return
     */
    List<Resource> listResource(Resource condition);

    /**
     * 删除指定菜单，如果是有子节点，也删除
     * @param id
     * @return
     */
    void deleteResource(Integer id);

    /**
     * 保存菜单
     * @param resource
     * @return
     *
     */
    Resource saveResource(Resource resource);

    Resource updateResource(Resource resource);

    /**
     * 改变菜单的权限
     * @param id
     * @param status
     * @return
     */
    Resource changeStatus(Integer id, Integer status);

    /**
     * 获取菜单节点的所有叶子节点
     * @param parent  菜单树的父节点
     * @param leafs   将所有叶子节点添加进该列表
     */
    default void fillLeaf(Resource parent, List<Resource> leafs) {
        List<Resource> children = parent.getChildren();
        // 如果节点没有子节点则说明为叶子节点
        if (CollectionUtils.isEmpty(children)) {
            leafs.add(parent);
            return;
        }
        // 递归调用子节点，查找叶子节点
        for (Resource child : children) {
            fillLeaf(child, leafs);
        }
    }
}
