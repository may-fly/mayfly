package mayfly.sys.module.redis.service.impl;

import io.lettuce.core.api.sync.RedisCommands;
import io.lettuce.core.cluster.api.sync.RedisClusterCommands;
import mayfly.core.base.service.impl.BaseServiceImpl;
import mayfly.core.exception.BusinessAssert;
import mayfly.core.util.Assert;
import mayfly.core.util.bean.BeanUtils;
import mayfly.sys.module.redis.controller.form.RedisForm;
import mayfly.sys.module.redis.entity.Redis;
import mayfly.sys.module.redis.mapper.RedisMapper;
import mayfly.sys.module.redis.service.RedisService;
import mayfly.sys.module.redis.connection.RedisConnectionRegistry;
import mayfly.sys.module.redis.connection.RedisInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
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

    @Autowired
    @Override
    protected void setBaseMapper() {
        super.baseMapper = redisMapper;
    }

    private RedisConnectionRegistry registry = RedisConnectionRegistry.getInstance();

    @Override
    public RedisCommands<String, byte[]> getCmds(int redisId) {
        //如果不存在该redis信息，则先连接对应的单机or集群连接
        if (registry.getRedisInfo(redisId) == null) {
            Redis redis = getById(redisId);
            BusinessAssert.notNull(redis, "redis实例不存在！");
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

    @Override
    public void saveNode(RedisForm redisForm) {
        Redis redis = BeanUtils.copyProperties(redisForm, Redis.class);
        // 测试连接
        RedisConnectionRegistry.RedisConnection redisConnection = RedisConnectionRegistry.RedisConnection.connectStandalone(toRedisInfo(redis));
        redisConnection.close();
        LocalDateTime now = LocalDateTime.now();
        redis.setCreateTime(now);
        redis.setUpdateTime(now);
        if (redisForm.getId() == null) {
            insert(redis);
        } else {
            updateById(redis);
        }
    }

    @Override
    public void delete(int redisId) {
        Redis redis = getById(redisId);
        BusinessAssert.notNull(redis, "节点不存在");

        if (isStandalone(redis.getClusterId())) {
            registry.remove(redisId, false);
            deleteById(redisId);
        }
    }


    private RedisInfo toRedisInfo(Redis redis) {
        Assert.notNull(redis, "不存在该redis实例！");
        BusinessAssert.state(isStandalone(redis.getClusterId()), "该redis为集群模式！");
        return RedisInfo.builder(redis.getId()).info(redis.getHost(), redis.getPort(), redis.getPwd()).build();
    }

    private Set<RedisInfo> toRedisCluster(int clusterId) {
        List<Redis> nodes = listByCondition(Redis.builder().clusterId(clusterId).build());
        BusinessAssert.notEmpty(nodes, "不存在该redis集群实例！");
        return nodes.stream().map(n -> RedisInfo.builder(n.getId()).clusterId(clusterId).info(n.getHost(), n.getPort(), n.getPwd()).build())
                .collect(Collectors.toSet());
    }

    private boolean isStandalone(Integer clusterId) {
        return clusterId == null || clusterId == RedisInfo.STANDALONE;
    }
}
