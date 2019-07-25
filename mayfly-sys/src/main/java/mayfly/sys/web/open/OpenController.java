package mayfly.sys.web.open;

import mayfly.common.log.MethodLog;
import mayfly.common.result.Result;
import mayfly.common.validation.annotation.Valid;
import mayfly.entity.Admin;
import mayfly.sys.service.permission.AdminService;
import mayfly.sys.service.permission.PermissionService;
import mayfly.sys.web.permission.form.AdminLoginForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author meilin.huang
 * @version 1.0
 * @date 2019-07-06 15:03
 */
@RestController
@RequestMapping("/open")
public class OpenController {

    @Autowired
    private PermissionService permissionService;
    @Autowired
    private AdminService adminService;

    @MethodLog(value = "管理员登录", result = false)
    @PostMapping("/v1/login")
    public Result login(@RequestBody @Valid AdminLoginForm loginForm) {
        Admin result = adminService.login(loginForm);
        if (result == null) {
            return Result.noFound("用户名或密码错误！");
        }
        return Result.success().with(permissionService.saveIdAndPermission(result));
    }
}
