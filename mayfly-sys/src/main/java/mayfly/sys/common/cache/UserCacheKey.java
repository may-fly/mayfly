package mayfly.sys.common.cache;

/**
 * @author meilin.huang
 * @version 1.0
 * @date 2018-11-24 4:02 PM
 */
public class UserCacheKey {

    public static final Integer EXPIRE_TIME = 30;

    public static final String ALL_PERMISSION_KEY = "permissions";

    /**
     * 用户id
     */
    public static final String USER_ID_KEY = "user:token:{token}:id";

    /**
     *  用户权限
     */
    public static final String USER_PERMISSION_KEY = "user:id:{id}:permissions";
}
