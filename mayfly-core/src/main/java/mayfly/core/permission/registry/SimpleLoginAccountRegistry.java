package mayfly.core.permission.registry;

import mayfly.core.permission.LoginAccount;

/**
 * 简单登录账号注册器，只含有获取登录账号方法
 *
 * @author meilin.huang
 * @date 2020-03-06 8:41 上午
 */
public interface SimpleLoginAccountRegistry {
    /**
     * 根据token获取登录账号信息
     *
     * @param token token
     * @return login account
     */
    LoginAccount getLoginAccount(String token);
}
