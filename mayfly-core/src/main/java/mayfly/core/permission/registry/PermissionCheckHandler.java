package mayfly.core.permission.registry;

import mayfly.core.exception.BusinessException;
import mayfly.core.permission.LoginAccount;
import mayfly.core.permission.Permission;
import mayfly.core.permission.PermissionDisabledException;
import mayfly.core.permission.PermissionInfo;
import mayfly.core.util.StringUtils;
import mayfly.core.util.annotation.AnnotationUtils;

import java.lang.reflect.Method;

/**
 * 权限校验处理器
 *
 * @author meilin.huang
 * @version 1.0
 * @date 2019-03-28 2:03 PM
 */
public class PermissionCheckHandler<I> {

    /**
     * 权限码与状态分割符号
     */
    public static final String CODE_STATUS_SEPARATOR = ":";

    /**
     * 用户权限校验
     */
    private LoginAccountRegistry<I> loginAccountRegistry;

    private PermissionCheckHandler(LoginAccountRegistry<I> loginAccountRegistry) {
        this.loginAccountRegistry = loginAccountRegistry;
    }

    /**
     * 权限检查器工厂方法
     *
     * @param loginAccountRegistry login account registry
     * @return PermissionCheckHandler
     */
    public static <T> PermissionCheckHandler<T> of(LoginAccountRegistry<T> loginAccountRegistry) {
        if (loginAccountRegistry == null) {
            loginAccountRegistry = DefaultLoginAccountRegistry.getInstance();
        }
        return new PermissionCheckHandler<T>(loginAccountRegistry);
    }

    /**
     * 判断指定方法是否需要权限校验，需要则校验用户是否具有该方法@Permisison注解对应的code
     *
     * @param token  请求token
     * @param method 用户访问的方法
     * @return true 拥有该权限
     * @throws PermissionDisabledException 权限禁用异常
     */
    public boolean hasPermission(String token, Method method) throws BusinessException {
        LoginAccount<I> loginAccount;
        if (StringUtils.isEmpty(token) || (loginAccount = loginAccountRegistry.getLoginAccount(token)) == null) {
            return false;
        }
        LoginAccount.set(loginAccount);
        PermissionInfo pi = getPermissionInfo(method);
        if (pi == null) {
            return true;
        }

        return hasPermission(loginAccount, pi.getPermissionCode());
    }

    /**
     * 判断用户是否拥有指定权限code
     *
     * @param loginAccount   登录账号
     * @param permissionCode 权限code
     * @return true：拥有该权限
     * @throws PermissionDisabledException 若不存在权限code,而存在与之对应的禁用权限code,抛出此异常
     */
    public boolean hasPermission(LoginAccount<I> loginAccount, String permissionCode) throws BusinessException {
        //判断code注册器是否含有该用户的权限code
        if (loginAccount.hasPermission(permissionCode)) {
            return true;
        }
        // 判断该权限是否有被禁用
        if (loginAccount.hasPermission(getDisablePermissionCode(permissionCode))) {
            throw new PermissionDisabledException();
        }
        throw new BusinessException("没有该权限");
    }

    /**
     * 根据方法获取对应的权限信息,如果方法声明类上有@{@linkplain Permission}注解则为类名权限code + 方法权限code(方法权限code不存在，则为方法名)<br/>
     * 如果声明类没有@{@linkplain Permission}注解则只返回方法上权限code
     *
     * @param method 方法
     * @return 权限信息
     */
    public PermissionInfo getPermissionInfo(Method method) {
        Permission permission = AnnotationUtils.getAnnotation(method.getDeclaringClass(), Permission.class);
        if (permission == null) {
            permission = AnnotationUtils.getAnnotation(method, Permission.class);
            if (permission == null || !permission.requireCode()) {
                return null;
            }

            return new PermissionInfo(permission.code());
        }

        String classCode = permission.code();
        Permission methodCodeAnno = AnnotationUtils.getAnnotation(method, Permission.class);
        if (methodCodeAnno != null) {
            if (!methodCodeAnno.requireCode()) {
                return null;
            }
            return new PermissionInfo(classCode + methodCodeAnno.code());
        } else {
            return new PermissionInfo(classCode + method.getName());
        }
    }

    /**
     * 获取权限code对应的禁用code,即code + ":" + 0
     *
     * @param code code
     * @return 禁用code
     */
    public static String getDisablePermissionCode(String code) {
        return code + CODE_STATUS_SEPARATOR + 0;
    }
}
