package mayfly.common.exception;

/**
 * @author meilin.huang
 * @version 1.0
 * @date 2019-01-05 2:19 PM
 */
public class BusinessRuntimeException extends RuntimeException {

    private static final long serialVersionUID = -789021883759549647L;

    /**
     * 异常码
     */
    private Integer code;

    public BusinessRuntimeException(String msg) {
        super(msg);
    }

    public BusinessRuntimeException(Integer code, String msg) {
        super(msg);
        this.code = code;
    }

    public Integer getCode() {
        return code;
    }
}
