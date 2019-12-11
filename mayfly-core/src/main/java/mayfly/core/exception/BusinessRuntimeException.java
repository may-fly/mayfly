package mayfly.core.exception;

import mayfly.core.result.Result;

/**
 * @author meilin.huang
 * @version 1.0
 * @date 2019-01-05 2:19 PM
 */
public class BusinessRuntimeException extends RuntimeException {

    private static final long serialVersionUID = -789021883759549647L;

    /**
     * 异常结果
     */
    private Result result;

    public BusinessRuntimeException(String msg) {
        super(msg);
    }


    public BusinessRuntimeException(Result result) {
        super(result.getMsg());
        this.result = result;
    }

    public Result getResult() {
        return result;
    }
}
