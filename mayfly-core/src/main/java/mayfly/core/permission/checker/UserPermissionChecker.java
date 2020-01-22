package mayfly.core.permission.checker;

/**
 * 用户权限校验检查
 *
 * @author meilin.huang
 * @version 1.0
 * @date 2019-03-28 1:44 PM
 */
public interface UserPermissionChecker<I> {

    /**
     * 判断用户是否拥有该权限
     *
     * @param userId         用户id
     * @param permissionCode 权限code
     * @return
     */
    boolean has(I userId, String permissionCode);

    /**
     * 获取用户id
     * @param token token
     * @return      user id
     */
    I getUserIdByToken(String token);
}
