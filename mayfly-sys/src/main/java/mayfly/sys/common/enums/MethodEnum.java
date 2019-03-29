package mayfly.sys.common.enums;

import mayfly.common.enums.BaseEnum;

/**
 * @author meilin.huang
 * @version 1.0
 * @date 2019-03-23 10:49 AM
 */
public enum MethodEnum implements BaseEnum {

    /**
     * get请求方法
     */
    GET(1, "GET"),

    /**
     * post请求方法
     */
    POST(2, "POST"),

    /**
     * put请求方法
     */
    PUT(3, "PUT"),

    /**
     * delete请求方法
     */
    DELETE(4, "DELETE");


    private Integer value;

    private String name;

    MethodEnum(Integer value, String name) {
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
