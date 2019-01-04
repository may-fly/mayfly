package mayfly.common.redis;

import io.lettuce.core.AbstractRedisClient;
import io.lettuce.core.api.StatefulConnection;
import org.apache.commons.pool2.impl.GenericObjectPool;

/**
 * @author meilin.huang
 * @version 1.0
 * @date 2018-12-19 5:28 PM
 */
public class RedisConnectionPool {
    private static AbstractRedisClient redisClient;

    private GenericObjectPool<StatefulConnection<String, byte[]>> pool;

    public StatefulConnection<String, byte[]> getConnection() {
        return null;
    }
}
