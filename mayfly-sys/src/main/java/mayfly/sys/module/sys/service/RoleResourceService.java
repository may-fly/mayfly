package mayfly.sys.module.sys.service;

import mayfly.core.base.service.BaseService;
import mayfly.sys.module.sys.controller.vo.RoleResourceVO;
import mayfly.sys.module.sys.entity.RoleResourceDO;

import java.util.List;

/**
 * @author meilin.huang
 * @version 1.0
 * @date 2019-06-30 11:58
 */
public interface RoleResourceService extends BaseService<Long, RoleResourceDO> {

    /**
     * 获取角色拥有的资源id列表
     *
     * @param roleId 角色id
     * @return 资源id列表
     */
    List<Long> listResourceId(Long roleId);

    /**
     * 获取角色拥有的资源列表树
     *
     * @param roleId 角色id
     * @return 资源列表树
     */
    List<RoleResourceVO> listResource(Long roleId);

    /**
     * 保存角色关联的资源信息
     *
     * @param roleId 角色id
     * @param resourceIds 资源id列表
     */
    void saveResource(Long roleId, List<Long> resourceIds);
}
