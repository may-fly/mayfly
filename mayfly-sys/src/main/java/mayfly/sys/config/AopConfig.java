package mayfly.sys.config;

import mayfly.core.log.LogAspect;
import mayfly.core.log.LogTypeEnum;
import mayfly.core.validation.ParamValidAspect;
import mayfly.sys.module.sys.service.OperationLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author meilin.huang
 * @date 2020-03-01 12:44 下午
 */
@Configuration
public class AopConfig {

    @Autowired
    private OperationLogService logService;

    @ConditionalOnProperty(value = "savelog", havingValue = "false", matchIfMissing = true)
    @Bean
    public LogAspect logAspect() {
        return new LogAspect();
    }

    @ConditionalOnProperty(value = "savelog", havingValue = "true")
    @Bean
    public LogAspect logAspectAndSaveLog() {
        return new LogAspect((type, logMsg) -> {
            logService.asyncLog(logMsg, type == LogTypeEnum.NORMAN ? mayfly.sys.module.sys.enums.LogTypeEnum.SYS_LOG
                    : mayfly.sys.module.sys.enums.LogTypeEnum.ERR_LOG);
        });
    }

    @Bean
    public ParamValidAspect paramValid() {
        return new ParamValidAspect();
    }
}
