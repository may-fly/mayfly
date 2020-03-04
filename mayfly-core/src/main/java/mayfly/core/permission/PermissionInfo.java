package mayfly.core.permission;

import mayfly.core.util.StringUtils;
import mayfly.core.util.annotation.AnnotationUtils;

import java.lang.reflect.Method;

/**
 * @author meilin.huang
 * @version 1.0
 * @date 2018-12-14 3:09 PM
 */
public class PermissionInfo {

    private boolean requireCode;

    private String permissionCode;

    public PermissionInfo() {
    }

    public PermissionInfo(String permissionCode) {
        this.permissionCode = permissionCode;
    }

    /**
     * 根据方法获取对应的权限信息,如果方法声明类上有@{@linkplain Permission}注解则为类名权限code + 方法权限code(方法权限code不存在，则为方法名)<br/>
     * 如果声明类没有@{@linkplain Permission}注解则只返回方法上权限code
     *
     * @param method 方法
     * @return 权限信息
     */
    public static PermissionInfo parse(Method method) {
        Permission permission = AnnotationUtils.getAnnotation(method.getDeclaringClass(), Permission.class);
        if (permission == null) {
            permission = AnnotationUtils.getAnnotation(method, Permission.class);
            if (permission == null) {
                return null;
            }

            return new PermissionInfo(permission.code()).setRequireCode(permission.requireCode());
        }

        String classCode = permission.code();
        Permission methodPermissionAnno = AnnotationUtils.getAnnotation(method, Permission.class);

        if (methodPermissionAnno != null) {
            String methodCode = methodPermissionAnno.code();
            return new PermissionInfo(classCode + (StringUtils.isEmpty(methodCode)
                    ? method.getName() : methodCode)).setRequireCode(methodPermissionAnno.requireCode());
        } else {
            return new PermissionInfo(classCode + method.getName())
                    .setRequireCode(permission.requireCode());
        }
    }


    // -----   getter setter  ----- //

    public String getPermissionCode() {
        return permissionCode;
    }

    public boolean isRequireCode() {
        return requireCode;
    }

    public PermissionInfo setRequireCode(boolean requireCode) {
        this.requireCode = requireCode;
        return this;
    }
}
