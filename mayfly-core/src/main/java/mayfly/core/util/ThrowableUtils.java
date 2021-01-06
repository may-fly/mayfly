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

    /**
     * 获取指定层级堆栈信心
     *
     * @param e     异常信息
     * @param level 堆栈层级
     * @return 堆栈信息
     */
    public static String getStackTraceByLevel(Throwable e, int level) {
        StringBuilder s = new StringBuilder("\n").append(e);
        int start = 0;
        for (StackTraceElement traceElement : e.getStackTrace()) {
            s.append("\n\tat ").append(traceElement);
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
        StringBuilder s = new StringBuilder("\n").append(e);
        for (StackTraceElement traceElement : e.getStackTrace()) {
            if (!traceElement.getClassName().startsWith(packagePrefix)) {
                break;
            }
            s.append("\n\tat ").append(traceElement);
        }
        return s.toString();
    }
}
