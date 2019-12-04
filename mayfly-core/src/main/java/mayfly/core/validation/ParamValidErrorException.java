package mayfly.core.validation;

/**
 * 校验参数错误异常
 * @author hml
 * @version 1.0
 * @date 2018-10-28 10:35 AM
 */
public class ParamValidErrorException extends Exception {

    private static final long serialVersionUID = 7940419914374927057L;

    public ParamValidErrorException(String msg) {
        super(msg);
    }
}
