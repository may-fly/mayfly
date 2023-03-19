package mayfly.sys.module.redis.controller.form;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import mayfly.core.validation.annotation.EnumValue;
import mayfly.sys.module.redis.enums.RedisValueTypeEnum;

/**
 * @author meilin.huang
 * @version 1.0
 * @date 2019-04-04 14:06
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class KeyValueForm {

    private Long ttl;

    @NotBlank
    private String key;

    @NotBlank
    private String value;

    /**
     * @see mayfly.sys.module.redis.enums.RedisValueTypeEnum
     */
    @NotNull
    @EnumValue(RedisValueTypeEnum.class)
    private Integer type;
}
