package mayfly.sys.devops.module.redis.controller.form;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import mayfly.core.validation.annotation.EnumValue;
import mayfly.core.validation.annotation.NotBlank;
import mayfly.core.validation.annotation.NotNull;
import mayfly.sys.devops.redis.enums.RedisValueTypeEnum;

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
     * @see mayfly.sys.devops.redis.enums.RedisValueTypeEnum
     */
    @NotNull
    @EnumValue(RedisValueTypeEnum.class)
    private Integer type;
}
