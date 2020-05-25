package mayfly.core.permission.registry;

import mayfly.core.permission.LoginAccount;
import mayfly.core.permission.PermissionDisabledException;
import mayfly.core.permission.PermissionInfo;
import mayfly.core.util.StringUtils;

import java.lang.reflect.Method;

/**
 * 权限校验处理器
 *
 * @author meilin.huang
 * @version 1.0
 * @date 2019-03-28 2:03 PM
 */
public class PermissionCheckHandler {

    /**
     * 用户权限校验
     */
    private final SimpleLoginAccountRegistry loginAccountRegistry;

    private PermissionCheckHandler(SimpleLoginAccountRegistry loginAccountRegistry) {
        this.loginAccountRegistry = loginAccountRegistry;
    }

    /**
     * 权限检查器工厂方法
     *
     * @param loginAccountRegistry login account registry
     * @return PermissionCheckHandler
     */
    public static PermissionCheckHandler of(SimpleLoginAccountRegistry loginAccountRegistry) {
        if (loginAccountRegistry == null) {
            return getDefaultHandler();
        }
        return new PermissionCheckHandler(loginAccountRegistry);
    }

    /**
     * 默认权限检查器工厂方法（使用{@linkplain DefaultLoginAccountRegistry}）
     *
     * @return PermissionCheckHandler
     */
    public static PermissionCheckHandler getDefaultHandler() {
        return new PermissionCheckHandler(DefaultLoginAccountRegistry.getInstance());
    }

    /**
     * 判断指定方法是否需要权限校验，需要则校验用户是否具有该方法@Permisison注解对应的code
     *
     * @param token  请求token
     * @param method 用户访问的方法
     * @return true 拥有该权限
     * @throws PermissionDisabledException 权限禁用异常
     */
    public boolean hasPermission(String token, Method method) throws PermissionDisabledException {
        LoginAccount loginAccount;
        if (StringUtils.isEmpty(token) || (loginAccount = loginAccountRegistry.getLoginAccount(token)) == null) {
            return false;
        }
        LoginAccount.setToContext(loginAccount);
        PermissionInfo pi = PermissionInfo.parse(method);
        if (pi == null) {
            return true;
        }

        return hasPermission(loginAccount, pi);
    }

    /**
     * 判断指定token是否拥有指定权限
     *
     * @param token          token
     * @param permissionInfo permission info
     * @return true 拥有该权限
     * @throws PermissionDisabledException 权限禁用异常
     */
    public boolean hasPermission(String token, PermissionInfo permissionInfo) throws PermissionDisabledException {
        LoginAccount loginAccount;
        if (StringUtils.isEmpty(token) || (loginAccount = loginAccountRegistry.getLoginAccount(token)) == null) {
            return false;
        }
        LoginAccount.setToContext(loginAccount);

        return hasPermission(loginAccount, permissionInfo);
    }

    /**
     * 判断用户是否拥有指定权限code
     *
     * @param loginAccount   登录账号
     * @param permissionInfo 权限info
     * @return true：拥有该权限
     * @throws PermissionDisabledException 若不存在权限code,而存在与之对应的禁用权限code,抛出此异常
     */
    public boolean hasPermission(LoginAccount loginAccount, PermissionInfo permissionInfo) throws PermissionDisabledException {
        // 判断code注册器是否含有该用户的权限code
        return loginAccount.hasPermission(permissionInfo);
    }
}
