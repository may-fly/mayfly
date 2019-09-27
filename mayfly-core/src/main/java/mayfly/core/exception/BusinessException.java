package mayfly.core.exception;

/**
 * @author meilin.huang
 * @version 1.0
 * @date 2018-12-07 3:10 PM
 */
public class BusinessException extends Exception {

    private static final long serialVersionUID = 7448623878598565912L;

    public BusinessException(String msg) {
        super(msg);
    }
}
