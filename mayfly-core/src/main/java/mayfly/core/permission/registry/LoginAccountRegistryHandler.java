package mayfly.core.permission.registry;

import mayfly.core.permission.LoginAccount;

import java.util.concurrent.TimeUnit;

/**
 * @author meilin.huang
 * @version 1.0
 * @date 2018-11-26 11:02 AM
 */
public final class LoginAccountRegistryHandler<I> {

    /**
     * 登录账号注册器
     */
    private final LoginAccountRegistry<I> loginAccountRegistry;


    private LoginAccountRegistryHandler(LoginAccountRegistry<I> loginAccountRegistry) {
        this.loginAccountRegistry = loginAccountRegistry;
    }

    /**
     * 权限缓存工厂方法
     *
     * @param loginAccountRegistry 登录账号注册器(null则使用默认注册器 {@link DefaultLoginAccountRegistry})
     * @return LoginAccountRegistryHandler
     */
    public static <T> LoginAccountRegistryHandler<T> of(LoginAccountRegistry<T> loginAccountRegistry) {
        if (loginAccountRegistry == null) {
            loginAccountRegistry = DefaultLoginAccountRegistry.<T>getInstance();
        }
        return new LoginAccountRegistryHandler<T>(loginAccountRegistry);
    }

    /**
     * 保存登录账号信息
     *
     * @param loginAccount 登录账号
     * @param time         时间
     * @param timeUnit     单位
     */
    public void saveLoginAccount(String token, LoginAccount<I> loginAccount, long time, TimeUnit timeUnit) {
        loginAccountRegistry.save(token, loginAccount, time, timeUnit);
    }

    public void removeLoginAccount(String token) {
        loginAccountRegistry.delete(token);
    }
}
