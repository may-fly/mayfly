package mayfly.sys.devops.module.redis.controller.vo;

import lombok.Builder;
import lombok.Data;

/**
 * @author meilin.huang
 * @version 1.0
 * @date 2019-01-17 4:57 PM
 */
@Data
@Builder
public class RedisInfoVO {

    private Integer id;

    private boolean cluster;

    private String host;

    private int port;

    private String password;
}
