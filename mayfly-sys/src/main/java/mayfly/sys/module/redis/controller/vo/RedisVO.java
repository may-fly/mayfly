package mayfly.sys.module.redis.controller.vo;

import lombok.Data;

/**
 * @author meilin.huang
 * @date 2020-03-07 2:24 下午
 */
@Data
public class RedisVO {

    private Long id;

    private String host;

    private Integer port;

    private String description;

    private Long clusterId;
}
