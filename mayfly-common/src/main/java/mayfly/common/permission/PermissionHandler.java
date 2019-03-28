package mayfly.common.permission;

import mayfly.common.enums.StatusEnum;
import mayfly.common.permission.registry.DefaultUserPermissionCodeRegistry;
import mayfly.common.permission.registry.SysPermissionCodeRegistry;
import mayfly.common.permission.registry.UserPermissionCodeRegistry;
import mayfly.common.web.RequestUri;
import mayfly.common.web.UriMatchHandler;
import mayfly.common.web.UriPattern;

import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

/**
 * @author meilin.huang
 * @version 1.0
 * @date 2018-11-26 11:02 AM
 */
public final class PermissionHandler {

    /**
     * 权限码与状态分割符号
     */
    public static final String CODE_STATUS_SEPARATOR = ":";

    private static PermissionHandler permissionHandler = new PermissionHandler(new DefaultUserPermissionCodeRegistry(), null);

    /**
     * 用户权限码注册
     */
    private UserPermissionCodeRegistry userCodeRegistry;

    /**
     * 系统所有权限码注册
     */
    private SysPermissionCodeRegistry sysCodeRegistry;

    /**
     * 判断是否保存了系统所有权限信息
     */
    private boolean saveSysCode = false;

    /**
     * uri匹配处理器
     */
    private UriMatchHandler uriMatchHandler = UriMatchHandler.getInstance();

    private PermissionHandler(UserPermissionCodeRegistry userCodeRegistry, SysPermissionCodeRegistry sysCodeRegistry){
        this.userCodeRegistry = userCodeRegistry;
        this.sysCodeRegistry = sysCodeRegistry;
    }

    public static final PermissionHandler getInstance() {
        return permissionHandler;
    }

    public PermissionHandler withUserCodeRegistry(UserPermissionCodeRegistry registry) {
        this.userCodeRegistry = registry;
        return this;
    }

    public PermissionHandler withSysCodeRegistry(SysPermissionCodeRegistry sysCodeRegistry) {
        this.sysCodeRegistry = sysCodeRegistry;
        return this;
    }

    /**
     * 保存用户权限列表
     * @param userId  用户id
     * @param permissionCodes  权限列表
     * @param time  时间
     * @param timeUnit  时间单位
     */
    public void savePermission(Integer userId, Collection<String> permissionCodes, long time, TimeUnit timeUnit) {
        //如果首次没有保存系统所有权限，则保存系统所有权限
        if (!saveSysCode && sysCodeRegistry != null) {
            sysCodeRegistry.save();
            saveSysCode = true;
        }
        userCodeRegistry.save(userId, permissionCodes, time, timeUnit);
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
        if (userCodeRegistry.has(userId, permissionCode)) {
            // 判断该权限是否有被禁用,可用于判断实时禁用
            if (sysCodeRegistry != null && sysCodeRegistry.has(getDisablePermissionCode(permissionCode))) {
                throw new PermissionDisabledException();
            }
            return true;
        }
        // 判断该权限是否有被禁用
        if (userCodeRegistry.has(userId, getDisablePermissionCode(permissionCode))) {
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

    /**
     * 禁用指定权限code的权限
     * @param permissionCode
     */
    public void disabledPermission(String permissionCode) {
        if (sysCodeRegistry != null && sysCodeRegistry.has(permissionCode)) {
            sysCodeRegistry.rename(permissionCode, getDisablePermissionCode(permissionCode));
        }
    }

    /**
     * 启用指定权限code的权限
     * @param permissionCode
     */
    public void enablePermission(String permissionCode) {
        String disableCode = getDisablePermissionCode(permissionCode);
        if (sysCodeRegistry != null && sysCodeRegistry.has(disableCode)) {
            sysCodeRegistry.rename(disableCode, permissionCode);
        }
    }


    /**
     * 返回与requestUri匹配的uriPattern
     * @param requestUri
     * @param uriPatterns
     * @return
     */
    public UriPattern matchAndReturnPattern(RequestUri requestUri, Collection<UriPattern>uriPatterns) {
        return this.uriMatchHandler.matchAndReturnPattern(requestUri, uriPatterns);
    }

    /**
     * 获取权限code对应的禁用code,即code + ":" + 0
     * @param code
     * @return
     */
    public static String getDisablePermissionCode(String code) {
        return code + CODE_STATUS_SEPARATOR + StatusEnum.DISABLE.getValue();
    }
}
