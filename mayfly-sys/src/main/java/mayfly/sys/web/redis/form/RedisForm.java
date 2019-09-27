package mayfly.sys.web.redis.form;

import lombok.Data;
import mayfly.core.validation.annotation.NotBlank;
import mayfly.core.validation.annotation.NotNull;

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
    private Integer port;

    private String pwd;

    private Integer clusterId;

    private String description;
}
