package mayfly.sys.config;

import mayfly.core.web.DefaultGlobalExceptionHandler;
import mayfly.core.permission.PermissionInterceptor;
import mayfly.core.web.ResponseAdvice;
import mayfly.sys.config.sign.SignInterceptor;
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
public class WebConfig implements WebMvcConfigurer {

    @Autowired
    private PermissionService permissionService;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new SignInterceptor()).addPathPatterns("/**");
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
     * 全局异常处理器以及统一结果返回
     *
     * @return GlobalExceptionHandler
     */
    @Bean
    public DefaultGlobalExceptionHandler globalExceptionHandler() {
        return new DefaultGlobalExceptionHandler();
    }

    /**
     * 统一包装成功返回结果集
     *
     * @return response advice
     */
    @Bean
    public ResponseAdvice responseAdvice() {
        return new ResponseAdvice();
    }
}
