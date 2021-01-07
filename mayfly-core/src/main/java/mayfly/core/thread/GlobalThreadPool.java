package mayfly.core.thread;

import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 全局公共线程池
 *
 * @author meilin.huang
 * @version 1.0
 * @date 2019-11-11 3:32 下午
 */
public class GlobalThreadPool {

    /**
     * 设置为 CPU 最大核心数
     */
    private static final int CORE_SIZE = Runtime.getRuntime().availableProcessors() * 10;

    /**
     * 线程名前缀
     */
    public static String threadNamePrefix = "mayfly-thread-pool";

    /**
     * 线程池
     */
    private static final ThreadPoolExecutor EXECUTOR = ThreadPoolBuilder.newBuilder()
            .threadName(threadNamePrefix)
            .corePoolSize(CORE_SIZE)
            .maxPoolSize(CORE_SIZE)
            .keepAliveTime(2, TimeUnit.HOURS)
            // 允许空闲的核心线程超时关闭
            .allowCoreThreadTimeOut(true)
            .build();

    /**
     * 执行线程
     *
     * @param runnable runnable
     */
    public static void execute(Runnable runnable) {
        EXECUTOR.execute(runnable);
    }

    /**
     * 执行线程
     *
     * @param threadNameSuffix 线程名后缀，用于标识该线程执行的功能作用（在原有线程名基础上加上":threadNameSuffix" ，任务执行结束会恢复原线程名）
     * @param runnable runnable
     */
    public static void execute(String threadNameSuffix, Runnable runnable) {
        EXECUTOR.execute(() -> {
            Thread currentThread = Thread.currentThread();
            String oldName = currentThread.getName();
            currentThread.setName(oldName + ":" + threadNameSuffix);
            try {
                runnable.run();
            } finally {
                currentThread.setName(oldName);
            }
        });
    }

    /**
     * 执行有返回值的异步方法<br>
     * Future代表一个异步执行的操作，通过get()方法可以获得操作的结果，如果异步操作还没有完成，则get()会使当前线程阻塞
     *
     * @param <T>  执行的Task返回值类型
     * @param task {@link Callable}
     * @return Future
     */
    public static <T> Future<T> submit(Callable<T> task) {
        return EXECUTOR.submit(task);
    }

    /**
     * 执行有返回值的异步方法<br>
     * Future代表一个异步执行的操作，通过get()方法可以获得操作的结果，如果异步操作还没有完成，则get()会使当前线程阻塞
     *
     * @param <T>  执行的Task返回值类型
     * @param threadNameSuffix 线程名后缀，用于标识该线程执行的功能作用（在原有线程名基础上加上":threadNameSuffix" ，任务执行结束会恢复原线程名）
     * @param task {@link Callable}
     * @return Future
     */
    public static <T> Future<T> submit(String threadNameSuffix, Callable<T> task) {
        return EXECUTOR.submit(() -> {
            Thread currentThread = Thread.currentThread();
            String oldName = currentThread.getName();
            currentThread.setName(oldName + ":" + threadNameSuffix);
            try {
                return task.call();
            } finally {
                currentThread.setName(oldName);
            }
        });
    }

    /**
     * 执行有返回值的异步方法<br>
     * Future代表一个异步执行的操作，通过get()方法可以获得操作的结果，如果异步操作还没有完成，则get()会使当前线程阻塞
     *
     * @param runnable 可运行对象
     * @return {@link Future}
     */
    public static Future<?> submit(Runnable runnable) {
        return EXECUTOR.submit(runnable);
    }
}
