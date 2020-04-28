package mayfly.core.result;

import mayfly.core.util.enums.NameValueEnum;

/**
 * 结果枚举类
 *
 * @author meilin.huang
 * @version 1.0
 * @date 2019-06-26 15:24
 */
public enum ResultEnum implements NameValueEnum<Integer> {
    /**
     * 操作成功
     */
    SUCCESS(200, "SUCCESS"),

    /**
     * 操作失败（通常为业务逻辑错误）
     */
    FAILURE(400, "FAILURE"),

    /**
     * 参数错误
     */
    PARAM_ERROR(405, "PARAM_ERROR"),

    /**
     * 资源未找到
     */
    NO_FOUND(404, "NO_FOUND"),

    /**
     * 服务器异常（其他未知错误）
     */
    SERVER_ERROR(500, "SERVER_ERROR"),

    /**
     * 无权限
     */
    NO_PERMISSION(501, "NO_PERMISSION");


    /**
     * 结果操作码
     */
    private final Integer code;
    /**
     * 结果消息
     */
    private final String msg;

    ResultEnum(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    @Override
    public String getName() {
        return this.msg;
    }

    @Override
    public Integer getValue() {
        return this.code;
    }
}
