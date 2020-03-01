package mayfly.sys.common.utils.ssh;

/**
 * @author meilin.huang
 * @version 1.0
 * @date 2019-01-07 1:48 PM
 */
public class SSHException extends Exception {

    private static final long serialVersionUID = -6213665149000064880L;

    public SSHException() {
        super();
    }

    public SSHException(String message) {
        super(message);
    }

    public SSHException(String message, Throwable cause) {
        super(message, cause);
    }

    public SSHException(Throwable cause) {
        super(cause);
    }

}
