package mayfly.sys.module.db.enums;

import mayfly.core.util.enums.NameValueEnum;

/**
 * @author meilin.huang
 * @version 1.0
 * @date 2020-01-02 6:06 下午
 */
public enum  DbTypeEnum implements NameValueEnum<Integer> {

    /**
     * mysql
     */
    MYSQL(1, "mysql");

    private Integer value;
    private String name;

    DbTypeEnum(Integer value, String name) {
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
