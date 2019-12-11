package mayfly.core.permission.checker;

import mayfly.core.permission.Permission;
import mayfly.core.permission.registry.PermissionCacheHandler;
import mayfly.core.permission.PermissionDisabledException;
import mayfly.core.permission.PermissionInfo;
import mayfly.core.util.annotation.AnnotationUtils;

import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Optional;

/**
 * 权限校验处理器
 *
 * @author meilin.huang
 * @version 1.0
 * @date 2019-03-28 2:03 PM
 */
public class PermissionCheckHandler<I> {
    /**
     * 用户权限校验
     */
    private UserPermissionChecker<I> userPermissionChecker;

    private PermissionCheckHandler(UserPermissionChecker<I> userPermissionChecker) {
        this.userPermissionChecker = userPermissionChecker;
    }

    /**
     * 权限检查器工厂方法
     *
     * @param userPermissionChecker 用户权限检查（为null则使用默认检查器 {@link DefaultUserPermissionChecker}）
     * @return
     */
    public static <T> PermissionCheckHandler<T> of(UserPermissionChecker<T> userPermissionChecker) {
        if (userPermissionChecker == null) {
            userPermissionChecker = new DefaultUserPermissionChecker<T>();
        }
        return new PermissionCheckHandler<T>(userPermissionChecker);
    }

    /**
     * 判断用户是否拥有指定权限code
     *
     * @param userId         用户id
     * @param permissionCode 权限code
     * @return
     * @throws PermissionDisabledException 若不存在权限code,而存在与之对应的禁用权限code,抛出此异常
     */
    public boolean hasPermission(I userId, String permissionCode) throws PermissionDisabledException {
        //判断code注册器是否含有该用户的权限code
        if (userPermissionChecker.has(userId, permissionCode)) {
            return true;
        }
        // 判断该权限是否有被禁用
        if (userPermissionChecker.has(userId, PermissionCacheHandler.getDisablePermissionCode(permissionCode))) {
            throw new PermissionDisabledException();
        }
        return false;
    }

    /**
     * 判断指定方法是否需要权限校验，需要则校验用户是否具有该方法@Permisison注解对应的code
     *
     * @param userId 用户id
     * @param method 用户访问的方法
     * @return
     * @throws PermissionDisabledException
     */
    public boolean hasPermission(I userId, Method method) throws PermissionDisabledException {
        PermissionInfo pi = getPermissionInfo(method);
        if (pi == null) {
            return true;
        }
        return hasPermission(userId, pi.getPermissionCode());
    }

    /**
     * 从用户拥有的权限列表中判断用户是否含有该方法权限code
     *
     * @param method
     * @param permissionCodes
     * @return
     */
    public boolean hasPermission(Method method, Collection<String> permissionCodes) {
        PermissionInfo pi = getPermissionInfo(method);
        if (pi == null) {
            return true;
        }

        return permissionCodes.contains(pi.getPermissionCode());
    }

    /**
     * 根据方法获取对应的权限信息,如果方法声明类上有@{@linkplain Permission}注解则为类名权限code + 方法权限code(方法权限code不存在，则为方法名)<br/>
     * 如果声明类没有@{@linkplain Permission}注解则只返回方法上权限code
     *
     * @param method
     * @return
     */
    public PermissionInfo getPermissionInfo(Method method) {
        Permission permission = AnnotationUtils.getAnnotation(method.getDeclaringClass(), Permission.class);
        if (permission == null) {
            permission = AnnotationUtils.getAnnotation(method, Permission.class);
            if (permission == null) {
                return null;
            }

            return new PermissionInfo(permission.code());
        }

        String classCode = permission.code();
        return Optional.ofNullable(AnnotationUtils.getAnnotation(method, Permission.class))
                .map(p -> new PermissionInfo(classCode + p.code()))
                .orElse(new PermissionInfo(classCode + method.getName()));
    }
}
