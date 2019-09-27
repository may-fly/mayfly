package mayfly.common.util;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.*;

/**
 * @author meilin.huang
 * @version 1.0
 * @date 2019-01-15 3:35 PM
 */
public class ScheduleUtils {

    private static int corePoolSize = 1;

    /**
     *  定时任务线程池
     */
    private static ScheduledExecutorService schedule = Executors.newScheduledThreadPool(corePoolSize);

    /**
     * 存定时任务结果
     */
    private static Map<String, ScheduledFuture> scheduledFutureMap = new ConcurrentHashMap<>();

    static {
        //定期检查map中是否有已经执行完成的，有则移除
        scheduleAtFixedRate("removeCompletedFuture", () -> {
            scheduledFutureMap.forEach((key, value) -> {
                if (value.isDone()) {
                    removeFuture(key);
                }
            });
        }, 0, 10, TimeUnit.SECONDS);
    }


    /**
     * 倒计时执行
     * @param id
     * @param runnable
     * @param time
     * @param timeUnit
     */
    public static void schedule(String id, Runnable runnable, long time, TimeUnit timeUnit) {
        scheduledFutureMap.put(id, schedule.schedule(runnable, time, timeUnit));
    }

    /**
     * 周期性定时执行
     * @param id
     * @param runnable
     * @param time
     * @param timeUnit
     */
    public static void scheduleAtFixedRate(String id, Runnable runnable, long initialDelay, long time, TimeUnit timeUnit) {
        scheduledFutureMap.put(id, schedule.scheduleAtFixedRate(runnable, initialDelay, time, timeUnit));
    }

    public static void removeFuture(String id) {
        Optional.ofNullable(scheduledFutureMap.get(id)).ifPresent(x -> {
            scheduledFutureMap.remove(id);
        });
    }

    public static void cancel(String id) {
        Optional.ofNullable(scheduledFutureMap.get(id)).ifPresent(x -> {
            if (!x.isDone()) {
                x.cancel(true);
            }
        });
    }

    public static void shutdownNow() {
        schedule.shutdownNow();
        scheduledFutureMap.clear();
    }
}
