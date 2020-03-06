package mayfly.sys.module.sys.enums;

import mayfly.core.util.enums.NameValueEnum;

/**
 * @author meilin.huang
 * @date 2020-03-05 5:01 下午
 */
public enum LogTypeEnum implements NameValueEnum<Integer> {
    /**
     * 更新
     */
    UPDATE(2, "更新"),
    DELETE(3, "删除"),
    SYS_LOG(4, "系统日志"),
    ERR_LOG(5, "异常");

    private final Integer value;
    private final String name;
    LogTypeEnum(Integer value, String name) {
        this.value = value;
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Integer getValue() {
        return value;
    }
}
