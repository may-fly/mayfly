package mayfly.core.log.annotation;

import java.lang.annotation.*;

/**
 * 可用于类和方法，用于类则该类的方法都会记录日志 <br/>
 * 如果方法和类都有该注解，res属性则使用方法级的注解属性描述，而value信息则为两则相加 </br>
 * 如果方法的返回值为void则不记录返回结果
 *
 * @author meilin.huang
 * @version 1.0
 * @date 2018-11-06 10:41 AM
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Log {
    /**
     * 日志描述description,如果类和方法该值都为空，则不会有该项日志记录。
     * 可使用spel表达式进行日志描述。如可使用请求参数，或'res'代表返回值。
     * 也可通过{@linkplain mayfly.core.log.LogContext#putVar(String, Object)}方法设置变量，并在spel表达式中使用
     */
    String value() default "";

    /**
     * 是否记录返回结果，默认true
     */
    boolean res() default true;

    /**
     * 是否使用spel表达式
     * 为true则value可设置对应的spel表达式
     */
    boolean el() default false;

    /**
     * 打印日志的级别，默认info级别
     */
    Level level() default Level.INFO;

    /**
     * 只有日志级别为该级别时，才会打印方法返回结果（用于灵活控制避免打印很大的结果列表等）
     */
    Level resLevel() default Level.INFO;

    /**
     * 打印日志的级别
     */
    enum Level {
        /**
         * 不打印日志
         */
        NONE(0),

        DEBUG(1),

        INFO(2),

        WARN(3),

        ERROR(4);

        private final int order;

        Level(int order) {
            this.order = order;
        }

        public int order() {
            return this.order;
        }
    }
}

