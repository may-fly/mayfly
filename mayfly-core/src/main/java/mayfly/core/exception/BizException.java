package mayfly.core.exception;

import mayfly.core.model.result.CodeMessage;
import mayfly.core.model.result.CommonCodeEnum;

/**
 * 业务逻辑异常
 *
 * @author meilin.huang
 * @version 1.0
 * @date 2018-12-07 3:10 PM
 */
public class BizException extends Exception {

    private static final long serialVersionUID = 7448623878598565912L;

    /**
     * 异常码
     */
    private final Integer errorCode;

    /**
     * 默认错误code为 {@linkplain CommonCodeEnum#FAILURE}
     *
     * @param msg 错误消息
     */
    public BizException(String msg) {
        super(msg);
        this.errorCode = CommonCodeEnum.FAILURE.getCode();
    }

    /**
     * @param errorEnum 错误枚举值
     */
    public BizException(CodeMessage errorEnum) {
        super(errorEnum.getMessage());
        this.errorCode = errorEnum.getCode();
    }

    public Integer getErrorCode() {
        return errorCode;
    }
}
