package mayfly.core.log;

import java.lang.reflect.Method;
import java.util.Optional;

/**
 * 日志信息
 *
 * @author meilin.huang
 * @date 2021-09-17 7:40 下午
 */
public class LogInfo {
    /**
     * 日志信息
     */
    private String content;

    /**
     * 日志级别
     */
    private Log.LogLevel level;

    /**
     * 记录返回值日志级别
     */
    private Log.LogLevel resLevel;

    /**
     * 是否记录返回结果，默认true
     */
    private boolean logRes;

    /**
     * 方法信息
     */
    private Method method;

    /**
     * 从方法获取对应的日志元信息
     *
     * @param method 方法
     * @return 日志元信息
     */
    public static LogInfo from(Method method) {
        Log ml = Optional.ofNullable(method.getAnnotation(Log.class))
                .orElse(method.getDeclaringClass().getAnnotation(Log.class));
        LogInfo li = new LogInfo();
        li.method = method;
        li.content = ml.value();
        li.level = ml.level();
        li.resLevel = ml.resLevel();
        li.logRes = ml.res();
        return li;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Log.LogLevel getLevel() {
        return level;
    }

    public void setLevel(Log.LogLevel level) {
        this.level = level;
    }

    public Log.LogLevel getResLevel() {
        return resLevel;
    }

    public void setResLevel(Log.LogLevel resLevel) {
        this.resLevel = resLevel;
    }

    public boolean isLogRes() {
        return logRes;
    }

    public void setLogRes(boolean logRes) {
        this.logRes = logRes;
    }

    public Method getMethod() {
        return method;
    }

    public void setMethod(Method method) {
        this.method = method;
    }
}
