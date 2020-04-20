package mayfly.core.thread;

import mayfly.core.util.Assert;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicLong;

/**
 * 线程工厂对象建造者
 *
 * @author meilin.huang
 * @version 1.0
 * @date 2019-07-09 13:25
 */
public class ThreadFactoryBuilder {

    /**
     * 线程名（会自动在线程名后面加上-%d以标识线程，如name = rpc-thread；<br/>
     * 那么创建出来的线程名就类似rpc-thread-1，rpc-thread-2等）
     */
    private final String name;

    private Boolean daemon;

    private Integer priority;

    private Thread.UncaughtExceptionHandler uncaughtExceptionHandler;


    private ThreadFactoryBuilder(String name) {
        this.name = name;
    }

    /**
     * 线程名
     *
     * @param name 线程名（会自动在线程名后面加上-%d以标识线程，如name = rpc-thread；<br/>
     *             那么创建出来的线程名就类似rpc-thread-1，rpc-thread-2等）
     * @return thread factory builder
     */
    public static ThreadFactoryBuilder newBuilder(String name) {
        return new ThreadFactoryBuilder(name);
    }

    public ThreadFactoryBuilder daemon(boolean daemon) {
        this.daemon = daemon;
        return this;
    }

    public ThreadFactoryBuilder priority(Integer priority) {
        this.priority = priority;
        return this;
    }

    public ThreadFactoryBuilder uncaughtExceptionHandler(Thread.UncaughtExceptionHandler ueh) {
        this.uncaughtExceptionHandler = ueh;
        return this;
    }

    public ThreadFactory build() {
        Assert.notEmpty(name, "线程名不能为空");
        final AtomicLong count = new AtomicLong(1);
        // return new ThreadFactory
        return r -> {
            Thread t = new Thread(r);
            t.setName(String.format(name + "-%d", count.getAndIncrement()));
            if (this.daemon != null) {
                t.setDaemon(daemon);
            }
            if (this.priority != null) {
                t.setPriority(priority);
            }
            if (this.uncaughtExceptionHandler != null) {
                t.setUncaughtExceptionHandler(uncaughtExceptionHandler);
            }
            return t;
        };
    }
}
