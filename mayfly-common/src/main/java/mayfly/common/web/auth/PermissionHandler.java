package mayfly.common.web.auth;

import mayfly.common.web.RequestUri;
import mayfly.common.web.UriMatchHandler;
import mayfly.common.web.UriPattern;

import java.lang.reflect.Method;
import java.util.*;

/**
 * @author meilin.huang
 * @version 1.0
 * @date 2018-11-26 11:02 AM
 */
public class PermissionHandler {

    private UriMatchHandler uriMatchHandler = UriMatchHandler.getInstance();

    private static PermissionHandler permissionHandler = new PermissionHandler();

    private PermissionHandler(){}

    public static final PermissionHandler getInstance() {
        return permissionHandler;
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
     * 返回与requestUri匹配的uriPattern
     * @param requestUri
     * @param uriPatterns
     * @return
     */
    public UriPattern matchAndReturnPattern(RequestUri requestUri, List<UriPattern>uriPatterns) {
        return this.uriMatchHandler.matchAndReturnPattern(requestUri, uriPatterns);
    }
}
