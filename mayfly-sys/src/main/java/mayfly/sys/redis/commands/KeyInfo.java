package mayfly.sys.redis.commands;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author meilin.huang
 * @version 1.0
 * @date 2019-01-05 3:05 PM
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class KeyInfo<T> {

    private Long ttl;

    /**
     * @see mayfly.sys.redis.enums.RedisValueTypeEnum
     */
    private Integer type;

    private String key;

    private T value;
}
