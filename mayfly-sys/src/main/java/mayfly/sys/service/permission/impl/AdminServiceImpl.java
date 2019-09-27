package mayfly.sys.service.permission.impl;

import mayfly.core.enums.BoolEnum;
import mayfly.core.util.BusinessAssert;
import mayfly.core.util.DigestUtils;
import mayfly.dao.AdminMapper;
import mayfly.entity.Admin;
import mayfly.sys.common.utils.BeanUtils;
import mayfly.sys.service.base.impl.BaseServiceImpl;
import mayfly.sys.service.permission.AdminService;
import mayfly.sys.web.permission.form.AdminForm;
import mayfly.sys.web.permission.form.AdminLoginForm;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Objects;

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
        Admin admin = getByCondition(condition);
        if (admin != null) {
            BusinessAssert.state(Objects.equals(admin.getStatus(), BoolEnum.TRUE.getValue()), "该账号已被禁用");
        }
        return admin;
    }

    @Override
    public void saveAdmin(AdminForm adminForm) {
        BusinessAssert.isNull(getByCondition(Admin.builder().username(adminForm.getUsername()).build()),
                "该用户名已存在");
        Admin admin = BeanUtils.copyProperties(adminForm, Admin.class);
        admin.setPassword(DigestUtils.md5DigestAsHex(adminForm.getPassword()));
        LocalDateTime now = LocalDateTime.now();
        admin.setCreateTime(now);
        admin.setUpdateTime(now);
        // 默认启用状态
        admin.setStatus(BoolEnum.TRUE.getValue());
        save(admin);
    }
}
