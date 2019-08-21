package mayfly.sys.service.permission;

import mayfly.entity.Admin;
import mayfly.sys.service.base.BaseService;
import mayfly.sys.web.permission.form.AdminForm;
import mayfly.sys.web.permission.form.AdminLoginForm;

/**
 * @author meilin.huang
 * @version 1.0
 * @date 2019-07-06 14:56
 */
public interface AdminService extends BaseService<Admin> {

    Admin login(AdminLoginForm adminForm);

    void saveAdmin(AdminForm adminForm);
}
