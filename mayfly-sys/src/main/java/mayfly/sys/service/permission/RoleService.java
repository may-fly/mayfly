package mayfly.sys.service.permission;

import mayfly.common.exception.BusinessException;
import mayfly.entity.Role;
import mayfly.sys.common.enums.ResourceTypeEnum;
import mayfly.sys.service.base.BaseService;

import java.util.List;

/**
 * @author meilin.huang
 * @version 1.0
 * @date 2018-12-07 4:13 PM
 */
public interface RoleService extends BaseService<Role> {

    /**
     * 获取角色拥有的资源列表id
     * @param roleId
     * @return
     */
    List<Integer> listResourceId(Integer roleId, ResourceTypeEnum type);

    Boolean saveResource(Integer roleId, List<Integer> resourceIds, ResourceTypeEnum type) throws BusinessException;
}
