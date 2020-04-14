package mayfly.sys.module.redis.controller.form;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;


/**
 * @author meilin.huang
 * @version 1.0
 * @date 2019-01-04 4:31 PM
 */
@Data
public class RedisForm {

    private Long id;

    @NotBlank
    private String host;

    @NotNull
    private Integer port;

    private String pwd;

    private Long clusterId;

    private String description;
}
