package mayfly.core.model.result;

/**
 * 公共结果码枚举类
 *
 * @author meilin.huang
 * @date 2020-12-26 4:34 下午
 */
public enum CommonCodeEnum implements CodeMessage {
    /**
     * 操作成功
     */
    SUCCESS("200", "success"),

    /**
     * 操作失败（通常为业务逻辑错误）
     */
    FAILURE("400", "failure"),

    /**
     * 参数错误
     */
    PARAM_ERROR("405", "参数错误"),

    /**
     * 资源未找到
     */
    NO_FOUND("404", "no found"),

    /**
     * 服务器异常（其他未知错误）
     */
    SERVER_ERROR("500", "server error"),

    /**
     * 无权限
     */
    NO_PERMISSION("501", "no permission");


    /**
     * 结果操作码
     */
    private final String code;
    /**
     * 结果消息
     */
    private final String msg;

    CommonCodeEnum(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    @Override
    public String getMessage() {
        return this.msg;
    }

    @Override
    public String getCode() {
        return this.code;
    }
}
