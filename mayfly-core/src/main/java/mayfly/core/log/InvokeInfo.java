package mayfly.core.log;

/**
 * 方法调用信息
 *
 * @author meilin.huang
 * @date 2021-09-17 4:18 下午
 */
public class InvokeInfo {
    /**
     * 方法参数
     */
    private Object[] args;

    /**
     * 方法执行结果
     */
    private Object res;

    /**
     * 执行时间
     */
    private long execTime;

    /**
     * 异常信息
     */
    private Exception exception;

    /**
     * 当前系统日志级别
     */
    private Log.LogLevel sysLevel;

    public static InvokeInfo newInstance() {
        return new InvokeInfo();
    }

    public InvokeInfo args(Object[] args) {
        this.args = args;
        return this;
    }

    public InvokeInfo res(Object res) {
        this.res = res;
        return this;
    }

    public InvokeInfo execTime(long execTime) {
        this.execTime = execTime;
        return this;
    }

    public InvokeInfo exception(Exception e) {
        this.exception = e;
        return this;
    }

    public InvokeInfo sysLevel(Log.LogLevel sysLevel) {
        this.sysLevel = sysLevel;
        return this;
    }


    public Object[] getArgs() {
        return args;
    }

    public Object getRes() {
        return res;
    }

    public long getExecTime() {
        return execTime;
    }

    public Exception getException() {
        return exception;
    }

    public Log.LogLevel getSysLevel() {
        return sysLevel;
    }
}
