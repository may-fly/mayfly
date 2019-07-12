package mayfly.sys.service.permission.impl;

import mayfly.common.util.DigestUtils;
import mayfly.dao.AdminMapper;
import mayfly.entity.Admin;
import mayfly.sys.service.base.impl.BaseServiceImpl;
import mayfly.sys.service.permission.AdminService;
import mayfly.sys.web.permission.form.AdminLoginForm;
import org.springframework.stereotype.Service;

/**
 * @author meilin.huang
 * @version 1.0
 * @date 2019-07-06 14:57
 */
@Service
public class AdminServiceImpl extends BaseServiceImpl<AdminMapper, Admin> implements AdminService {

    @Override
    public Admin login(AdminLoginForm adminForm) {
        Admin condition = Admin.builder().username(adminForm.getUsername())
                .password(DigestUtils.md5DigestAsHex(adminForm.getPassword())).build();
        return getByCondition(condition);
    }
}
