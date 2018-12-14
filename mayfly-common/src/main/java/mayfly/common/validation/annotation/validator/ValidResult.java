package mayfly.common.validation.annotation.validator;;

/**
 * @author hml
 * @version 1.0
 * @description: 校验结果
 * @date 2018-10-31 8:42 AM
 */
public class ValidResult {

    private String message;

    private boolean right;

    public ValidResult(){}
    public ValidResult(boolean right, String message) {
        this.right = right;
        this.message = message;
    }

    /**
     * @return  返回正确的校验结果
     */
    public static ValidResult right() {
        return new ValidResult(true, null);
    }

    /**
     * @param message  错误消息
     * @return  返回错误的校验结果
     */
    public static ValidResult error(String message) {
        return new ValidResult(false, message);
    }


    public String getMessage() {
        return message;
    }

    public boolean isRight() {
        return right;
    }
}
