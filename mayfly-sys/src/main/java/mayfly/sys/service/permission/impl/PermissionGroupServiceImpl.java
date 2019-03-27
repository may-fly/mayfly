package mayfly.sys.service.permission.impl;

import mayfly.dao.PermissionGroupMapper;
import mayfly.entity.PermissionGroup;
import mayfly.sys.service.base.impl.BaseServiceImpl;
import mayfly.sys.service.permission.PermissionGroupService;
import org.springframework.stereotype.Service;

/**
 * @author meilin.huang
 * @version 1.0
 * @date 2019-03-26 2:54 PM
 */
@Service
public class PermissionGroupServiceImpl extends BaseServiceImpl<PermissionGroupMapper, PermissionGroup> implements PermissionGroupService {
}
