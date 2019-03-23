package mayfly.sys.redis.connection;

import io.lettuce.core.RedisURI;
import lombok.Data;

/**
 * @author meilin.huang
 * @version 1.0
 * @date 2019-01-16 4:16 PM
 */
@Data
public class RedisInfo implements Comparable<RedisInfo>{

    /**
     * 默认为单机
     */
    public static final int STANDALONE = 0;

    /**
     * redis id
     */
    private int id;

    /**
     * 集群节点id
     */
    private int clusterId = STANDALONE;

    /**
     * redis uri
     */
    private RedisURI uri;



    public String getHost() {
        return uri.getHost();
    }

    public int getPort() {
        return uri.getPort();
    }

    public static Builder builder(int id) {
        return new Builder(id);
    }


    @Override
    public int compareTo(RedisInfo o) {
        return id == o.getId() ? 0 : -1;
    }


    public static class Builder{
        private RedisInfo redisInfo;

        public Builder(int id) {
            redisInfo = new RedisInfo();
            redisInfo.id = id;
        }

        public Builder uri(RedisURI redisURI) {
            redisInfo.uri = redisURI;
            return this;
        }

        public Builder clusterId(int clusterId) {
            redisInfo.clusterId = clusterId;
            return this;
        }

        public RedisInfo build() {
            return redisInfo;
        }

    }

    public Integer getId() {
        return id;
    }
}
