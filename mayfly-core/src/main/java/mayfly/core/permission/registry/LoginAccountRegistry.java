package mayfly.core.permission.registry;

import mayfly.core.permission.LoginAccount;

import java.util.concurrent.TimeUnit;

/**
 * 登录账号注册器 泛型<I>表示账号id类型
 *
 * @author meilin.huang
 * @version 1.0
 * @date 2019-03-23 8:17 PM
 */
public interface LoginAccountRegistry extends SimpleLoginAccountRegistry {

    /**
     * 保存登录账号信息
     *
     * @param token        token
     * @param loginAccount 登录账号
     * @param time         保存时间
     * @param timeUnit     时间单位
     */
    void save(String token, LoginAccount loginAccount, long time, TimeUnit timeUnit);

    /**
     * 删除token关联的账号信息
     *
     * @param token token
     */
    void delete(String token);
}
