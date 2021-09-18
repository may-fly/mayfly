package mayfly.sys.config;

import mayfly.core.log.LogAspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author meilin.huang
 * @date 2020-03-01 12:44 下午
 */
@Configuration(proxyBeanMethods = false)
public class AopConfig {

    @Autowired
    private DbLogHandler dbLogHandler;

    @ConditionalOnProperty(value = "savelog", havingValue = "false", matchIfMissing = true)
    @Bean
    public LogAspect logAspect() {
        return new LogAspect();
    }

    @ConditionalOnProperty(value = "savelog", havingValue = "true")
    @Bean
    public LogAspect logAspectAndSaveLog() {
        LogAspect la = new LogAspect();
        la.addLogHandler(dbLogHandler);
        return la;
    }
}
