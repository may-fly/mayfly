package mayfly.common.enums;

/**
 * @author meilin.huang
 * @version 1.0
 * @date 2018-12-07 1:43 PM
 */
public enum StatusEnum implements ValueEnum {
    /**
     * 禁用状态
     */
    DISABLE(0),

    /**
     * 启用状态
     */
    ENABLE(1);

    /**
     * 状态值
     */
    private Integer value;

    StatusEnum(Integer value) {
        this.value = value;
    }


    @Override
    public Integer getValue() {
        return this.value;
    }
}


