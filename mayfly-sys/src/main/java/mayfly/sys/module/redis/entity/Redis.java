package mayfly.sys.module.redis.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import mayfly.core.base.model.BaseEntity;

/**
 * @author meilin.huang
 * @version 1.0
 * @date 2019-01-07 4:06 PM
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Redis extends BaseEntity {
    private String host;

    private Integer port;

    private String pwd;

    private String description;

    private Integer clusterId;
}
