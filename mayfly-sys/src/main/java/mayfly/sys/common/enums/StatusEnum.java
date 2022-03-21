package mayfly.sys.common.enums;

/**
 * @author meilin.huang
 * @date 2022-02-15 3:30 PM
 */
public enum StatusEnum {

    /**
     * 启用状态
     */
    ENABLE(1, "启用"),

    /**
     * 禁用状态
     */
    DISABLE(0, "禁用");

    private final Integer value;
    private final String name;
    StatusEnum(Integer value, String name) {
        this.value = value;
        this.name = name;
    }

    public static String getNameByValue(Integer value) {
        StatusEnum[] values = StatusEnum.values();
        for (StatusEnum status : values) {
            if (status.value.equals(value)) {
                return status.name;
            }
        }
        return null;
    }

    public Integer getValue() {
        return value;
    }

    public String getName() {
        return name;
    }
}
