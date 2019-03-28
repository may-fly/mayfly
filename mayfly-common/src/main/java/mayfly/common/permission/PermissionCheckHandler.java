package mayfly.common.permission;

import mayfly.common.permission.check.SysPermissionCheck;
import mayfly.common.permission.check.UserPermissionCheck;

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
    private UserPermissionCheck userPermissionCheck;

    private SysPermissionCheck sysPermissionCheck;

    private PermissionCheckHandler(UserPermissionCheck userPermissionCheck, SysPermissionCheck sysPermissionCheck) {
        this.userPermissionCheck = userPermissionCheck;
        this.sysPermissionCheck = sysPermissionCheck;
    }

    public static PermissionCheckHandler of(UserPermissionCheck userPermissionCheck, SysPermissionCheck sysPermissionCheck){
        return new PermissionCheckHandler(userPermissionCheck, sysPermissionCheck);
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
        if (userPermissionCheck.has(userId, permissionCode)) {
            // 判断该权限是否有被禁用,可用于判断实时禁用
            if (sysPermissionCheck != null && sysPermissionCheck.has(PermissionCacheHandler.getDisablePermissionCode(permissionCode))) {
                throw new PermissionDisabledException();
            }
            return true;
        }
        // 判断该权限是否有被禁用
        if (userPermissionCheck.has(userId, PermissionCacheHandler.getDisablePermissionCode(permissionCode))) {
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
        Permission permission = method.getDeclaringClass().getAnnotation(Permission.class);
        if (permission == null) {
            permission = method.getAnnotation(Permission.class);
            if (permission == null) {
                return null;
            }

            return new PermissionInfo(permission.code());
        }

        String classCode = permission.code();
        return Optional.ofNullable(method.getAnnotation(Permission.class))
                .map(p -> new PermissionInfo(classCode + p.code()))
                .orElse(new PermissionInfo(classCode + method.getName()));
    }
}
