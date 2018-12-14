package mayfly.sys.service.impl;

import mayfly.dao.RoleMapper;
import mayfly.dao.RoleMenuMapper;
import mayfly.entity.Role;
import mayfly.sys.service.RoleService;
import mayfly.sys.service.base.impl.BaseServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author meilin.huang
 * @version 1.0
 * @date 2018-12-07 4:13 PM
 */
@Service
public class RoleServiceImplImpl extends BaseServiceImpl<RoleMapper, Role> implements RoleService {

    @Autowired
    private RoleMapper roleMapper;
    @Autowired
    private RoleMenuMapper roleMenuMapper;

}
