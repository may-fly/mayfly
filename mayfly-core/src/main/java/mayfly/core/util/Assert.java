package mayfly.core.util;

import java.util.Collection;
import java.util.function.Supplier;

/**
 * @author meilin.huang
 * @version 1.0
 * @date 2019-01-19 4:22 PM
 */
public class Assert {

    private Assert() {}

    public static void notEmpty(String string, String message) {
        state(!StringUtils.isEmpty(string), message);
    }

    public static void notEmpty(String string, Supplier<String> messageSupplier) {
        state(!StringUtils.isEmpty(string), messageSupplier);
    }

    public static <T> void notNull(T object, String message) {
        state(object != null, message);
    }

    public static <T> void notNull(T object, Supplier<String> messageSupplier) {
        state(object != null, messageSupplier);
    }

    public static <T> void notEmpty(T[] array, String message) {
        state(array != null && array.length > 0, message);
    }

    public static void notEmpty(Collection<?> collection, String message) {
        state(!CollectionUtils.isEmpty(collection), message);
    }

    public static void assertState(boolean condition, String message) {
        if (!condition) {
            throw new IllegalStateException(message);
        }
    }

    public static void state(boolean expression, String message) {
        if (!expression) {
            throw new IllegalArgumentException(message);
        }
    }

    /**
     * 断言一个boolean表达式，用于需要大量拼接字符串以及一些其他操作等
     * @param expression  boolean表达式
     * @param supplier    msg生产者
     */
    public static void state(boolean expression, Supplier<String> supplier) {
        if (!expression) {
            throw new IllegalArgumentException(supplier.get());
        }
    }
}
