package mayfly.sys.redis.web.vo;

import lombok.Data;
import mayfly.common.validation.annotation.NotBlank;

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
