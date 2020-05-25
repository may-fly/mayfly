package mayfly.core.permission;


import mayfly.core.exception.BusinessException;

/**
 * @author meilin.huang
 * @version 1.0
 * @date 2019-03-23 8:39 PM
 */
public class PermissionDisabledException extends BusinessException {
    private static final long serialVersionUID = -6476165810262470327L;

    public PermissionDisabledException() {
        super("该功能权限暂时被禁用！");
    }
}
