package mayfly.sys.config;

import mayfly.core.log.LogContext;
import mayfly.core.log.handler.LogHandler;
import mayfly.core.log.InvokeInfo;
import mayfly.core.log.LogInfo;
import mayfly.sys.module.sys.service.OperationLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author meilin.huang
 * @date 2021-09-17 8:35 下午
 */
@Component
public class DbLogHandler implements LogHandler {

    @Autowired
    private OperationLogService logService;

    @Override
    public void handle(LogInfo metadata, InvokeInfo invokeInfo) {
        logService.asyncLog(LogContext.getDefaultLogMsg(), invokeInfo.getException() != null ? mayfly.sys.module.sys.enums.LogTypeEnum.SYS_LOG
                : mayfly.sys.module.sys.enums.LogTypeEnum.ERR_LOG);
    }
}
