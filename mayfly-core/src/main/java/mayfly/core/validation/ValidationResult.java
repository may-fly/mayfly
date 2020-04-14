package mayfly.core.validation;

import java.util.Arrays;

/**
 * @author meilin.huang
 * @date 2020-04-14 10:55 上午
 */
public class ValidationResult {

    /**
     * 校验是否成功
     */
    private final boolean success;

    /**
     * 校验失败的错误消息
     */
    private String[] errorMsgs;

    private ValidationResult(boolean success) {
        this.success = success;
    }

    private ValidationResult(boolean success, String[] errorMsgs) {
        this.success = success;
        this.errorMsgs = errorMsgs;
    }

    /**
     * 成功工厂方法
     *
     * @return self
     */
    public static ValidationResult success() {
        return new ValidationResult(true);
    }

    /**
     * 失败工厂方法
     *
     * @param msgs 错误消息数组
     * @return self
     */
    public static ValidationResult error(String[] msgs) {
        return new ValidationResult(false, msgs);
    }


    public String[] getErrorMsgs() {
        return errorMsgs;
    }

    public boolean isSuccess() {
        return success;
    }

    @Override
    public String toString() {
        return "ValidationResult{" +
                "success=" + success +
                ", errorMsgs=" + Arrays.toString(errorMsgs) +
                '}';
    }
}
