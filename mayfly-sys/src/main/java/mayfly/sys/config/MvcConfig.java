package mayfly.sys.config;

import mayfly.core.exception.DefaultGlobalExceptionHandler;
import mayfly.core.permission.PermissionInterceptor;
import mayfly.sys.module.sys.service.PermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * mvc配置
 *
 * @author hml
 * @date 2018/6/27 下午3:52
 */
@Configuration
public class MvcConfig implements WebMvcConfigurer {

    @Autowired
    private PermissionService permissionService;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new PermissionInterceptor(permissionService))
                .addPathPatterns("/sys/**", "/devops/**");
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedHeaders("*")
                .allowedMethods("*")
                .allowedOrigins("*")
                .allowCredentials(true);
    }

    /**
     * 全局异常处理器
     * @return GlobalExceptionHandler
     */
    @Bean
    public DefaultGlobalExceptionHandler globalExceptionHandler() {
        return new DefaultGlobalExceptionHandler();
    }
}
