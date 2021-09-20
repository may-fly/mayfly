package mayfly.core.log;

import mayfly.core.log.annotation.EnableLog;
import mayfly.core.log.handler.DefaultLogHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportAware;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.type.AnnotationMetadata;

/**
 * @author meilin.huang
 * @date 2021-09-20 1:48 下午
 */
@Configuration(proxyBeanMethods = false)
public class LogProxyAutoConfiguration implements ImportAware {

    private static final Logger LOG = LoggerFactory.getLogger(LogProxyAutoConfiguration.class);

    private AnnotationAttributes enableLog;

    @Bean
    public LogAspect logAspect() {
        if (enableLog.getBoolean("useDefaultHandler")) {
            return new LogAspect(new DefaultLogHandler());
        }
        return new LogAspect();
    }

    @Override
    public void setImportMetadata(AnnotationMetadata importMetadata) {
        this.enableLog = AnnotationAttributes.fromMap(
                importMetadata.getAnnotationAttributes(EnableLog.class.getName(), false));
        if (this.enableLog == null) {
            LOG.warn("@EnableLog is not present on importing class");
        }
    }
}
