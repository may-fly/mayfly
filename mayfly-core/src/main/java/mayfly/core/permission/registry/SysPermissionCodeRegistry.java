package mayfly.core.permission.registry;

import mayfly.core.permission.checker.SysPermissionChecker;

/**
 * 系统所有权限码注册器（用于实时禁用删除权限等）
 * @author meilin.huang
 * @version 1.0
 * @date 2019-03-28 10:47 AM
 */
public interface SysPermissionCodeRegistry extends SysPermissionChecker {
    /**
     * 保存系统所有权限
     * @return
     */
    void save();

    /**
     * 若有修改权限code以及状态，重新加载系统所有权限code，简单粗暴
     */
    void reload();

    /**
     * 新增系统权限code，因为新增的概率较大，故不使用reload方式
     * @param code
     */
    void add(String code);
}
