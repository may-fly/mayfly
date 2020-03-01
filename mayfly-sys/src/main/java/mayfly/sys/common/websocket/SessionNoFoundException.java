package mayfly.sys.common.websocket;

/**
 * @author meilin.huang
 * @version 1.0
 * @date 2019-11-12 10:17 下午
 */
public class SessionNoFoundException extends Exception {

    private static final long serialVersionUID = -4641006794114763898L;

    public SessionNoFoundException(Exception e) {
        super(e);
    }

    public SessionNoFoundException(String msg) {
        super(msg);
    }
}
