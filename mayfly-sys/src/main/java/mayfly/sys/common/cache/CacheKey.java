package mayfly.sys.common.cache;

/**
 * @author meilin.huang
 * @version 1.0
 * @date 2018-11-24 4:02 PM
 */
public interface CacheKey {
    /**
     * token过期时间
     */
    Integer SESSION_EXPIRE_TIME = 120;

    /**
     * 验证码过期时间
     */
    Integer CAPTCHA_EXPIRE_TIME = 3;


    String CAPTCHA_KEY = "captcha:{uuid}";

    /**
     * 账号token key
     */
    String ACCOUNT_TOKEN_KEY = "account:token:{token}";
}
