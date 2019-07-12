package mayfly.sys.web.permission.controller;

import mayfly.common.permission.Permission;
import mayfly.common.result.Page;
import mayfly.common.result.Result;
import mayfly.common.validation.annotation.Valid;
import mayfly.entity.Admin;
import mayfly.sys.common.utils.BeanUtils;
import mayfly.sys.service.permission.AdminService;
import mayfly.sys.web.form.PageForm;
import mayfly.sys.web.permission.form.AdminForm;
import mayfly.sys.web.permission.query.AdminQuery;
import mayfly.sys.web.permission.vo.AdminVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 管理员控制器
 * @author hml
 * @date 2018/6/27 下午4:44
 */
@Permission(code = "admin:")
@RestController
@RequestMapping("/sys")
public class AdminController {

    @Autowired
    private AdminService adminService;

    @GetMapping("/v1/admins")
    public Result list(@Valid PageForm pageForm, AdminQuery adminQuery) {
        Page<Admin> re = adminService.listByCondition(BeanUtils.copyProperties(adminQuery, Admin.class), pageForm);
        return Result.success().with(Page.with(re.getTotal(), BeanUtils.copyProperties(re.getList(), AdminVO.class)));
    }

    @PostMapping("/v1/admin")
    public Result save(@Valid AdminForm adminForm) {
        Admin admin = BeanUtils.copyProperties(adminForm, Admin.class);
        adminService.save(admin);
        return Result.success().with(adminForm);
    }

}
