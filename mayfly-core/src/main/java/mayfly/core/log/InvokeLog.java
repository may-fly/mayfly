package mayfly.core.log;

import mayfly.core.log.annotation.Log;

import java.lang.reflect.Method;
import java.util.List;

/**
 * 方法调用信息
 *
 * @author meilin.huang
 * @date 2021-09-17 4:18 下午
 */
public class InvokeLog {

    /**
     * 日志描述
     */
    private String description;

    /**
     * 调用方法
     */
    private Method method;

    /**
     * 方法参数
     */
    private Arg[] args;

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
     * 实体字段变更记录
     */
    private List<FieldValueChangeRecord> fieldChanges;

    /**
     * 记录该方法日志的日志级别
     */
    private Log.Level level;

    /**
     * 是否需要记录返回结果
     */
    private boolean logRes;

    public static InvokeLog newInstance() {
        return new InvokeLog();
    }

    public InvokeLog description(String description) {
        this.description = description;
        return this;
    }

    public InvokeLog method(Method method) {
        this.method = method;
        return this;
    }

    public InvokeLog args(Arg[] args) {
        this.args = args;
        return this;
    }

    public InvokeLog res(Object res) {
        this.res = res;
        return this;
    }

    public InvokeLog execTime(long execTime) {
        this.execTime = execTime;
        return this;
    }

    public InvokeLog exception(Exception e) {
        this.exception = e;
        return this;
    }

    public InvokeLog fieldChanges(List<FieldValueChangeRecord> fieldChanges) {
        this.fieldChanges = fieldChanges;
        return this;
    }

    public InvokeLog logRes(boolean logRes) {
        this.logRes = logRes;
        return this;
    }

    public InvokeLog level(Log.Level level) {
        this.level = level;
        return this;
    }

    /**
     * 参数信息
     */
    public static class Arg {
        private final String name;

        private final Object value;

        public Arg(String name, Object value) {
            this.name = name;
            this.value = value;
        }

        public String getName() {
            return name;
        }

        public Object getValue() {
            return value;
        }
    }

    public Method getMethod() {
        return method;
    }

    public Arg[] getArgs() {
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

    public String getDescription() {
        return description;
    }

    public List<FieldValueChangeRecord> getFieldChanges() {
        return fieldChanges;
    }

    public boolean isLogRes() {
        return logRes;
    }

    public Log.Level getLevel() {
        return level;
    }
}
