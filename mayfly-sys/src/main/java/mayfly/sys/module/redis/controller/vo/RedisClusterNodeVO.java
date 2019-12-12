package mayfly.sys.module.redis.controller.vo;

import io.lettuce.core.cluster.models.partitions.RedisClusterNode;
import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.Set;

/**
 * @author meilin.huang
 * @version 1.0
 * @date 2019-01-21 10:27 AM
 */
@Data
@Builder
public class RedisClusterNodeVO {

    private String address;

    private String nodeId;

    private String slaveof;

    private long configEpoch;

    private Set<RedisClusterNode.NodeFlag> flags;

    private Integer startSlots;

    private Integer endSlogts;

    private List<RedisClusterNodeVO> slaves;
}
