package mayfly.common.permission.checker;

import mayfly.common.permission.Permission;
import mayfly.common.permission.registry.PermissionCacheHandler;
import mayfly.common.permission.PermissionDisabledException;
import mayfly.common.permission.PermissionInfo;
import mayfly.common.utils.AnnotationUtils;

import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Optional;

/**
 * 权限校验处理器
 * @author meilin.huang
 * @version 1.0
 * @date 2019-03-28 2:03 PM
 */
public class PermissionCheckHandler {
    /**
     * 用户权限校验
     */
    private UserPermissionChecker userPermissionChecker;

    /**
     * 系统所有权限校验
     */
    private SysPermissionChecker sysPermissionChecker;

    private PermissionCheckHandler(UserPermissionChecker userPermissionChecker, SysPermissionChecker sysPermissionChecker) {
        this.userPermissionChecker = userPermissionChecker;
        this.sysPermissionChecker = sysPermissionChecker;
    }

    /**
     * 权限检查器工厂方法
     * @param userPermissionChecker 用户权限检查（为null则使用默认检查器 {@link DefaultUserPermissionChecker}）
     * @param sysPermissionChecker 系统所有权限校验，可为空
     * @return
     */
    public static PermissionCheckHandler of(UserPermissionChecker userPermissionChecker, SysPermissionChecker sysPermissionChecker){
        if (userPermissionChecker == null) {
            userPermissionChecker = new DefaultUserPermissionChecker();
        }
        return new PermissionCheckHandler(userPermissionChecker, sysPermissionChecker);
    }

    /**
     * 判断用户是否拥有指定权限code
     * @param userId  用户id
     * @param permissionCode  权限code
     * @return
     * @throws PermissionDisabledException  若不存在权限code,而存在与之对应的禁用权限code,抛出此异常
     */
    public boolean hasPermission(Integer userId, String permissionCode) throws PermissionDisabledException{
        //判断code注册器是否含有该用户的权限code
        if (userPermissionChecker.has(userId, permissionCode)) {
            // 判断该权限是否有被禁用,可用于判断实时禁用
            if (sysPermissionChecker != null && sysPermissionChecker.has(PermissionCacheHandler.getDisablePermissionCode(permissionCode))) {
                throw new PermissionDisabledException();
            }
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
     * @param userId
     * @param method
     * @return
     * @throws PermissionDisabledException
     */
    public boolean hasPermission(Integer userId, Method method) throws PermissionDisabledException{
        PermissionInfo pi = getPermissionInfo(method);
        if (pi == null) {
            return true;
        }
        return hasPermission(userId, pi.getPermissionCode());
    }

    /**
     * 从用户拥有的权限列表中判断用户是否含有该方法权限code
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
     * 根据方法获取对应的权限信息,如果方法声明类上有@Permission注解则为类名权限code + 方法权限code <br/>
     * 否则返回方法上权限code
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
