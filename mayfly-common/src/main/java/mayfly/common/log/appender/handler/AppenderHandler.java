package mayfly.common.log.appender.handler;

import mayfly.common.log.appender.LogVO;

/**
 * @author meilin.huang
 * @version 1.0
 * @date 2018-12-29 5:28 PM
 */
@FunctionalInterface
public interface AppenderHandler {

    /**
     * 日志对象处理器
     * @param log
     */
    void handler(LogVO log);
}
