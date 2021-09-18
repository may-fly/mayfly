package mayfly.core.log.handler;

import mayfly.core.log.InvokeInfo;
import mayfly.core.log.LogInfo;

/**
 * @author meilin.huang
 * @date 2021-09-17 7:25 下午
 */
public interface LogHandler {

    /**
     * 日志处理
     *
     * @param metadata   日志信息
     * @param invokeInfo 方法调用信息
     */
    void handle(LogInfo metadata, InvokeInfo invokeInfo);
}
