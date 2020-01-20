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
    private static int coreSize = Runtime.getRuntime().availableProcessors();

    /**
     * 线程池
     */
    private static ThreadPoolExecutor executor = ThreadPoolBuilder.newBuilder()
            .threadFactory(ThreadFactoryBuilder.newBuilder("mayfly-thread-pool").build())
            .corePoolSize(coreSize)
            .maxPoolSize(coreSize)
            .keepAliveTime(5, TimeUnit.MINUTES)
            // 允许空闲的核心线程超时关闭
            .allowCoreThreadTimeOut(true)
            .build();

    /**
     * 执行线程
     *
     * @param runnable runnable
     */
    public static void execute(Runnable runnable) {
        executor.execute(runnable);
    }

    /**
     * 执行有返回值的异步方法<br>
     * Future代表一个异步执行的操作，通过get()方法可以获得操作的结果，如果异步操作还没有完成，则get()会使当前线程阻塞
     *
     * @param <T>  执行的Task
     * @param task {@link Callable}
     * @return Future
     */
    public static <T> Future<T> submit(Callable<T> task) {
        return executor.submit(task);
    }


    /**
     * 执行有返回值的异步方法<br>
     * Future代表一个异步执行的操作，通过get()方法可以获得操作的结果，如果异步操作还没有完成，则get()会使当前线程阻塞
     *
     * @param runnable 可运行对象
     * @return {@link Future}
     */
    public static Future<?> submit(Runnable runnable) {
        return executor.submit(runnable);
    }
}
