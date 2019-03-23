package mayfly.common.utils;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author meilin.huang
 * @version 1.0
 * @date 2019-01-15 3:35 PM
 */
public class ScheduleUtils {

    public static final int corePoolSize = 1;

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
            x.cancel(true);
        });
    }

    public static void shutdownNow() {
        schedule.shutdownNow();
        scheduledFutureMap.clear();
    }


    public static void main(String[] args) throws Exception {
        AtomicInteger index = new AtomicInteger(1);
        String id = "1";
        for (int i = 0; i < 100; i++) {
            schedule(String.valueOf(index.getAndIncrement()), ()-> {

                System.out.println("哈哈1...");
//                cancelAndRemove("1");
            },5, TimeUnit.SECONDS);
        }
//        cancelAndRemove(id);
//        scheduleAtFixedRate("2", ()-> {
//            System.out.println("哈哈2...");
//        }, 1,1, TimeUnit.SECONDS);
//        scheduleAtFixedRate("3", ()-> {
//            System.out.println("哈哈3...");
//        }, 1,1, TimeUnit.SECONDS);
//        scheduleAtFixedRate("4", ()-> {
//            System.out.println("哈哈4...");
//        }, 1,1, TimeUnit.SECONDS);
//        scheduleAtFixedRate("5", ()-> {
//            System.out.println("哈哈5...");
//        }, 1,1, TimeUnit.SECONDS);
//        scheduleAtFixedRate("6", ()-> {
//            System.out.println("哈哈6...");
//        }, 1,1, TimeUnit.SECONDS);

//        System.out.println(scheduledFutureMap.get("1").isCancelled());
    }

}
