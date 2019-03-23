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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.ByteBuffer;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * @author meilin.huang
 * @version 1.0
 * @date 2019-01-16 5:26 PM
 */
public class RedisConnectionRegister {

    private static final Logger LOG = LoggerFactory.getLogger(RedisConnectionRegister.class);

    private Map<Integer, RedisConnection> standaloneConMap = new ConcurrentHashMap<>();

    private Map<Integer, RedisConnection> clusterConMap = new ConcurrentHashMap<>();

    /**
     * 所有连接过的redis信息
     */
    private Set<RedisInfo> allRedis = new TreeSet<>();

    private static final RedisConnectionRegister instance = new RedisConnectionRegister();

    public static RedisConnectionRegister getInstance() {
        return instance;
    }

    private RedisConnectionRegister(){}

    public synchronized void register(RedisInfo redisInfo) {
        Assert.notNull(redisInfo, "单机节点uri不能为空！");
        Assert.notNull(redisInfo.getUri(), "单机节点uri不能为空！");

        if (standaloneConMap.containsKey(redisInfo.getId())) {
            return;
        }
        RedisConnection rc = RedisConnection.connectStandalone(redisInfo);
        standaloneConMap.put(redisInfo.getId(), rc);
        allRedis.add(rc.getRedisInfo());
    }

    public synchronized void register(Set<RedisInfo> redisCluster) {
        Assert.notEmpty(redisCluster, "集群节点uri不能为空！");

        int clusterId = redisCluster.iterator().next().getClusterId();
        Assert.assertState(clusterId != RedisInfo.STANDALONE, "集群id不存在！");

        if (clusterConMap.containsKey(clusterId)) {
            return;
        }
        RedisConnection rc = RedisConnection.connectCluster(redisCluster);
        clusterConMap.put(clusterId, rc);
        allRedis.addAll(rc.getRedisInfos());
    }

    public void remove(RedisInfo redisInfo) {
        int id = redisInfo.getId();
        standaloneConMap.get(id).close();
        standaloneConMap.remove(id);
        allRedis.remove(redisInfo);
    }

    public void remove(int clusterId) {
        clusterConMap.get(clusterId).close();
        allRedis.removeAll(getClusterRedisInfo(clusterId));
        clusterConMap.remove(clusterId);
    }

    public boolean contains(boolean cluster, int id) {
        return cluster ? clusterConMap.containsKey(id) : standaloneConMap.containsKey(id);
    }

    public RedisClient getClient(int id) {
        return (RedisClient) standaloneConMap.get(id).getRequireRedisClient();
    }

    public RedisClusterClient getClusterClient(int clusterId) {
        return (RedisClusterClient) clusterConMap.get(clusterId).getRequireRedisClient();
    }

    /**
     * 获取单台机器的连接
     * @param redisId  redis id
     * @return
     */
    public StatefulRedisConnection<String, byte[]> getConnection(int redisId) {
        RedisInfo ri = getRedisInfo(redisId);
        Assert.notNull(ri, "不存在该redis实例连接");

        if (ri.getClusterId() == RedisInfo.STANDALONE) {
            return (StatefulRedisConnection<String, byte[]>) standaloneConMap.get(redisId).getRequireConnection();
        }
        return getClusterConnection(ri.getClusterId()).getConnection(ri.getHost(), ri.getPort());
    }

    /**
     * 获取集群连接
     * @param clusterId 集群id
     * @return
     */
    public StatefulRedisClusterConnection<String, byte[]> getClusterConnection(int clusterId) {
        return Optional.ofNullable(clusterConMap.get(clusterId))
                .map(x -> (StatefulRedisClusterConnection<String, byte[]>)x.getRequireConnection())
                .orElseThrow(() -> new BusinessRuntimeException("不存在该集群连接实例！"));
    }


    /**
     * 获取redis信息
     * @param redisId
     * @return
     */
    public RedisInfo getRedisInfo(int redisId) {
        Iterator<RedisInfo> it = allRedis.iterator();
        while (it.hasNext()) {
            RedisInfo ri = it.next();
            if (ri.getId() == redisId) {
                return ri;
            }
        }

        return null;
    }

    public Set<RedisInfo> getClusterRedisInfo(int clusterId) {
        return clusterConMap.get(clusterId).getRedisInfos();
    }

    public Set<RedisInfo> getAllRedis() {
        return allRedis;
    }

    /**
     * 获取指定机器的命令操作
     * @param redisId
     * @return
     */
    public RedisCommands<String, byte[]> getCmds(int redisId) {
        return getConnection(redisId).sync();
    }

    public RedisClusterCommands<String, byte[]> getClusterCmds(int clusterId) {
        return getClusterConnection(clusterId).sync();
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
        private boolean cluster;

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
            this.cluster = false;
            this.redisInfo = redisInfo;
        }

        private RedisConnection(Set<RedisInfo> redisCluster) {
            this.cluster = true;
            this.redisInfos = redisCluster;
        }

        /**
         * 连接单机redis
         * @param redisInfo
         * @return
         */
        public static RedisConnection connectStandalone(RedisInfo redisInfo) {
            LOG.debug("连接redis----> id: {}", redisInfo.getId());
            return new RedisConnection(redisInfo).connect();
        }

        /**
         * 连接集群redis
         * @param redisCluster
         * @return
         */
        public static RedisConnection connectCluster(Set<RedisInfo> redisCluster) {
            LOG.debug("连接redis集群----> id: {}", redisCluster.iterator().next().getClusterId());
            return new RedisConnection(redisCluster).connect();
        }

        public synchronized RedisConnection connect() {
            if (!cluster) {
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
            return this;
        }

        /**
         * 关闭连接
         */
        public void close() {
            LOG.debug("断开redis连接----> cluster:{}, id: {}", this.cluster, cluster ? getRedisInfos().iterator().next().getClusterId() : redisInfo.getId());
            if (redisClient != null) {
                redisClient.shutdown();
                redisClient = null;
            }
            if (connection != null) {
                if (connection.isOpen()) {
                    connection.close();
                }
                connection = null;
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
            return cluster;
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
