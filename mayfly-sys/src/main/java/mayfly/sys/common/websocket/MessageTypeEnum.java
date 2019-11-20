package mayfly.sys.common.websocket;

import mayfly.core.result.ResultEnum;
import mayfly.core.util.enums.ValueEnum;

/**
 * @author meilin.huang
 * @version 1.0
 * @date 2019-11-13 2:19 下午
 */
public enum MessageTypeEnum implements ValueEnum<Integer> {

    /**
     * info消息
     */
    INFO(1),

    /**
     * 成功消息
     */
    SUCCESS(2),

    /**
     * 错误消息
     */
    ERROR(3);
    ;

    private Integer value;

    MessageTypeEnum(Integer value) {
        this.value = value;
    }

    /**
     * 获取WebSocketMessage对象的字符串信息
     * @param data  数据
     * @return      websocket message
     */
    public String toMsg(Object data) {
        return WebSocketMessage.msg(this, ResultEnum.SUCCESS, data);
    }

    @Override
    public Integer getValue() {
        return value;
    }
}
