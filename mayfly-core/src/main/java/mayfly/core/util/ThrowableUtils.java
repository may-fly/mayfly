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
}
