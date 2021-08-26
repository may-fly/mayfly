package mayfly.core.model.result;

/**
 * 结果码及消息接口
 *
 * @author meilin.huang
 * @date 2020-12-26 4:11 下午
 */
public interface CodeMessage {

    /**
     * 返回结果码
     *
     * @return code
     */
    String getCode();

    /**
     * 返回结果消息
     *
     * @return msg
     */
    String getMessage();

    /**
     * 获取完整带有占位符的错误消息
     *
     * @param params 占位符参数值
     * @return 完整错误消息
     */
    default String getMessage(Object... params) {
        return String.format(getMessage(), params);
    }

    /**
     * 转换为Result
     *
     * @param message 消息
     * @return {@linkplain Result}
     */
    default Result<?> toResult(String message) {
        return Result.of(this.getCode(), message);
    }

    /**
     * 转换为Result
     *
     * @param params 消息占位符参数
     * @return {@linkplain Result}
     */
    default Result<?> toResult(Object... params) {
        return Result.of(this, params);
    }
}
