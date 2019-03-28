package mayfly.common.permission.check;

/**
 * @author meilin.huang
 * @version 1.0
 * @date 2019-03-28 1:44 PM
 */
@FunctionalInterface
public interface UserPermissionCheck {

    /**
     * 判断用户时候拥有该权限
     * @param userId  用户id
     * @param permissionCode
     * @return
     */
    boolean has(Integer userId, String permissionCode);
}
