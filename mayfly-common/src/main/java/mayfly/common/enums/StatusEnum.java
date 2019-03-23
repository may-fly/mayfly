package mayfly.common.enums;

/**
 * @author meilin.huang
 * @version 1.0
 * @date 2018-12-07 1:43 PM
 */
public enum StatusEnum implements BaseEnum{
    /**
     * 禁用状态
     */
    DISABLE(0, "禁用"),

    /**
     * 启用状态
     */
    ENABLE(1, "启用");

    /**
     * 状态值
     */
    private Integer value;

    private String name;

    StatusEnum(Integer value, String name) {
        this.value = value;
        this.name = name;
    }


    @Override
    public Integer getValue() {
        return this.value;
    }

    @Override
    public String getName() {
        return this.name;
    }
}
