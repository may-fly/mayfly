package mayfly.sys.module.open.controller;

import mayfly.core.exception.BusinessAssert;
import mayfly.core.permission.Permission;
import mayfly.core.result.Result;
import mayfly.sys.module.open.controller.form.AccountLoginForm;
import mayfly.sys.module.open.service.OpenService;
import mayfly.sys.module.sys.entity.AccountDO;
import mayfly.sys.module.sys.service.AccountService;
import mayfly.sys.module.sys.service.PermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * @author meilin.huang
 * @version 1.0
 * @date 2019-07-06 15:03
 */
@RestController
@RequestMapping("/open")
@Permission(requireCode = false)
public class OpenController {

    @Autowired
    private PermissionService permissionService;
    @Autowired
    private AccountService accountService;
    @Autowired
    private OpenService openService;

    @GetMapping("/captcha")
    public Result<?> captcha() {
        return Result.success(openService.generateCaptcha());
    }

    @PostMapping("/v1/login")
    public Result<?> login(@RequestBody @Valid AccountLoginForm loginForm) {
        BusinessAssert.state(openService.checkCaptcha(loginForm.getUuid(), loginForm.getCaptcha()), "验证码错误");
        AccountDO result = accountService.login(loginForm);
        BusinessAssert.notNull(result, "用户名或密码错误！");
        return Result.success(permissionService.saveIdAndPermission(result));
    }
}
