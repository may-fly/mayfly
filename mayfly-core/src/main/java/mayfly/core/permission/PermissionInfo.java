package mayfly.core.permission;

/**
 * @author meilin.huang
 * @version 1.0
 * @date 2018-12-14 3:09 PM
 */
public class PermissionInfo {

    private boolean requireToken;

    private boolean requireCode;

    private String permissionCode;

    public PermissionInfo() {
    }

    public PermissionInfo(String permissionCode) {
        this.permissionCode = permissionCode;
    }

    public PermissionInfo(boolean requireToken, boolean requireCode, String permissionCode) {
        this.requireCode = requireCode;
        this.requireToken = requireToken;
        this.permissionCode = permissionCode;
    }

    public String getPermissionCode() {
        return permissionCode;
    }

    public boolean isRequireCode() {
        return requireCode;
    }

    public void setRequireCode(boolean requireCode) {
        this.requireCode = requireCode;
    }

    public boolean isRequireToken() {
        return requireToken;
    }

    public void setRequireToken(boolean requireToken) {
        this.requireToken = requireToken;
    }
}
