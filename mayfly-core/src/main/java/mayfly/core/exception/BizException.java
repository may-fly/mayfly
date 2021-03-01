package mayfly.core.exception;

import mayfly.core.model.result.CodeMessage;
import mayfly.core.model.result.CommonCodeEnum;

/**
 * 业务逻辑运行时异常
 *
 * @author meilin.huang
 * @version 1.0
 * @date 2019-01-05 2:19 PM
 */
public class BizException extends RuntimeException {

    private static final long serialVersionUID = -789021883759549647L;

    /**
     * 异常码
     */
    private Integer errorCode;

    protected BizException(String msg) {
        super(msg);
    }

    /**
     * @param errorMsg 错误消息
     */
    public BizException(CodeMessage errorMsg, Object... params) {
        super(params == null ? errorMsg.getMessage() : errorMsg.getMessage(params));
        this.errorCode = errorMsg.getCode();
    }


    /**
     * @param errCode 错误code
     * @param msg     错误消息
     */
    public BizException(Integer errCode, String msg) {
        super(msg);
        this.errorCode = errCode;
    }

    /**
     * 获取错误码
     *
     * @return error code
     */
    public Integer getErrorCode() {
        return errorCode;
    }
}
