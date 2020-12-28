package mayfly.core.exception;

import mayfly.core.base.model.CodeMessage;
import mayfly.core.base.model.CommonCodeEnum;
import mayfly.core.util.CollectionUtils;
import mayfly.core.util.StringUtils;

import java.util.Collection;
import java.util.Objects;
import java.util.function.Supplier;

/**
 * 业务断言类，不满足断言条件的都抛{@link BizRuntimeException}（默认错误码为{@link CommonCodeEnum#PARAM_ERROR}）
 *
 * @author meilin.huang
 * @version 1.0
 * @date 2019-07-14 18:24
 */
public final class BizAssert {

    public static CodeMessage defaultError = CommonCodeEnum.PARAM_ERROR;

    /**
     * 设置默认错误消息，可根据不同模块设置不同错误码，方便问题溯源
     *
     * @param defaultError 默认错误码
     */
    public static void setDefaultError(CodeMessage defaultError) {
        defaultError = defaultError;
    }

    private BizAssert() {
    }

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

    /**
     * 断言对象不为空
     *
     * @param object   对象
     * @param supplier 错误消息供应器
     */
    public static void notNull(Object object, Supplier<String> supplier) {
        if (object == null) {
            throw newBizRuntimeException(supplier.get());
        }
    }

    /**
     * 断言对象不能为空
     *
     * @param object    对象
     * @param errorEnum 错误枚举值
     */
    public static void notNull(Object object, CodeMessage errorEnum, Object... params) {
        if (object == null) {
            throw newBizRuntimeException(errorEnum, params);
        }
    }

    /**
     * 断言对象为空
     *
     * @param object 对象
     * @param msg    不满足断言的异常信息
     */
    public static void isNull(Object object, String msg) {
        isTrue(object == null, msg);
    }

    /**
     * 断言对象为空
     *
     * @param object   obj
     * @param supplier 错误消息提供器
     */
    public static void isNull(Object object, Supplier<String> supplier) {
        isTrue(object == null, supplier);
    }

    /**
     * 断言对象为空
     *
     * @param object    obj
     * @param errorEnum 错误枚举
     */
    public static void isNull(Object object, CodeMessage errorEnum, Object... params) {
        isTrue(object == null, errorEnum, params);
    }


    /**
     * 断言字符串不为空
     *
     * @param str 字符串
     * @param msg 不满足断言的异常信息
     */
    public static void notEmpty(String str, String msg) {
        isTrue(!StringUtils.isEmpty(str), msg);
    }

    /**
     * 断言字符串不为空
     *
     * @param str      字符串
     * @param supplier 错误消息供应器
     */
    public static void notEmpty(String str, Supplier<String> supplier) {
        isTrue(!StringUtils.isEmpty(str), supplier);
    }

    /**
     * 断言字符串不为空
     *
     * @param str       字符串
     * @param errorEnum 错误枚举
     */
    public static void notEmpty(String str, CodeMessage errorEnum, Object... params) {
        isTrue(!StringUtils.isEmpty(str), errorEnum, params);
    }

    /**
     * 断言集合不为空
     *
     * @param collection 集合
     * @param msg        不满足断言的异常信息
     */
    public static void notEmpty(Collection<?> collection, String msg) {
        isTrue(CollectionUtils.isNotEmpty(collection), msg);
    }

    /**
     * 断言集合不为空
     *
     * @param collection 集合
     * @param supplier   不满足断言的异常信息提供者
     */
    public static void notEmpty(Collection<?> collection, Supplier<String> supplier) {
        isTrue(CollectionUtils.isNotEmpty(collection), supplier);
    }

    /**
     * 断言集合不为空
     *
     * @param collection 集合
     * @param errorEnum  错误枚举
     */
    public static void notEmpty(Collection<?> collection, CodeMessage errorEnum, Object... params) {
        isTrue(CollectionUtils.isNotEmpty(collection), errorEnum, params);
    }

    /**
     * 断言集合为空
     *
     * @param collection 集合
     * @param msg        不满足断言的异常信息
     */
    public static void empty(Collection<?> collection, String msg) {
        isTrue(CollectionUtils.isEmpty(collection), msg);
    }

    /**
     * 断言集合为空
     *
     * @param collection 集合
     * @param supplier   不满足断言的异常信息提供器
     */
    public static void empty(Collection<?> collection, Supplier<String> supplier) {
        isTrue(CollectionUtils.isEmpty(collection), supplier);
    }

    /**
     * 断言集合为空
     *
     * @param collection 集合
     * @param errorEnum  错误枚举
     */
    public static void empty(Collection<?> collection, CodeMessage errorEnum, Object... params) {
        isTrue(CollectionUtils.isEmpty(collection), errorEnum);
    }

    /**
     * 断言两个对象必须相等
     *
     * @param o1  对象1
     * @param o2  对象2
     * @param msg 错误消息
     */
    public static void equals(Object o1, Object o2, String msg) {
        isTrue(Objects.equals(o1, o2), msg);
    }

    /**
     * 断言两个对象必须相等
     *
     * @param o1          对象1
     * @param o2          对象2
     * @param msgSupplier 错误消息提供器
     */
    public static void equals(Object o1, Object o2, Supplier<String> msgSupplier) {
        isTrue(Objects.equals(o1, o2), msgSupplier);
    }

    /**
     * 断言两个对象必须相等
     *
     * @param o1        对象1
     * @param o2        对象2
     * @param errorEnum 错误枚举
     */
    public static void equals(Object o1, Object o2, CodeMessage errorEnum, Object params) {
        isTrue(Objects.equals(o1, o2), errorEnum);
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
    public static void isTrue(boolean expression, CodeMessage errorEnum, Object... params) {
        if (!expression) {
            throw newBizRuntimeException(errorEnum, params);
        }
    }

    /**
     * 创建业务运行时异常对象
     *
     * @param msg 异常信息
     * @return 异常
     */
    public static BizRuntimeException newBizRuntimeException(String msg) {
        return new BizRuntimeException(defaultError, msg);
    }

    /**
     * 创建业务运行时异常对象
     *
     * @param errorEnum 错误枚举
     * @return 异常
     */
    public static BizRuntimeException newBizRuntimeException(CodeMessage errorEnum, Object... params) {
        return new BizRuntimeException(errorEnum, params);
    }
}
