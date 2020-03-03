package mayfly.sys.common.cache;

/**
 * @author meilin.huang
 * @version 1.0
 * @date 2018-11-24 4:02 PM
 */
public class UserCacheKey {

    public static final Integer EXPIRE_TIME = 30;

    /**
     * 账号token key
     */
    public static final String ACCOUNT_TOKEN_KEY = "account:token:{token}";
}
