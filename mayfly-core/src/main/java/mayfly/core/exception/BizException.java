package mayfly.core.exception;

import mayfly.core.model.result.CodeMessage;

/**
 * 业务逻辑运行时异常
 *
 * @author meilin.huang
 * @version 1.0
 * @date 2019-01-05 2:19 PM
 */
public class BizException extends BaseException {

    private static final long serialVersionUID = -789021883759549647L;

    /**
     * @param errorMsg 错误消息
     */
    public BizException(CodeMessage errorMsg, Object... params) {
        super(errorMsg, params);
    }

    /**
     * @param errCode 错误code
     * @param msg     错误消息
     */
    public BizException(String errCode, String msg) {
        super(errCode, msg);
    }
}
