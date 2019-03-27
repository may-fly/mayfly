package mayfly.common.permission;

/**
 * @author meilin.huang
 * @version 1.0
 * @date 2018-12-14 3:09 PM
 */
public class PermissionInfo {

    private String permissionCode;

    public PermissionInfo(){}

    public PermissionInfo(String permissionCode) {
        this.permissionCode = permissionCode;
    }

    public String getPermissionCode() {
        return permissionCode;
    }

    public void setPermissionCode(String permissionCode) {
        this.permissionCode = permissionCode;
    }
}
