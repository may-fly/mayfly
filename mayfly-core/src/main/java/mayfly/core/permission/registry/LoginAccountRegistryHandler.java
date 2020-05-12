package mayfly.core.permission.registry;

import mayfly.core.permission.LoginAccount;

import java.util.concurrent.TimeUnit;

/**
 * @author meilin.huang
 * @version 1.0
 * @date 2018-11-26 11:02 AM
 */
public final class LoginAccountRegistryHandler {

    /**
     * 登录账号注册器
     */
    private final LoginAccountRegistry loginAccountRegistry;


    private LoginAccountRegistryHandler(LoginAccountRegistry loginAccountRegistry) {
        this.loginAccountRegistry = loginAccountRegistry;
    }

    /**
     * 登录账号缓存处理器工厂方法
     *
     * @param loginAccountRegistry 登录账号注册器(null则使用默认注册器 {@link DefaultLoginAccountRegistry})
     * @return LoginAccountRegistryHandler
     */
    public static LoginAccountRegistryHandler of(LoginAccountRegistry loginAccountRegistry) {
        if (loginAccountRegistry == null) {
            return getDefaultHandler();
        }
        return new LoginAccountRegistryHandler(loginAccountRegistry);
    }

    /**
     * 获取默认的登录账号缓存处理器工厂方法（默认使用{@linkplain DefaultLoginAccountRegistry}注册）
     *
     * @return LoginAccountRegistryHandler
     */
    public static LoginAccountRegistryHandler getDefaultHandler() {
        return new LoginAccountRegistryHandler(DefaultLoginAccountRegistry.getInstance());
    }

    /**
     * 保存登录账号信息
     *
     * @param loginAccount 登录账号
     * @param time         时间
     * @param timeUnit     单位
     */
    public void saveLoginAccount(String token, LoginAccount loginAccount, long time, TimeUnit timeUnit) {
        loginAccountRegistry.save(token, loginAccount, time, timeUnit);
    }

    public void removeLoginAccount(String token) {
        loginAccountRegistry.delete(token);
    }
}
