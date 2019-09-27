package mayfly.core.validation;

/**
 * @author hml
 * @version 1.0
 * @description: 校验参数错误异常
 * @date 2018-10-28 10:35 AM
 */
public class ParamValidErrorException extends Exception {

    public ParamValidErrorException(String msg) {
        super(msg);
    }
}
