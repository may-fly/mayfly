package mayfly.sys.redis.connection;

import io.lettuce.core.AbstractRedisClient;
import io.lettuce.core.RedisClient;
import io.lettuce.core.RedisConnectionException;
import io.lettuce.core.RedisURI;
import io.lettuce.core.api.StatefulConnection;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.api.sync.RedisCommands;
import io.lettuce.core.cluster.RedisClusterClient;
import io.lettuce.core.cluster.api.StatefulRedisClusterConnection;
import io.lettuce.core.cluster.api.sync.RedisClusterCommands;
import io.lettuce.core.codec.RedisCodec;
import mayfly.common.exception.BusinessRuntimeException;
import mayfly.common.utils.Assert;
import mayfly.common.utils.ScheduleUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.ByteBuffer;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * @author meilin.huang
 * @version 1.0
 * @date 2019-01-16 5:26 PM
 */
public class RedisConnectionRegistry {

    private static final Logger LOG = LoggerFactory.getLogger(RedisConnectionRegistry.class);

    /**
     * 连接缓存  key: 单机：standalone_{id}  集群：cluster_{clusterId}
     */
    private Map<String, RedisConnection> connectionCache = new ConcurrentHashMap<>(8);

    /**
     * 所有连接过的redis信息
     */
    private Set<RedisInfo> allRedis = new TreeSet<>();

    private static final RedisConnectionRegistry instance = new RedisConnectionRegistry();

    public static RedisConnectionRegistry getInstance() {
        return instance;
    }

    private RedisConnectionRegistry(){}

    public synchronized void registerStandalone(RedisInfo redisInfo) {
        Assert.notNull(redisInfo, "单机节点uri不能为空！");
        Assert.notNull(redisInfo.getUri(), "单机节点uri不能为空！");

        if (connectionCache.containsKey(getStandaloneKey(redisInfo.getId()))) {
            return;
        }
        RedisConnection rc = RedisConnection.connectStandalone(redisInfo);
        connectionCache.put(getStandaloneKey(redisInfo.getId()), rc);
        allRedis.add(rc.getRedisInfo());
    }

    public synchronized void registerCluster(Set<RedisInfo> redisCluster) {
        Assert.notEmpty(redisCluster, "集群节点uri不能为空！");

        int clusterId = redisCluster.stream().findFirst().get().getClusterId();
        Assert.assertState(clusterId != RedisInfo.STANDALONE, "集群id不存在！");

        if (connectionCache.containsKey(getClusterKey(clusterId))) {
            return;
        }
        RedisConnection rc = RedisConnection.connectCluster(redisCluster);
        connectionCache.put(getClusterKey(clusterId), rc);
        allRedis.addAll(rc.getRedisInfos());
    }

    /**
     * 移除单机
     * @param redisInfo
     */
    public void remove(RedisInfo redisInfo) {
        String key = getStandaloneKey(redisInfo.getId());
        connectionCache.get(key).close();
        connectionCache.remove(key);
        allRedis.remove(redisInfo);
        ScheduleUtils.cancel(key);
    }

    /**
     * 移除集群
     * @param clusterId
     */
    public void remove(int clusterId) {
        String key = getClusterKey(clusterId);
        connectionCache.get(key).close();
        allRedis.removeAll(connectionCache.get(key).getRedisInfos());
        connectionCache.remove(key);
        ScheduleUtils.cancel(key);
    }

    public boolean contains(boolean cluster, int id) {
        return cluster ? connectionCache.containsKey(getClusterKey(id)) : connectionCache.containsKey(getStandaloneKey(id));
    }

    public RedisClient getClient(int id) {
        return (RedisClient) connectionCache.get(id).getRequireRedisClient();
    }

    public RedisClusterClient getClusterClient(int clusterId) {
        return (RedisClusterClient) connectionCache.get(getClusterKey(clusterId)).getRequireRedisClient();
    }

    /**
     * 获取单台机器的连接
     * @param redisId  redis id
     * @return
     */
    public StatefulRedisConnection<String, byte[]> getConnection(int redisId) {
        RedisInfo ri = getRedisInfo(redisId);
        Assert.notNull(ri, "不存在该redis实例连接");
        //如果是单机则返回单机连接
        if (ri.isStandalone()) {
            return (StatefulRedisConnection<String, byte[]>) connectionCache.get(getStandaloneKey(redisId)).getRequireConnection();
        }
        //如果是集群，则从集群中获取具体节点连接
        return getClusterConnection(ri.getClusterId()).getConnection(ri.getHost(), ri.getPort());
    }

    /**
     * 获取集群连接
     * @param clusterId 集群id
     * @return
     */
    public StatefulRedisClusterConnection<String, byte[]> getClusterConnection(int clusterId) {
        return Optional.ofNullable(connectionCache.get(getClusterKey(clusterId)))
                .map(x -> (StatefulRedisClusterConnection<String, byte[]>)x.getRequireConnection())
                .orElseThrow(() -> new BusinessRuntimeException("不存在该集群连接实例！"));
    }


    /**
     * 获取redis信息
     * @param redisId
     * @return
     */
    public RedisInfo getRedisInfo(int redisId) {
        for (RedisInfo r : allRedis) {
            if (r.getId() == redisId) {
                return r;
            }
        }
        return null;
    }

