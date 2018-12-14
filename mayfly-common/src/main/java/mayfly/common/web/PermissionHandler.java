package mayfly.common.web;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Optional;

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

    public Permission getByMethod(Method method) {
        //如果方法声明类上有该注解，则返回方法声明类上的，否则返回方法上的
        return Optional.ofNullable(method.getDeclaringClass().getAnnotation(Permission.class))
                .orElse(method.getAnnotation(Permission.class));
    }

    /**
     * 判断方法的声明类或者本身是否含有@Permission注解
     * @param method
     * @return
     */
    public boolean hasPermission(Method method) {
        return method.getDeclaringClass().isAnnotationPresent(Permission.class) || method.isAnnotationPresent(Permission.class);
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
