package mayfly.sys.web.permission;

import mayfly.common.permission.Permission;
import mayfly.common.result.Page;
import mayfly.common.result.Result;
import mayfly.common.util.DigestUtils;
import mayfly.common.validation.annotation.Valid;
import mayfly.entity.Admin;
import mayfly.sys.common.utils.BeanUtils;
import mayfly.sys.service.permission.AdminService;
import mayfly.sys.service.permission.RoleUserService;
import mayfly.sys.web.form.PageForm;
import mayfly.sys.web.permission.form.AdminForm;
import mayfly.sys.web.permission.form.RoleUserForm;
import mayfly.sys.web.permission.query.AdminQuery;
import mayfly.sys.web.permission.vo.AdminVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
    @Autowired
    private RoleUserService roleUserService;

    @GetMapping("/v1/admins")
    public Result list(@Valid PageForm pageForm, AdminQuery adminQuery) {
        Page<Admin> re = adminService.listByCondition(BeanUtils.copyProperties(adminQuery, Admin.class), pageForm);
        return Result.success(Page.with(re.getTotal(), BeanUtils.copyProperties(re.getList(), AdminVO.class)));
    }

    @PostMapping("/v1/admins")
    public Result save(@Valid @RequestBody AdminForm adminForm) {
        Admin admin = BeanUtils.copyProperties(adminForm, Admin.class);
        admin.setPassword(DigestUtils.md5DigestAsHex(adminForm.getPassword()));
        adminService.save(admin);
        return Result.success(adminForm);
    }

    @GetMapping("/v1/admins/{id}/roles")
    public Result roles(@PathVariable Integer id) {
        return Result.success(roleUserService.listRoleIdByUserId(id));
    }

    @PostMapping("/v1/admins/{id}/roles")
    public Result saveRoles(@PathVariable Integer id, @RequestBody RoleUserForm adminForm) {
        List<Integer> ids;
        try {
            ids = Stream.of(adminForm.getRoleIds().split(",")).map(Integer::valueOf).collect(Collectors.toList());
        } catch (Exception e) {
            return Result.paramError("menuIds参数错误！");
        }
        roleUserService.saveRoles(id, ids);
        return Result.success();
    }

}
