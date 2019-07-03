package mayfly.sys.service.permission;

import mayfly.common.exception.BusinessException;
import mayfly.entity.RoleResource;
import mayfly.sys.common.enums.ResourceTypeEnum;
import mayfly.sys.service.base.BaseService;

import java.util.List;

/**
 * @author meilin.huang
 * @version 1.0
 * @date 2019-06-30 11:58
 */
public interface RoleResourceService extends BaseService<RoleResource> {

    /**
     * 获取角色拥有的资源列表id
     * @param roleId
     * @return
     */
    List<Integer> listResourceId(Integer roleId, ResourceTypeEnum type);

    Boolean saveResource(Integer roleId, List<Integer> resourceIds, ResourceTypeEnum type) throws BusinessException;

    /**
     * 删除角色资源
     * @param id 资源id
     * @param type 资源类型
     */
    void deleteByResourceIdAndType(Integer id, ResourceTypeEnum type);
}
