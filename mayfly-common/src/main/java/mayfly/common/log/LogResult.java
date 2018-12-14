package mayfly.common.log;

/**
 * @author meilin.huang
 * @version 1.0
 * @date 2018-11-19 3:27 PM
 */
public class LogResult {

    private Object[] args;

    private Object result;

    private long time;

    private Exception e;

    public LogResult(Object[] args, Object result, long time) {
        this.args = args;
        this.result = result;
        this.time = time;
    }

    public LogResult(Object[] args, Exception e) {
        this.args = args;
        this.e = e;
    }

    public static LogResult exception(Object[] args, Exception e) {
        return new LogResult(args, e);
    }

    public long getTime() {
        return time;
    }

    public Object getResult() {
        return result;
    }

    public Object[] getArgs() {
        return args;
    }

    public Exception getE() {
        return e;
    }
}
