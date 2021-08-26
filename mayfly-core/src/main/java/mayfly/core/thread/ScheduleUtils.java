package mayfly.core.thread;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;


/**
 * @author meilin.huang
 * @version 1.0
 * @date 2019-01-15 3:35 PM
 */
public class ScheduleUtils {

    private static final int CORE_POOL_SIZE = 2;

    /**
     * 定时任务线程池
     */
    private static final ScheduledExecutorService SCHEDULE = Executors.newScheduledThreadPool(CORE_POOL_SIZE
            , ThreadFactoryBuilder.newBuilder("mayfly-schedule").daemon(true).build());

    /**
     * 存定时任务结果
     */
    private static final Map<String, ScheduledFuture<?>> scheduledFutureMap = new ConcurrentHashMap<>();

    static {
        //定期检查map中是否有已经执行完成的，有则移除
        scheduleAtFixedRate("removeCompletedFuture", () -> {
            scheduledFutureMap.forEach((key, value) -> {
                if (value.isDone() || value.isCancelled()) {
                    scheduledFutureMap.remove(key);
                }
            });
        }, 0, 30, TimeUnit.SECONDS);
    }


    /**
     * 倒计时执行
     *
     * @param id
     * @param runnable
     * @param time
     * @param timeUnit
     */
    public static void schedule(String id, Runnable runnable, long time, TimeUnit timeUnit) {
        scheduledFutureMap.put(id, SCHEDULE.schedule(runnable, time, timeUnit));
    }

    /**
     * 周期性定时执行
     *
     * @param id
     * @param runnable
     * @param time
     * @param timeUnit
     */
    public static void scheduleAtFixedRate(String id, Runnable runnable, long initialDelay, long time, TimeUnit timeUnit) {
        scheduledFutureMap.put(id, SCHEDULE.scheduleAtFixedRate(runnable, initialDelay, time, timeUnit));
    }

    public static boolean containSchedule(String id) {
        ScheduledFuture<?> scheduledFuture = scheduledFutureMap.get(id);
        return scheduledFuture != null && !scheduledFuture.isDone();
    }

    /**
     * 取消并移除该定时任务
     *
     * @param id 任务id
     */
    public static void cancel(String id) {
        Optional.ofNullable(scheduledFutureMap.get(id)).ifPresent(x -> {
            if (!x.isDone()) {
                x.cancel(true);
            }
            scheduledFutureMap.remove(id);
        });
    }

    public static void shutdownNow() {
        SCHEDULE.shutdownNow();
        scheduledFutureMap.clear();
    }
}
