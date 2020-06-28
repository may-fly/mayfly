package mayfly.sys.module.redis.service.impl;

import io.lettuce.core.api.sync.RedisCommands;
import io.lettuce.core.cluster.api.sync.RedisClusterCommands;
import mayfly.core.base.service.impl.BaseServiceImpl;
import mayfly.core.exception.BizAssert;
import mayfly.core.util.Assert;
import mayfly.core.util.bean.BeanUtils;
import mayfly.sys.module.redis.connection.RedisConnectionRegistry;
import mayfly.sys.module.redis.connection.RedisInfo;
import mayfly.sys.module.redis.controller.form.RedisForm;
import mayfly.sys.module.redis.entity.RedisDO;
import mayfly.sys.module.redis.mapper.RedisMapper;
import mayfly.sys.module.redis.service.RedisService;
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
public class RedisServiceImpl extends BaseServiceImpl<RedisMapper, Long, RedisDO> implements RedisService {

    @Autowired
    private RedisMapper redisMapper;


    private RedisConnectionRegistry registry = RedisConnectionRegistry.getInstance();

    @Override
    public RedisCommands<String, byte[]> getCmds(long redisId) {
        //如果不存在该redis信息，则先连接对应的单机or集群连接
        if (registry.getRedisInfo(redisId) == null) {
            RedisDO redis = getById(redisId);
            BizAssert.notNull(redis, "redis实例不存在！");
            if (redis.getClusterId() == RedisInfo.STANDALONE) {
                registry.registerStandalone(toRedisInfo(redis));
            } else {
                registry.registerCluster(toRedisCluster(redis.getClusterId()));
            }
        }
        return registry.getCmds(redisId);
    }

    @Override
    public RedisClusterCommands<String, byte[]> getClusterCmds(long clusterId) {
        //如果不存在该集群连接，则先连接
        if (!registry.contains(true, clusterId)) {
            registry.registerCluster(toRedisCluster(clusterId));
        }
        return registry.getClusterCmds(clusterId);
    }

    @Override
    public void saveNode(RedisForm redisForm) {
        RedisDO redis = BeanUtils.copyProperties(redisForm, RedisDO.class);
        // 测试连接
        RedisConnectionRegistry.RedisConnection redisConnection = RedisConnectionRegistry.RedisConnection.connectStandalone(toRedisInfo(redis));
        redisConnection.close();
        if (redisForm.getId() == null) {
            insert(redis);
        } else {
            updateByIdSelective(redis);
        }
    }

    @Override
    public void delete(long redisId) {
        RedisDO redis = getById(redisId);
        BizAssert.notNull(redis, "节点不存在");

        if (isStandalone(redis.getClusterId())) {
            registry.remove(redisId, false);
            deleteById(redisId);
        }
    }


    private RedisInfo toRedisInfo(RedisDO redis) {
        Assert.notNull(redis, "不存在该redis实例！");
        BizAssert.isTrue(isStandalone(redis.getClusterId()), "该redis为集群模式！");
        return RedisInfo.builder(redis.getId()).info(redis.getHost(), redis.getPort(), redis.getPwd()).build();
    }

    private Set<RedisInfo> toRedisCluster(long clusterId) {
        List<RedisDO> nodes = listByCondition(RedisDO.builder().clusterId(clusterId).build());
        BizAssert.notEmpty(nodes, "不存在该redis集群实例！");
        return nodes.stream().map(n -> RedisInfo.builder(n.getId()).clusterId(clusterId).info(n.getHost(), n.getPort(), n.getPwd()).build())
                .collect(Collectors.toSet());
    }

    private boolean isStandalone(Long clusterId) {
        return clusterId == null || clusterId == RedisInfo.STANDALONE;
    }
}
