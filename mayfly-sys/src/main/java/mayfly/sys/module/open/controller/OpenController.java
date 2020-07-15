package mayfly.sys.module.open.controller;

import mayfly.core.base.model.Result;
import mayfly.core.exception.BizAssert;
import mayfly.core.util.DateUtils;
import mayfly.core.util.HttpUtils;
import mayfly.core.util.JsonUtils;
import mayfly.core.util.MapUtils;
import mayfly.core.util.PlaceholderResolver;
import mayfly.sys.module.open.controller.form.AccountLoginForm;
import mayfly.sys.module.open.service.OpenService;
import mayfly.sys.module.sys.entity.AccountDO;
import mayfly.sys.module.sys.enums.LogTypeEnum;
import mayfly.sys.module.sys.service.AccountService;
import mayfly.sys.module.sys.service.OperationLogService;
import mayfly.sys.module.sys.service.PermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.Map;

/**
 * @author meilin.huang
 * @version 1.0
 * @date 2019-07-06 15:03
 */
@RestController
@RequestMapping("/open")
public class OpenController {

    private static final String IP_API = "http://ip-api.com/json/${ip}?lang=zh-CN";
    private static final String LOGIN_LOG_TEMP = "${username}于${time}在${regionName}-${city}(ip: ${ip})登录";

    @Autowired
    private PermissionService permissionService;
    @Autowired
    private AccountService accountService;
    @Autowired
    private OpenService openService;
    @Autowired
    private OperationLogService operationLogService;

    @GetMapping("/captcha")
    public Result<?> captcha() {
        return Result.success(openService.generateCaptcha());
    }

    @PostMapping("/login")
    public Result<?> login(@RequestBody @Valid AccountLoginForm loginForm) {
        BizAssert.isTrue(openService.checkCaptcha(loginForm.getUuid(), loginForm.getCaptcha()), "验证码错误");
        AccountDO result = accountService.login(loginForm);
        saveLoginLog(result);
        return Result.success(permissionService.saveIdAndPermission(result));
    }

    private void saveLoginLog(AccountDO accountDO) {
        try {
            HttpServletRequest request = ((ServletRequestAttributes) (RequestContextHolder.currentRequestAttributes())).getRequest();
            String ipAddr = request.getRemoteAddr();
            Map<String, Object> res = JsonUtils.parse(HttpUtils.get(PlaceholderResolver.getDefaultResolver().resolve(IP_API, ipAddr)));
            if ("fail".equals(MapUtils.getString(res, "status"))) {
                return;
            }
            String log = PlaceholderResolver.getDefaultResolver().resolve(LOGIN_LOG_TEMP, accountDO.getUsername(),
                    DateUtils.defaultFormat(LocalDateTime.now()), MapUtils.getString(res, "regionName"), MapUtils.getString(res, "city"),
                    ipAddr);
            operationLogService.asyncLog(log, LogTypeEnum.SYS_LOG);
        } catch (Exception e) {
            // skip
        }
    }
}
