package mayfly.core.log.handler;

import mayfly.core.log.InvokeLog;

/**
 * @author meilin.huang
 * @date 2021-09-17 7:25 下午
 */
public interface LogHandler {

    /**
     * 日志处理
     *
     * @param invokeLog 方法调用日志信息
     */
    void handle(InvokeLog invokeLog);
}
