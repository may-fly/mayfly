package mayfly.common.permission;

/**
 * @author meilin.huang
 * @version 1.0
 * @date 2019-03-23 8:39 PM
 */
public class PermissionDisabledException extends Exception {
    public PermissionDisabledException() {
        super("该功能权限暂时被禁用！");
    }
}
