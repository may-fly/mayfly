package mayfly.sys.module.redis.controller.vo;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * @author meilin.huang
 * @version 1.0
 * @date 2019-01-19 1:31 PM
 */
@Data
public class RedisConfParamVO {

    @NotBlank
    private String param;

    @NotBlank
    private String value;
}
