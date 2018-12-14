package mayfly.common.validation;

/**
 * @author hml
 * @version 1.0
 * @description: 校验参数错误异常
 * @date 2018-10-28 10:35 AM
 */
public class ParamErrorException extends Exception {

    public ParamErrorException(String msg) {
        super(msg);
    }
}
