package mayfly.sys.module.open.controller;

import lombok.extern.slf4j.Slf4j;
import mayfly.core.exception.BizAssert;
import mayfly.core.log.annotation.Log;
import mayfly.core.model.result.Response2Result;
import mayfly.core.thread.GlobalThreadPool;
import mayfly.core.util.DateUtils;
import mayfly.core.util.HttpUtils;
import mayfly.core.util.JsonUtils;
import mayfly.core.util.MapUtils;
import mayfly.core.util.PlaceholderResolver;
import mayfly.sys.module.open.controller.form.AccountLoginForm;
import mayfly.sys.module.open.controller.vo.CaptchaVO;
import mayfly.sys.module.open.service.OpenService;
import mayfly.sys.module.sys.controller.vo.LoginSuccessVO;
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

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.Map;

/**
 * @author meilin.huang
 * @version 1.0
 * @date 2019-07-06 15:03
 */
@Response2Result
@Slf4j
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

    //    @NeedSign()
    @GetMapping("/captcha")
    public CaptchaVO captcha() {
        return openService.generateCaptcha();
    }

    @PostMapping("/login")
    @Log(value = "用户登录", resLevel = Log.Level.NONE)
    public LoginSuccessVO login(@RequestBody @Valid AccountLoginForm loginForm) {
        BizAssert.isTrue(openService.checkCaptcha(loginForm.getUuid(), loginForm.getCaptcha()), "验证码错误");
        AccountDO result = accountService.login(loginForm);
        saveLoginLog(result);
        return permissionService.saveIdAndPermission(result);
    }

    private void saveLoginLog(AccountDO accountDO) {
        String ipAddr = ((ServletRequestAttributes) (RequestContextHolder.currentRequestAttributes())).getRequest().getRemoteAddr();
        GlobalThreadPool.execute(() -> {
            try {
                // 更新最后登录时间
                AccountDO updateDo = new AccountDO().setLastLoginTime(LocalDateTime.now());
                updateDo.setId(accountDO.getId());
                accountService.updateByIdSelective(updateDo);

                Map<String, Object> res = JsonUtils.parse(HttpUtils.get(PlaceholderResolver.getDefaultResolver().resolve(IP_API, ipAddr)));
                if ("fail".equals(MapUtils.getString(res, "status"))) {
                    return;
                }
                String log = PlaceholderResolver.getDefaultResolver().resolve(LOGIN_LOG_TEMP, accountDO.getUsername(),
                        DateUtils.defaultFormat(LocalDateTime.now()), MapUtils.getString(res, "regionName"), MapUtils.getString(res, "city"),
                        ipAddr);
                operationLogService.asyncLog(log, LogTypeEnum.SYS_LOG);
            } catch (Exception e) {
                log.error("执行登录日志保存异常", e);
            }
        });
    }
}
