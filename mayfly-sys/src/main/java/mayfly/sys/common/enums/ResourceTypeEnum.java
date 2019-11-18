package mayfly.sys.common.enums;

import mayfly.core.util.enums.NameValueEnum;

/**
 * 资源类型枚举
 * @author meilin.huang
 * @version 1.0
 * @date 2019-03-25 5:10 PM
 */
public enum ResourceTypeEnum implements NameValueEnum<Integer> {

    /**
     * 菜单类型
     */
    MENU(1, "菜单"),

    /**
     * 权限类型
     */
    PERMISSION(2, "权限");

    private Integer value;

    private String name;

    ResourceTypeEnum(Integer value, String name) {
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
