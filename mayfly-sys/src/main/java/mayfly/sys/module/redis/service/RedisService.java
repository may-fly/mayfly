package mayfly.sys.module.redis.service;

import io.lettuce.core.api.sync.RedisCommands;
import io.lettuce.core.cluster.api.sync.RedisClusterCommands;
import mayfly.core.base.service.BaseService;
import mayfly.sys.module.redis.controller.form.RedisForm;
import mayfly.sys.module.redis.entity.RedisDO;

/**
 * @author meilin.huang
 * @version 1.0
 * @date 2019-01-07 4:07 PM
 */
public interface RedisService extends BaseService<Integer, RedisDO> {

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

    /**
     * 新增节点
     *
     * @param redis  redis表单
     */
    void saveNode(RedisForm redis);

    /**
     * 删除redis节点
     *
     * @param redisId     redis id
     */
    void delete(int redisId);
}
