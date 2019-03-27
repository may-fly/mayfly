package mayfly.common.permission;

import mayfly.common.enums.StatusEnum;
import mayfly.common.permission.registry.DefaultPermissionCodeRegistry;
import mayfly.common.permission.registry.PermissionCodeRegistry;
import mayfly.common.web.RequestUri;
import mayfly.common.web.UriMatchHandler;
import mayfly.common.web.UriPattern;

import java.lang.reflect.Method;
import java.util.Collection;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
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

    private static PermissionHandler permissionHandler = new PermissionHandler(new DefaultPermissionCodeRegistry());

    /**
     * 权限码注册
     */
    private PermissionCodeRegistry codeRegistry;

    /**
     * 所有最新的权限信息，包含最新是否禁用状态
     */
    private Set<String> allCode = new HashSet<>();

    /**
     * uri匹配处理器
     */
    private UriMatchHandler uriMatchHandler = UriMatchHandler.getInstance();

    private PermissionHandler(PermissionCodeRegistry codeRegistry){
        this.codeRegistry = codeRegistry;
    }

    public static final PermissionHandler getInstance() {
        return permissionHandler;
    }

    public PermissionHandler withCodeRegistry(PermissionCodeRegistry registry) {
        this.codeRegistry = registry;
        return this;
    }

    public void savePermission(Integer userId, Collection<String> permissionCodes, long time, TimeUnit timeUnit) {
        allCode.addAll(permissionCodes);
        codeRegistry.save(userId, permissionCodes, time, timeUnit);
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
        if (codeRegistry.has(userId, permissionCode)) {
            // 判断该权限是否有被禁用,可用于判断实时禁用
            if (allCode.contains(getDisablePermissionCode(permissionCode))) {
                throw new PermissionDisabledException();
            }
            return true;
        }
        // 判断该权限是否有被禁用
        if (codeRegistry.has(userId, getDisablePermissionCode(permissionCode))) {
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
        if (allCode.contains(permissionCode)) {
            allCode.remove(permissionCode);
            allCode.add(getDisablePermissionCode(permissionCode));
        }
    }

    /**
     * 启用指定权限code的权限
     * @param permissionCode
     */
    public void enablePermission(String permissionCode) {
        String disableCode = getDisablePermissionCode(permissionCode);
        if (allCode.contains(disableCode)) {
            allCode.remove(disableCode);
            allCode.add(permissionCode);
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
    private String getDisablePermissionCode(String code) {
        return code + CODE_STATUS_SEPARATOR + StatusEnum.DISABLE.getValue();
    }
}
