package mayfly.sys.config;

import mayfly.core.log.LogAspect;
import mayfly.core.validation.ParamValidAspect;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author meilin.huang
 * @date 2020-03-01 12:44 下午
 */
@Configuration
public class AopConfig {

    @Bean
    public LogAspect logAspect() {
        return new LogAspect();
    }

    @Bean
    public ParamValidAspect paramValid() {
        return new ParamValidAspect();
    }
}
