package mayfly.sys.module.redis.connection;

import io.lettuce.core.RedisURI;
import lombok.Data;
import mayfly.core.util.StringUtils;

/**
 * @author meilin.huang
 * @version 1.0
 * @date 2019-01-16 4:16 PM
 */
@Data
public class RedisInfo implements Comparable<RedisInfo> {

    /**
     * 默认为单机
     */
    public static final long STANDALONE = 0L;

    /**
     * redis id
     */
    private Long id;

    /**
     * 集群节点id
     */
    private long clusterId = STANDALONE;

    /**
     * redis uri
     */
    private RedisURI uri;


    /**
     * 是否为单机模式
     *
     * @return
     */
    public static boolean isStandlone(Long clusterId) {
        return clusterId == STANDALONE;
    }

    public static Builder builder(Long id) {
        return new Builder(id == null ?  0 : id);
    }

    public static class Builder {
        private RedisInfo redisInfo;

        public Builder(long id) {
            redisInfo = new RedisInfo();
            redisInfo.id = id;
        }

        public Builder clusterId(long clusterId) {
            redisInfo.clusterId = clusterId;
            return this;
        }

        public Builder info(String host, int port, String password) {
            RedisURI redisURI = RedisURI.create(host, port);
            if (!StringUtils.isEmpty(password)) {
                redisURI.setPassword(password);
            }
            redisInfo.uri = redisURI;
            return this;
        }

        public RedisInfo build() {
            return redisInfo;
        }
    }

    @Override
    public int compareTo(RedisInfo o) {
        return id == o.getId() ? 0 : -1;
    }

    public String getHost() {
        return uri.getHost();
    }

    public int getPort() {
        return uri.getPort();
    }

    public long getId() {
        return id;
    }
}
