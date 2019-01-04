package mayfly.sys.service.permission;

import mayfly.common.exception.BusinessException;
import mayfly.entity.ApiGroup;
import mayfly.sys.service.base.BaseService;

/**
 * @author meilin.huang
 * @version 1.0
 * @date 2018-12-13 6:01 PM
 */
public interface ApiGroupService extends BaseService<ApiGroup> {

    ApiGroup saveGroup(ApiGroup apiGroup) throws BusinessException;
}
