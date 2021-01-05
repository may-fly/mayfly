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
    Integer getCode();

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
}
