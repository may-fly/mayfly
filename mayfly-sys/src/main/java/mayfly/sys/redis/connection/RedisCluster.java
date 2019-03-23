package mayfly.sys.redis.connection;

import lombok.Builder;
import lombok.Data;

import java.util.Set;

/**
 * @author meilin.huang
 * @version 1.0
 * @date 2019-01-18 1:44 PM
 */
@Data
@Builder
public class RedisCluster {
    /**
     * 集群id
     */
    private int id;

    /**
     * 节点信息
     */
    private Set<RedisInfo> nodes;

    /**
     * 获取node信息
     * @param id
     * @return
     */
    public RedisInfo getNode(int id) {
        for (RedisInfo ri : nodes) {
            if (id == ri.getId()) {
                return ri;
            }
        }

        return null;
    }
}
