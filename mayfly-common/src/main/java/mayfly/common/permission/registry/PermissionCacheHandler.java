package mayfly.common.permission.registry;

import mayfly.common.enums.StatusEnum;
import mayfly.common.web.RequestUri;
import mayfly.common.web.UriMatchHandler;
import mayfly.common.web.UriPattern;

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

    private static PermissionCacheHandler permissionCacheHandler = new PermissionCacheHandler(DefaultUserPermissionCodeRegistry.getInstance(), null);

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

    /**
     * uri匹配处理器
     */
    private UriMatchHandler uriMatchHandler = UriMatchHandler.getInstance();

    private PermissionCacheHandler(UserPermissionCodeRegistry userCodeRegistry, SysPermissionCodeRegistry sysCodeRegistry){
        this.userCodeRegistry = userCodeRegistry;
        this.sysCodeRegistry = sysCodeRegistry;
    }

    public static final PermissionCacheHandler getInstance() {
        return permissionCacheHandler;
    }

    public PermissionCacheHandler withUserCodeRegistry(UserPermissionCodeRegistry registry) {
        this.userCodeRegistry = registry;
        return this;
    }

    public PermissionCacheHandler withSysCodeRegistry(SysPermissionCodeRegistry sysCodeRegistry) {
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
