package mayfly.sys.module.open.controller;

import mayfly.core.result.Result;
import mayfly.core.validation.annotation.Valid;
import mayfly.sys.module.open.controller.form.AccountLoginForm;
import mayfly.sys.module.sys.entity.AccountDO;
import mayfly.sys.module.sys.service.AccountService;
import mayfly.sys.module.sys.service.PermissionService;
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
    private AccountService accountService;

    @PostMapping("/v1/login")
    public Result<?> login(@RequestBody @Valid AccountLoginForm loginForm) {
        AccountDO result = accountService.login(loginForm);
        if (result == null) {
            return Result.noFound("用户名或密码错误！");
        }
        return Result.success(permissionService.saveIdAndPermission(result));
    }
}
