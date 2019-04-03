package mayfly.sys.service.redis.impl;

import io.lettuce.core.api.sync.RedisCommands;
import io.lettuce.core.cluster.api.sync.RedisClusterCommands;
import mayfly.common.exception.BusinessRuntimeException;
import mayfly.common.utils.Assert;
import mayfly.dao.RedisMapper;
import mayfly.entity.Redis;
import mayfly.sys.redis.connection.RedisConnectionRegistry;
import mayfly.sys.redis.connection.RedisInfo;
import mayfly.sys.service.base.impl.BaseServiceImpl;
import mayfly.sys.service.redis.RedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author meilin.huang
 * @version 1.0
 * @date 2019-01-07 4:08 PM
 */
@Service
public class RedisServiceImpl extends BaseServiceImpl<RedisMapper, Redis> implements RedisService {

    @Autowired
    private RedisMapper redisMapper;

    private RedisConnectionRegistry registry = RedisConnectionRegistry.getInstance();

    @Override
    public RedisCommands<String, byte[]> getCmds(int redisId) {
        //如果不存在该redis信息，则先连接对应的单机or集群连接
        if (registry.getRedisInfo(redisId) == null) {
            Redis redis = getById(redisId);
            if (redis.getClusterId() == RedisInfo.STANDALONE) {
                registry.registerStandalone(toRedisInfo(redis));
            } else {
                registry.registerCluster(toRedisCluster(redis.getClusterId()));
            }
        }
        return registry.getCmds(redisId);
    }

    @Override
    public RedisClusterCommands<String, byte[]> getClusterCmds(int clusterId) {
        //如果不存在该集群连接，则先连接
        if (!registry.contains(true, clusterId)) {
            registry.registerCluster(toRedisCluster(clusterId));
        }
        return registry.getClusterCmds(clusterId);
    }

    private RedisInfo toRedisInfo(Redis redis) {
        Assert.notNull(redis, "不存在该redis实例！");
        if (redis.getClusterId() != null && redis.getClusterId() != RedisInfo.STANDALONE) {
            throw new BusinessRuntimeException("该redis为集群模式！");
        }
        return RedisInfo.builder(redis.getId()).info(redis.getHost(), redis.getPort(), redis.getPwd()).build();
    }

    private Set<RedisInfo> toRedisCluster(int clusterId) {
        List<Redis> nodes = listByCondition(Redis.builder().clusterId(clusterId).build());
        Assert.notEmpty(nodes, "不存在该redis集群实例！");
        return nodes.stream().map(n -> RedisInfo.builder(n.getId()).clusterId(clusterId).info(n.getHost(), n.getPort(), n.getPwd()).build())
                .collect(Collectors.toSet());
    }
}
