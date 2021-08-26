package mayfly.core.exception;

import mayfly.core.model.result.CodeMessage;

/**
 * @author meilin.huang
 * @date 2021-08-26 10:46 上午
 */
public class BaseException extends RuntimeException{
    /**
     * 异常码
     */
    private String errorCode;

    protected BaseException(String msg) {
        super(msg);
    }

    /**
     * @param errorMsg 错误消息
     */
    public BaseException(CodeMessage errorMsg, Object... params) {
        super(params == null ? errorMsg.getMessage() : errorMsg.getMessage(params));
        this.errorCode = errorMsg.getCode();
    }


    /**
     * @param errCode 错误code
     * @param msg     错误消息
     */
    public BaseException(String errCode, String msg) {
        super(msg);
        this.errorCode = errCode;
    }

    /**
     * 获取错误码
     *
     * @return error code
     */
    public String getErrorCode() {
        return errorCode;
    }
}
