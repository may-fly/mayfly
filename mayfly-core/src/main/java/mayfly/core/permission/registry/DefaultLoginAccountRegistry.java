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
public class DefaultLoginAccountRegistry implements LoginAccountRegistry {

    private static final DefaultLoginAccountRegistry DEFAULT_REGISTRY = new DefaultLoginAccountRegistry();

    public static DefaultLoginAccountRegistry getInstance() {
        return DEFAULT_REGISTRY;
    }

    /**
     * 登录账号信息缓存
     */
    private final Map<String, LoginAccount> loginAccountMap = new ConcurrentHashMap<>(255);

    private DefaultLoginAccountRegistry() {
    }

    @Override
    public void save(String token, LoginAccount loginAccount, long time, TimeUnit timeUnit) {
        this.loginAccountMap.put(token, loginAccount);
        ScheduleUtils.schedule("removeLoginAccount", () -> {
            this.delete(token);
        }, time, timeUnit);
    }

    @Override
    public LoginAccount getLoginAccount(String token) {
        return loginAccountMap.get(token);
    }

    @Override
    public void delete(String token) {
        this.loginAccountMap.remove(token);
    }
}
