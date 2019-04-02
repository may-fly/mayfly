package mayfly.sys.web.redis.form;

import lombok.Data;
import mayfly.common.validation.annotation.NotBlank;
import mayfly.common.validation.annotation.NotNull;

/**
 * @author meilin.huang
 * @version 1.0
 * @date 2019-01-04 4:31 PM
 */
@Data
public class RedisForm {

    @NotBlank
    private String host;

    @NotNull
    private int port = 6379;

    private String pwd;

    private Integer clusterId;

    private String description;
}
