package mayfly.sys.redis;

import io.lettuce.core.AbstractRedisClient;
import io.lettuce.core.RedisClient;
import io.lettuce.core.RedisConnectionException;
import io.lettuce.core.RedisURI;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.api.sync.RedisCommands;
import io.lettuce.core.codec.RedisCodec;
import mayfly.common.exception.BusinessRuntimeException;
import mayfly.common.utils.ScheduleUtils;
import mayfly.common.utils.StringUtils;
import mayfly.sys.redis.enumration.RedisInfoEnum;
import mayfly.sys.redis.parser.RedisInfoParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.ByteBuffer;
import java.time.Duration;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

/**
 * @author meilin.huang
 * @version 1.0
 * @date 2018-12-19 5:28 PM
 */
public class RedisHandler {

    private static final Logger LOG = LoggerFactory.getLogger(RedisHandler.class);

    private static Map<Integer, Redis> redisMachineCache = new ConcurrentHashMap<>();

    /**
     * 获取操作redis命令的对象
     * @param id  redis机器id
     * @return
     */
    public static RedisCommands<String, byte[]> getCommands(Integer id) {
        if (id == null) {
            throw new BusinessRuntimeException("key param error");
        }
        return Optional.ofNullable(redisMachineCache.get(id))
                .map(r -> r.cmds())
                .orElseThrow(() -> new BusinessRuntimeException("redis未连接!"));
    }

    /**
     * 连接并创建一个redis对象
     * @param redisId  为空不缓存该redis对象
     * @param host
     * @param port
     * @param password
     */
    public static void connect(Integer redisId, String host, int port, String password) {
        if (redisMachineCache.containsKey(redisId)) {
            return;
        }
        char[] pass = StringUtils.isEmpty(password) ? null : password.toCharArray();
        Redis redis = Redis.connect(redisId, host, port, pass);

        Optional.ofNullable(redisId).ifPresent(id -> {
            redisMachineCache.put(redisId, redis);
        });
    }

    /**
     * 关闭redis服务器的连接
     * @param id
     */
    public static void close(Integer id) {
        Redis cp = redisMachineCache.get(id);
        if (cp == null) {
            throw new BusinessRuntimeException("redis未连接！");
        }
        //关闭redisClient以及关闭连接池
        cp.shutdown();
    }

    /**
     * 移除redis机器
     * @param id redis机器id
     */
    public static void remove(Integer id) {
        close(id);
        redisMachineCache.remove(id);
    }

    public static Map<RedisInfoEnum, Map<String, Object>> getInfo(Integer id) {
        String statResult = redisMachineCache.get(id).cmds().info();
        return RedisInfoParser.parse(statResult);
    }

    /**
     * redis信息
     */
    private static class Redis {
        private static ByteCodec byteCodec = new ByteCodec();

        /**
         * 连接失效时间
         */
        private static long connectionInvalid = 1;

        /**
         * redis机器id
         */
        private Integer id;
        /**
         * redis uri
         */
        private RedisURI redisURI;

        private AbstractRedisClient redisClient;

        private StatefulRedisConnection<String, byte[]> connection;


        public static Redis connect(Integer id, String host, int port, char[] password) {
            LOG.debug("connectStandalone redis: {}:{}", host, port);
            Redis rc = new Redis();
            rc.id = id;
            rc.redisURI = RedisURI.create(host, port);
            if (password != null && password.length > 0) {
                rc.redisURI.setPassword(password);
            }
            rc.redisURI.setTimeout(Duration.ofSeconds(1));
            rc.connect();

            return rc;
        }

        public void connect() {
            this.redisClient = RedisClient.create(redisURI);
            try {
                this.connection = ((RedisClient)this.redisClient).connect(byteCodec);
            } catch (RedisConnectionException e) {
                LOG.error("connectStandalone redis error: {}:{}", redisURI.getHost(), redisURI.getPort());
                throw new BusinessRuntimeException(e.getMessage());
            }

            Optional.ofNullable(id).ifPresent(i -> {
                //指定时间段自动关闭连接
                ScheduleUtils.schedule(String.valueOf(id), () -> {
                    this.shutdown();
                }, connectionInvalid, TimeUnit.MINUTES);
            });
        }

        /**
         * 获取redis操作
         * @return
         */
        public RedisCommands<String, byte[]> cmds() {
            if (this.redisClient == null) {
                LOG.debug("reconnect redis : {}:{}", redisURI.getHost(), redisURI.getPort());
                connect();
            }
            return this.connection.sync();
        }

        /**
         * 关闭该redis客户端以及连接
         */
        public void shutdown() {
            LOG.debug("close redis: {}:{}", redisURI.getHost(), redisURI.getPort());
            connection.close();
            connection = null;
            redisClient.shutdown();
            redisClient = null;

            ScheduleUtils.removeFuture(String.valueOf(id));
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
