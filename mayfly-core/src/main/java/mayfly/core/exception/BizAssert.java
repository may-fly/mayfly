package mayfly.core.exception;

import mayfly.core.util.CollectionUtils;
import mayfly.core.util.StringUtils;
import mayfly.core.util.enums.NameValueEnum;

import java.util.Collection;
import java.util.Objects;
import java.util.function.Supplier;

/**
 * 业务断言
 *
 * @author meilin.huang
 * @version 1.0
 * @date 2019-07-14 18:24
 */
public final class BizAssert {

    private BizAssert() {}

    /**
     * 断言对象不为空
     *
     * @param object 对象
     * @param msg    不满足断言的异常信息
     */
    public static void notNull(Object object, String msg) {
        if (object == null) {
            throw newBizRuntimeException(msg);
        }
    }

    public static void notNull(Object object, Supplier<String> supplier) {
        if (object == null) {
            throw newBizRuntimeException(supplier.get());
        }
    }

    public static <E extends Enum<?> & NameValueEnum<Integer>> void notNullByEnum(Object object, E errorEnum) {
        if (object == null) {
            throw newBizRuntimeException(errorEnum);
        }
    }

    /**
     * 断言对象为空
     *
     * @param object 对象
     * @param msg    不满足断言的异常信息
     */
    public static void isNull(Object object, String msg) {
        if (object != null) {
            throw newBizRuntimeException(msg);
        }
    }

    public static void isNull(Object object, Supplier<String> supplier) {
        if (object != null) {
            throw newBizRuntimeException(supplier.get());
        }
    }

    public static <E extends Enum<?> & NameValueEnum<Integer>> void isNullByEnum(Object object, E errorEnum) {
        if (object != null) {
            throw newBizRuntimeException(errorEnum);
        }
    }


    /**
     * 断言字符串不为空
     *
     * @param str 字符串
     * @param msg 不满足断言的异常信息
     */
    public static void notEmpty(String str, String msg) {
        if (StringUtils.isEmpty(str)) {
            throw newBizRuntimeException(msg);
        }
    }

    public static void notEmpty(String str, Supplier<String> supplier) {
        if (StringUtils.isEmpty(str)) {
            throw newBizRuntimeException(supplier.get());
        }
    }

    public static <E extends Enum<?> & NameValueEnum<Integer>> void notEmptyByEnum(String str, E errorEnum) {
        if (StringUtils.isEmpty(str)) {
            throw newBizRuntimeException(errorEnum);
        }
    }

    /**
     * 断言集合不为空
     *
     * @param collection 集合
     * @param msg        不满足断言的异常信息
     */
    public static void notEmpty(Collection<?> collection, String msg) {
        if (CollectionUtils.isEmpty(collection)) {
            throw newBizRuntimeException(msg);
        }
    }

    /**
     * 断言集合不为空
     *
     * @param collection 集合
     * @param supplier   不满足断言的异常信息提供者
     */
    public static void notEmpty(Collection<?> collection, Supplier<String> supplier) {
        if (CollectionUtils.isEmpty(collection)) {
            throw newBizRuntimeException(supplier.get());
        }
    }

    /**
     * 断言集合为空
     *
     * @param collection 集合
     * @param msg        不满足断言的异常信息
     */
    public static void empty(Collection<?> collection, String msg) {
        if (CollectionUtils.isNotEmpty(collection)) {
            throw newBizRuntimeException(msg);
        }
    }

    /**
     * 断言集合为空
     *
     * @param collection 集合
     * @param supplier   不满足断言的异常信息提供器
     */
    public static void empty(Collection<?> collection, Supplier<String> supplier) {
        if (CollectionUtils.isNotEmpty(collection)) {
            throw newBizRuntimeException(supplier.get());
        }
    }

    /**
     * 断言两个对象必须相等
     *
     * @param o1  对象1
     * @param o2  对象2
     * @param msg 错误消息
     */
    public static void equals(Object o1, Object o2, String msg) {
        if (!Objects.equals(o1, o2)) {
            throw newBizRuntimeException(msg);
        }
    }

    /**
     * 断言两个对象必须相等
     *
     * @param o1          对象1
     * @param o2          对象2
     * @param msgSupplier 错误消息提供器
     */
    public static void equals(Object o1, Object o2, Supplier<String> msgSupplier) {
        if (!Objects.equals(o1, o2)) {
            throw newBizRuntimeException(msgSupplier.get());
        }
    }

    /**
     * 断言两个对象必须相等
     *
     * @param o1        对象1
     * @param o2        对象2
     * @param errorEnum 错误枚举
     */
    public static <E extends Enum<?> & NameValueEnum<Integer>> void equalsByEnum(Object o1, Object o2, E errorEnum) {
        if (!Objects.equals(o1, o2)) {
            throw newBizRuntimeException(errorEnum);
        }
    }


    /**
     * 断言一个boolean表达式为true
     *
     * @param expression boolean表达式
     * @param message    不满足断言的异常信息
     */
    public static void isTrue(boolean expression, String message) {
        if (!expression) {
            throw newBizRuntimeException(message);
        }
    }

    /**
     * 断言一个boolean表达式为true，用于需要大量拼接字符串以及一些其他操作等
     *
     * @param expression boolean表达式
     * @param supplier   msg生产者
     */
    public static void isTrue(boolean expression, Supplier<String> supplier) {
        if (!expression) {
            throw newBizRuntimeException(supplier.get());
        }
    }

    /**
     * 断言一个boolean表达式为true，用于需要大量拼接字符串以及一些其他操作等
     *
     * @param expression boolean表达式
     * @param errorEnum  错误枚举
     */
    public static <E extends Enum<?> & NameValueEnum<Integer>> void isTrueByEnum(boolean expression, E errorEnum) {
        if (!expression) {
            throw newBizRuntimeException(errorEnum);
        }
    }

    /**
     * 创建业务运行时异常对象
     *
     * @param msg 异常信息
     * @return 异常
     */
    public static BizRuntimeException newBizRuntimeException(String msg) {
        return new BizRuntimeException(msg);
    }

    /**
     * 创建业务运行时异常对象
     *
     * @param errorEnum 错误枚举
     * @return 异常
     */
    public static <E extends Enum<?> & NameValueEnum<Integer>> BizRuntimeException newBizRuntimeException(E errorEnum) {
        return new BizRuntimeException(errorEnum);
    }
}
