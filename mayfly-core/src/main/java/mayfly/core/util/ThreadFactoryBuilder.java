package mayfly.core.util;

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

    private String nameFormat;

    private Boolean daemon;

    private Integer priority;

    private ThreadFactoryBuilder (String nameFormat) {
        this.nameFormat = nameFormat;
    }

    /**
     * 线程名格式化
     * @param nameFormat 可使用%d来代替线程编号 如：thread-%d-xxx
     * @return
     */
    public static ThreadFactoryBuilder nameFormat(String nameFormat) {
        return new ThreadFactoryBuilder(nameFormat);
    }

    public ThreadFactoryBuilder daemon(boolean daemon) {
        this.daemon = daemon;
        return this;
    }

    public ThreadFactory build() {
        final AtomicLong count = !StringUtils.isEmpty(nameFormat) ? new AtomicLong(0) : null;
        return r -> {
            Thread t = new Thread(r);
            if (!StringUtils.isEmpty(nameFormat)) {
                t.setName(String.format(nameFormat, count.getAndIncrement()));
            }
            if (this.daemon != null) {
                t.setDaemon(daemon);
            }
            if (this.priority != null) {
                t.setPriority(priority);
            }
            return t;
        };
    }
}
