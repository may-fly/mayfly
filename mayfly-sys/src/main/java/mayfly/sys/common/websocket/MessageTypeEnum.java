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
     * 系统通知消息
     */
    SYS_NOTIFY(1) {
        @Override
        public String toMsg(Object data) {
            return WebSocketMessage.msg(this, ResultEnum.SUCCESS, data);
        }

        @Override
        public String error(ResultEnum result) {
            return WebSocketMessage.msg(this, result);
        }
    }
    ;

    private Integer value;

    MessageTypeEnum(Integer value) {
        this.value = value;
    }

    /**
     * 获取WebSocketMessage对象的字符串信息
     * @param data  数据
     * @return
     */
    public abstract String toMsg(Object data);

    public abstract String error(ResultEnum result);

    @Override
    public Integer getValue() {
        return value;
    }
}
