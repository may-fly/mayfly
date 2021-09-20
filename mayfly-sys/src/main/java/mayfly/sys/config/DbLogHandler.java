package mayfly.sys.config;

import mayfly.core.log.InvokeLog;
import mayfly.core.log.LogContext;
import mayfly.core.log.handler.LogHandler;
import mayfly.sys.module.sys.enums.LogTypeEnum;
import mayfly.sys.module.sys.service.OperationLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

/**
 * 数据库日志处理器，将日志信息保存至数据库
 *
 * @author meilin.huang
 * @date 2021-09-17 8:35 下午
 */
@ConditionalOnProperty(value = "savelog", havingValue = "true")
@Component
public class DbLogHandler implements LogHandler {

    @Autowired
    private OperationLogService logService;

    @Override
    public void handle(InvokeLog invokeLog) {
        logService.asyncLog(LogContext.getDefaultLogMsg(), invokeLog.getException() != null ? LogTypeEnum.SYS_LOG : LogTypeEnum.ERR_LOG);
    }
}
