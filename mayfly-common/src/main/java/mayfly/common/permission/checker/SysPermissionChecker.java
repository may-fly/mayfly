package mayfly.common.permission.checker;

/**
 * @author meilin.huang
 * @version 1.0
 * @date 2019-03-28 1:46 PM
 */
@FunctionalInterface
public interface SysPermissionChecker {

    /**
     * 判断系统所有权限中是否含有该权限 (用于实时判断权限是否禁用删除等)
     * @param permissionCode 权限code
     * @return
     */
    boolean has(String permissionCode);
}
