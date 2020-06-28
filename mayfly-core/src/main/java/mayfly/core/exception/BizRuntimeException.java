package mayfly.core.exception;

import mayfly.core.base.model.Result;
import mayfly.core.util.enums.NameValueEnum;

/**
 * 业务逻辑运行时异常
 *
 * @author meilin.huang
 * @version 1.0
 * @date 2019-01-05 2:19 PM
 */
public class BizRuntimeException extends RuntimeException {

    private static final long serialVersionUID = -789021883759549647L;

    /**
     * 异常码
     */
    private final Integer errorCode;

    /**
     * 默认错误code为 {@linkplain mayfly.core.base.model.Result.CodeEnum#FAILURE}
     *
     * @param msg 错误消息
     */
    public BizRuntimeException(String msg) {
        super(msg);
        this.errorCode = Result.CodeEnum.FAILURE.getValue();
    }

    /**
     * @param errorEnum 错误枚举值
     * @param <E>       异常枚举类型
     */
    public <E extends Enum<?> & NameValueEnum<Integer>> BizRuntimeException(E errorEnum) {
        super(errorEnum.getName());
        this.errorCode = errorEnum.getValue();
    }

    public Integer getErrorCode() {
        return errorCode;
    }
}
