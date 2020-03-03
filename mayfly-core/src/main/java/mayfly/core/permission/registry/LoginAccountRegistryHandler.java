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
     * 用户权限码注册
     */
    private LoginAccountRegistry<I> userCodeRegistry;


    private LoginAccountRegistryHandler(LoginAccountRegistry<I> userCodeRegistry) {
        this.userCodeRegistry = userCodeRegistry;
    }

    /**
     * 权限缓存工厂方法
     *
     * @param userCodeRegistry 用户权限缓存器(null则使用默认注册器 {@link DefaultLoginAccountRegistry})
     * @return PermissionCacheHandler
     */
    public static <T> LoginAccountRegistryHandler<T> of(LoginAccountRegistry<T> userCodeRegistry) {
        if (userCodeRegistry == null) {
            userCodeRegistry = DefaultLoginAccountRegistry.<T>getInstance();
        }
        return new LoginAccountRegistryHandler<T>(userCodeRegistry);
    }

    /**
     * 保存登录账号信息
     *
     * @param loginAccount 登录账号
     * @param time         时间
     * @param timeUnit     单位
     */
    public void saveLoginAccount(String token, LoginAccount<I> loginAccount, long time, TimeUnit timeUnit) {
        userCodeRegistry.save(token, loginAccount, time, timeUnit);
    }

    public void removeLoginAccount(String token) {
        userCodeRegistry.delete(token);
    }
}
