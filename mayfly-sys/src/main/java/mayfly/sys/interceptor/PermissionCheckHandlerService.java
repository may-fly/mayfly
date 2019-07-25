package mayfly.sys.interceptor;

import mayfly.common.permission.checker.PermissionCheckHandler;
import mayfly.common.util.BracePlaceholder;
import mayfly.sys.common.cache.UserCacheKey;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

/**
 * @author meilin.huang
 * @version 1.0
 * @date 2019-03-28 2:43 PM
 */
@Component
public class PermissionCheckHandlerService {

    @Autowired
    private RedisTemplate redisTemplate;

    public Integer getIdByToken(String token) {
        return (Integer)redisTemplate.opsForValue().get(BracePlaceholder.resolveByObject(UserCacheKey.USER_ID_KEY, token));
    }

    public PermissionCheckHandler getCheckHandler() {
        return PermissionCheckHandler.of(
                (id, code)-> redisTemplate.opsForSet().isMember(BracePlaceholder.resolveByObject(UserCacheKey.USER_PERMISSION_KEY, id), code),
                sysCode -> redisTemplate.boundSetOps(UserCacheKey.ALL_PERMISSION_KEY).isMember(sysCode));
    }
}
