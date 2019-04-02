package mayfly.sys.service.redis.impl;

import io.lettuce.core.api.sync.RedisCommands;
import io.lettuce.core.cluster.api.sync.RedisClusterCommands;
import mayfly.common.exception.BusinessRuntimeException;
import mayfly.common.utils.Assert;
import mayfly.dao.RedisMapper;
import mayfly.entity.Redis;
import mayfly.sys.redis.connection.RedisConnectionRegistry;
import mayfly.sys.redis.connection.RedisInfo;
import mayfly.sys.service.redis.RedisService;
import mayfly.sys.service.base.impl.BaseServiceImpl;
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

    private RedisConnectionRegistry register = RedisConnectionRegistry.getInstance();

    @Override
    public void connect(boolean cluster, int id) {
        if (register.contains(cluster, id)) {
            return;
        }
        if (cluster) {
            register.registerCluster(toRedisCluster(id));
        } else {
            register.registerStandalone(toRedisInfo(id));
        }
    }

    @Override
    public RedisCommands<String, byte[]> getCmds(int redisId) {
        return register.getCmds(redisId);
    }

    @Override
    public RedisClusterCommands<String, byte[]> getClusterCmds(int clusterId) {
        return register.getClusterCmds(clusterId);
    }

    private RedisInfo toRedisInfo(int id) {
        Redis redis = getById(id);
        Assert.notNull(redis, "不存在该redis实例！");
        if (redis.getClusterId() != null && redis.getClusterId() != RedisInfo.STANDALONE) {
            throw new BusinessRuntimeException("该redis为集群模式！");
        }
        return RedisInfo.builder(redis.getId()).info(redis.getHost(), redis.getPort(), redis.getPwd()).build();
    }

    private Set<RedisInfo> toRedisCluster(int id) {
        List<Redis> nodes = listByCondition(Redis.builder().clusterId(id).build());
        Assert.notEmpty(nodes, "不存在该redis集群实例！");
        return nodes.stream().map(n -> RedisInfo.builder(n.getId()).clusterId(id).info(n.getHost(), n.getPort(), n.getPwd()).build())
                .collect(Collectors.toSet());
    }
}
