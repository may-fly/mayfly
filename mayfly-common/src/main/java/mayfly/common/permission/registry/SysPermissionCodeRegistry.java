package mayfly.common.permission.registry;

/**
 * 系统所有权限码注册器
 * @author meilin.huang
 * @version 1.0
 * @date 2019-03-28 10:47 AM
 */
public interface SysPermissionCodeRegistry {
    /**
     * 保存系统所有权限（用于实时禁用删除权限等）
     * @return
     */
    void save();

    /**
     * 判断在所有权限中是否存在该权限code (用于实时判断权限是否禁用删除等）
     * @param permissionCode
     * @return
     */
    boolean has(String permissionCode);

    /**
     * 重命名 (用于实时重命名系统权限中的状态，以便用于判断权限是否可用）
     * @param oldCode
     * @param newCode
     */
    void rename(String oldCode, String newCode);
}
