package mayfly.sys.web;

import mayfly.common.log.MethodLog;
import mayfly.common.result.Result;
import mayfly.sys.service.MenuService;
import mayfly.sys.service.PermissionService;
import mayfly.sys.web.form.AdminLoginForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Description: 管理员控制器
 * @author: hml
 * @date: 2018/6/27 下午4:44
 */
@RestController
@RequestMapping("/open")
public class AdminController {

    @Autowired
    private PermissionService permissionService;
    @Autowired
    private MenuService menuService;

    @MethodLog("管理员登录")
    @PostMapping("/v1/login")
    public Result login(AdminLoginForm loginForm) {
        return Result.success().withData(permissionService.saveIdAndPermission(1));
    }

}
