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
     * 获取单台机器的redis命令操作对象，可以是单机版也可以是集群版
     *
     * @param redisId
     * @return
     */
    RedisCommands<String, byte[]> getCmds(int redisId);

    /**
     * 获取集群连接
     *
     * @param clusterId
     * @return
     */
    RedisClusterCommands<String, byte[]> getClusterCmds(int clusterId);
}
