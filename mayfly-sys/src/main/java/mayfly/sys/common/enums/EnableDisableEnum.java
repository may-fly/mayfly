package mayfly.sys.common.enums;

import mayfly.core.util.enums.NameValueEnum;

/**
 * 启用禁用枚举类
 *
 * @author meilin.huang
 * @version 1.0
 * @date 2019-12-25 10:28 上午
 */
public enum EnableDisableEnum implements NameValueEnum<Integer> {
    /**
     * 启用状态
     */
    ENABLE(1, "启用"),

    /**
     * 禁用状态
     */
    DISABLE(0, "禁用");

    private Integer value;
    private String name;
    EnableDisableEnum(Integer value, String name) {
        this.value = value;
        this.name = name;
    }


    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public Integer getValue() {
        return this.value;
    }
}
