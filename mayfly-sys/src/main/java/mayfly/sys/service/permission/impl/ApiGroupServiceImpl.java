package mayfly.sys.service.permission.impl;

import mayfly.common.enums.StatusEnum;
import mayfly.common.exception.BusinessException;
import mayfly.dao.ApiGroupMapper;
import mayfly.entity.ApiGroup;
import mayfly.sys.service.permission.ApiGroupService;
import mayfly.sys.service.base.impl.BaseServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
 * @author meilin.huang
 * @version 1.0
 * @date 2018-12-13 6:01 PM
 */
@Service
public class ApiGroupServiceImpl extends BaseServiceImpl<ApiGroupMapper, ApiGroup> implements ApiGroupService {

    @Autowired
    private ApiGroupMapper apiGroupMapper;

    @Override
    public ApiGroup saveGroup(ApiGroup apiGroup) throws BusinessException {
        if (countByCondition(ApiGroup.builder().name(apiGroup.getName()).build()) != 0) {
            throw new BusinessException("该组名已经存在！");
        }
        LocalDateTime now = LocalDateTime.now();
        apiGroup.setCreateTime(now);
        apiGroup.setUpdateTime(now);
        apiGroup.setStatus(StatusEnum.ENABLE.value());
        return save(apiGroup);
    }
}
