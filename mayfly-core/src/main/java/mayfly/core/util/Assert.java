package mayfly.core.util;

import java.util.Collection;

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

    public static <T> void notNull(T object, String message) {
        state(object != null, message);
    }

    public static <T> void notEmpty(T[] array, String message) {
        state(array != null && array.length > 0, message);
    }

    public static void notEmpty(Collection<?> collection, String message) {
        state(!CollectionUtils.isEmpty(collection), message);
    }

    public static void state(boolean expression, String message) {
        if (!expression) {
            throw new IllegalArgumentException(message);
        }
    }

    public static void assertState(boolean condition, String message) {
        if (!condition) {
            throw new IllegalStateException(message);
        }
    }
}
