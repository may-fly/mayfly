package mayfly.sys.module.sys.service;

import mayfly.core.base.service.BaseService;
import mayfly.core.exception.BusinessException;
import mayfly.sys.module.sys.entity.RoleResource;

import java.util.List;

/**
 * @author meilin.huang
 * @version 1.0
 * @date 2019-06-30 11:58
 */
public interface RoleResourceService extends BaseService<RoleResource> {

    /**
     * 获取角色拥有的资源id列表
     *
     * @param roleId 角色id
     * @return 资源id列表
     */
    List<Integer> listResourceId(Integer roleId);

    Boolean saveResource(Integer roleId, List<Integer> resourceIds) throws BusinessException;
}
