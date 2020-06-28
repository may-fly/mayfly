package mayfly.sys.module.open.controller;

import mayfly.core.exception.BizAssert;
import mayfly.core.base.model.Result;
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

    @PostMapping("/login")
    public Result<?> login(@RequestBody @Valid AccountLoginForm loginForm) {
        BizAssert.isTrue(openService.checkCaptcha(loginForm.getUuid(), loginForm.getCaptcha()), "验证码错误");
        AccountDO result = accountService.login(loginForm);
        return Result.success(permissionService.saveIdAndPermission(result));
    }
}
