package mayfly.sys.redis.enums;

import mayfly.common.enums.BaseEnum;

/**
 * @author meilin.huang
 * @version 1.0
 * @date 2019-04-04 13:21
 */
public enum RedisValueTypeEnum implements BaseEnum {


    /**
     * String类型
     */
    STRING(1, "string"),

    SET(2, "set")
    ;

    private Integer value;

    private String name;

    RedisValueTypeEnum(Integer value, String name) {
        this.value = value;
        this.name = name;
    }

    @Override
    public Integer getValue() {
        return value;
    }

    @Override
    public String getName() {
        return name;
    }
}
