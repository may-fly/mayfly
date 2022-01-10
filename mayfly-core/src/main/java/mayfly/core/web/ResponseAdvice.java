package mayfly.core.web;

import mayfly.core.model.result.NoResponse2Result;
import mayfly.core.model.result.PageResult;
import mayfly.core.model.result.Response2Result;
import mayfly.core.model.result.Result;
import mayfly.core.util.JsonUtils;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import java.lang.reflect.Method;

/**
 * @author meilin.huang
 * @date 2020-12-30 4:43 下午
 */
@RestControllerAdvice
public class ResponseAdvice implements ResponseBodyAdvice<Object> {

    private static final MediaType MEDIA_TYPE = MediaType.parseMediaType(MediaType.APPLICATION_JSON_VALUE);
    
    /**
     * 若方法或方法声明类有{@link Response2Result}注解，则将其reponse body封装为{@link Result}统一结果对象
     *
     * @param methodParameter method parameter
     * @param converterType   converterType
     * @return ture: 调用beforeBodyWrite false: 不调用
     */
    @Override
    public boolean supports(MethodParameter methodParameter, Class<? extends HttpMessageConverter<?>> converterType) {
        Method method = methodParameter.getMethod();
        if (method == null) {
            return true;
        }
        if (method.isAnnotationPresent(NoResponse2Result.class)) {
            return false;
        }
        return methodParameter.getDeclaringClass().isAnnotationPresent(Response2Result.class)
                || method.isAnnotationPresent(Response2Result.class);
    }

    /**
     * 成功结果集统一包装返回{@link Result}
     */
    @Override
    public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType, Class<? extends HttpMessageConverter<?>> selectedConverterType, ServerHttpRequest request, ServerHttpResponse response) {
        if (body instanceof Result) {
            return body;
        }
        if (body instanceof PageResult) {
            return ((PageResult<?>) body).toResult();
        }
        // string特殊处理，返回类型为string 会使用StringHttpMessageConverter会报错
        if (body instanceof String) {
            response.getHeaders().setContentType(MEDIA_TYPE);
            return JsonUtils.toJSONString(Result.success(body));
        }
        return Result.success(body);
    }
}