    /**
     * 获取指定机器的命令操作对象
     * @param redisId
     * @return
     */
    public RedisCommands<String, byte[]> getCmds(int redisId) {
        return getConnection(redisId).sync();
    }

    /**
     * 获取集群命令操作对象
     * @param clusterId
     * @return
     */
    public RedisClusterCommands<String, byte[]> getClusterCmds(int clusterId) {
        return getClusterConnection(clusterId).sync();
    }

    /**
     * 获取集群缓存key
     * @param clusterId  集群id
     * @return
     */
    public static String getClusterKey(int clusterId) {
        return "cluster_" + clusterId;
    }

    /**
     * 获取单机缓存key
     * @param redisId
     * @return
     */
    public static String getStandaloneKey(int redisId) {
        return "redis_" + redisId;
    }


    /**
     * 连接对象
     */
    private static class RedisConnection {
        private static ByteCodec byteCodec = new ByteCodec();

        /**
         * 单机redis基本信息
         */
        private RedisInfo redisInfo;

        /**
         * 是否为集群模式
         */
        private int clusterId = RedisInfo.STANDALONE;

        private Set<RedisInfo> redisInfos;

        /**
         * redis客户端
         */
        private AbstractRedisClient redisClient;

        /**
         * redis连接
         */
        private StatefulConnection<String, byte[]> connection;


        private RedisConnection(RedisInfo redisInfo) {
            this.redisInfo = redisInfo;
        }

        private RedisConnection(Set<RedisInfo> redisCluster) {
            this.clusterId = redisCluster.stream().findFirst().get().getClusterId();
            this.redisInfos = redisCluster;
        }

        /**
         * 连接单机redis
         * @param redisInfo
         * @return
         */
        public static RedisConnection connectStandalone(RedisInfo redisInfo) {
            return new RedisConnection(redisInfo).connect();
        }

        /**
         * 连接集群redis
         * @param redisCluster
         * @return
         */
        public static RedisConnection connectCluster(Set<RedisInfo> redisCluster) {
            return new RedisConnection(redisCluster).connect();
        }

        public synchronized RedisConnection connect() {
            LOG.debug("连接redis----> cluster:{}, id:{}", isCluster(), isCluster() ? clusterId : redisInfo.getId());
            if (!isCluster()) {
                redisClient = RedisClient.create(redisInfo.getUri());
                try {
                    connection = ((RedisClient) redisClient).connect(byteCodec);
                } catch (RedisConnectionException e) {
                    throw new BusinessRuntimeException("无法连接redis实例！");
                }
            } else {
                Set<RedisURI> uris = redisInfos.stream().map(x -> x.getUri()).collect(Collectors.toSet());
                redisClient = RedisClusterClient.create(uris);
                try {
                    connection = ((RedisClusterClient) redisClient).connect(byteCodec);
                } catch (RedisConnectionException e) {
                    throw new BusinessRuntimeException("无法连接redis实例！");
                } catch (Exception ce) {
                    LOG.error("连接redis失败！");
                    throw new BusinessRuntimeException("连接redis实例失败！");
                }
            }

            ScheduleUtils.schedule(getKey(), () -> {
                close();
            }, 10, TimeUnit.MINUTES);
            return this;
        }

        /**
         * 关闭连接
         */
        public void close() {
            LOG.debug("断开redis----> cluster:{}, id: {}", isCluster(), isCluster() ? clusterId : redisInfo.getId());
            if (connection != null) {
                connection.close();
                connection = null;
            }
            if (redisClient != null) {
                redisClient.shutdown();
                redisClient = null;
            }
        }

        /**
         * 获取客户端，不存在重连
         * @return
         */
        public AbstractRedisClient getRequireRedisClient() {
            Assert.assertState(redisInfo != null || redisInfos != null, "请先连接redis!");
            return Optional.ofNullable(redisClient).orElseGet(() -> connect().redisClient);
        }

        /**
         * 获取redis连接，不存在则重连
         * @return
         */
        public StatefulConnection<String, byte[]> getRequireConnection() {
            Assert.assertState(redisInfo != null || redisInfos != null, "请先连接redis!");
            if (connection == null) {
                connect();
            }
            return connection;
        }

        public RedisInfo getRedisInfo() {
            return redisInfo;
        }

        public Set<RedisInfo> getRedisInfos() {
            return redisInfos;
        }

        public boolean isCluster() {
            return clusterId != RedisInfo.STANDALONE;
        }

        public String getKey() {
            return isCluster() ? getClusterKey(clusterId) : getStandaloneKey(redisInfo.getId());
        }
    }

    public static class ByteCodec implements RedisCodec<String, byte[]> {
        private static final byte[] EMPTY = new byte[0];

        @Override
        public String decodeKey(ByteBuffer byteBuffer) {
            return new String(getBytes(byteBuffer));
        }

        @Override
        public byte[] decodeValue(ByteBuffer byteBuffer) {
            return getBytes(byteBuffer);
        }

        @Override
        public ByteBuffer encodeKey(String s) {
            return ByteBuffer.wrap(s.getBytes());
        }

        @Override
        public ByteBuffer encodeValue(byte[] bytes) {
            return ByteBuffer.wrap(bytes);
        }


        private static byte[] getBytes(ByteBuffer buffer) {
            int remaining = buffer.remaining();

            if (remaining == 0) {
                return EMPTY;
            }

            byte[] b = new byte[remaining];
            buffer.get(b);
            return b;
        }
    }
}
