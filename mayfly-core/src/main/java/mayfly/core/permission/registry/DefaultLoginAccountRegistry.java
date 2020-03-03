package mayfly.core.permission.registry;

import mayfly.core.permission.LoginAccount;
import mayfly.core.thread.ScheduleUtils;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

/**
 * 默认登录账号注册器
 *
 * @author meilin.huang
 * @version 1.0
 * @date 2019-03-23 8:25 PM
 */
public class DefaultLoginAccountRegistry<I> implements LoginAccountRegistry<I> {

    @SuppressWarnings("all")
    private static DefaultLoginAccountRegistry defaultUserPermissionCodeRegistry = new DefaultLoginAccountRegistry();

    @SuppressWarnings("unchecked")
    public static <T> DefaultLoginAccountRegistry<T> getInstance() {
        return (DefaultLoginAccountRegistry<T>) defaultUserPermissionCodeRegistry;
    }

    /**
     * 权限缓存
     */
    private Map<String, LoginAccount<I>> loginAccountMap = new ConcurrentHashMap<>(255);

    private DefaultLoginAccountRegistry() {
    }

    @Override
    public void save(String token, LoginAccount<I> loginAccount, long time, TimeUnit timeUnit) {
        this.loginAccountMap.put(token, loginAccount);
        ScheduleUtils.schedule("removeLoginAccount", () -> {
            this.delete(token);
        }, time, timeUnit);
    }

    @Override
    public LoginAccount<I> getLoginAccount(String token) {
        return loginAccountMap.get(token);
    }

    @Override
    public void delete(String token) {
        this.loginAccountMap.remove(token);
    }
}
