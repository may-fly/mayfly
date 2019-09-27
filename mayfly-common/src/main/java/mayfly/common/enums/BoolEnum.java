package mayfly.common.enums;

/**
 * boolean枚举值
 * @author meilin.huang
 * @version 1.0
 * @date 2018-12-07 1:43 PM
 */
public enum BoolEnum implements ValueEnum<Integer> {
    /**
     * false
     */
    FALSE(0),

    /**
     * true
     */
    TRUE(1);

    /**
     * 状态值
     */
    private Integer value;

    BoolEnum(Integer value) {
        this.value = value;
    }


    @Override
    public Integer getValue() {
        return this.value;
    }
}


