package mayfly.common.permission.registry;

import mayfly.common.enums.BoolEnum;

import java.util.Collection;
import java.util.concurrent.TimeUnit;

/**
 * @author meilin.huang
 * @version 1.0
 * @date 2018-11-26 11:02 AM
 */
public final class PermissionCacheHandler {

    /**
     * 权限码与状态分割符号
     */
    public static final String CODE_STATUS_SEPARATOR = ":";

    /**
     * 用户权限码注册
     */
    private UserPermissionCodeRegistry userCodeRegistry;

    /**
     * 系统所有权限码注册器(主要用于实时禁用以及删除权限)
     */
    private SysPermissionCodeRegistry sysCodeRegistry;

    /**
     * 判断是否保存了系统所有权限信息
     */
    private boolean saveSysCode = false;


    private PermissionCacheHandler(UserPermissionCodeRegistry userCodeRegistry, SysPermissionCodeRegistry sysCodeRegistry){
        this.userCodeRegistry = userCodeRegistry;
        this.sysCodeRegistry = sysCodeRegistry;
    }

    /**
     * 权限缓存工厂方法
     * @param userCodeRegistry 用户权限缓存器(null则使用默认注册器 {@link DefaultUserPermissionCodeRegistry})
     * @param sysCodeRegistry  系统所有权限缓存器，可为空
     * @return
     */
    public static PermissionCacheHandler of(UserPermissionCodeRegistry userCodeRegistry, SysPermissionCodeRegistry sysCodeRegistry) {
        if (userCodeRegistry == null) {
            userCodeRegistry = DefaultUserPermissionCodeRegistry.getInstance();
        }
        return new PermissionCacheHandler(userCodeRegistry, sysCodeRegistry);
    }

    /**
     * 保存用户权限列表
     * @param userId  用户id
     * @param permissionCodes  权限列表
     * @param time  时间
     * @param timeUnit  时间单位
     */
    public void savePermission(Integer userId, Collection<String> permissionCodes, long time, TimeUnit timeUnit) {
        // 如果首次没有保存系统所有权限，则保存系统所有权限
        if (!saveSysCode && sysCodeRegistry != null) {
            sysCodeRegistry.save();
            saveSysCode = true;
        }
        userCodeRegistry.save(userId, permissionCodes, time, timeUnit);
    }

    /**
     * 若有修改权限code以及状态，重新加载系统所有权限code，简单粗暴
     */
    public void reloadSysPermission() {
        if (sysCodeRegistry != null) {
            sysCodeRegistry.reload();
        }
    }

    /**
     * 保存新增的系统权限code，新增的概率较大，故不使用reload方式
     * @param code  权限code
     */
    public void addSysPermission(String code) {
        if (sysCodeRegistry != null) {
            sysCodeRegistry.add(code);
        }
    }

    /**
     * 获取权限code对应的禁用code,即code + ":" + 0
     * @param code
     * @return
     */
    public static String getDisablePermissionCode(String code) {
        return code + CODE_STATUS_SEPARATOR + BoolEnum.FALSE.getValue();
    }
}
