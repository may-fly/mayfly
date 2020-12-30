package mayfly.core.util;

import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * @author meilin.huang
 * @date 2020-07-30 5:21 下午
 */
public final class ThrowableUtils {

    /**
     * 获取完整堆栈错误信息
     *
     * @param e throwable
     * @return 异常完整对象信息
     */
    public static String toString(Throwable e) {
        StringWriter stringWriter = new StringWriter();
        e.printStackTrace(new PrintWriter(stringWriter));
        return stringWriter.toString();
    }

    public static String getStackTraceByLevel(Throwable e, int level) {
        StackTraceElement[] trace = e.getStackTrace();
        StringBuilder s = new StringBuilder();
        s.append(e);
        int start = 0;
        for (StackTraceElement traceElement : trace) {
            s.append("\tat ").append(traceElement);
            if (++start == level) {
                break;
            }
        }
        return s.toString();
    }

    /**
     * 获取以指定包名为前缀的堆栈信息
     *
     * @param e             异常
     * @param packagePrefix 包前缀
     * @return 堆栈信息
     */
    public static String getStackTraceByPn(Throwable e, String packagePrefix) {
        StackTraceElement[] trace = e.getStackTrace();
        StringBuilder s = new StringBuilder();
        s.append("\n").append(e);
        for (StackTraceElement traceElement : trace) {
            if (!traceElement.getClassName().startsWith(packagePrefix)) {
                break;
            }
            s.append("\n\tat ").append(traceElement);
        }
        return s.toString();
    }

}
