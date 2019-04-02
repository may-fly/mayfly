package mayfly.sys.service.redis;

import io.lettuce.core.api.sync.RedisCommands;
import io.lettuce.core.cluster.api.sync.RedisClusterCommands;
import mayfly.entity.Redis;
import mayfly.sys.service.base.BaseService;

/**
 * @author meilin.huang
 * @version 1.0
 * @date 2019-01-07 4:07 PM
 */
public interface RedisService extends BaseService<Redis> {

    /**
     * 连接redis
     * @param cluster  是否是集群模式
     * @param id       机器id or 集群id
     */
    void connect(boolean cluster, int id);


    RedisCommands<String, byte[]> getCmds(int redisId);

    RedisClusterCommands<String, byte[]> getClusterCmds(int clusterId);
}
