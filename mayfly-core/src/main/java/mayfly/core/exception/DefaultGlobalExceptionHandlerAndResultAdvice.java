package mayfly.core.exception;

import mayfly.core.base.model.PageResult;
import mayfly.core.base.model.Response2Result;
import mayfly.core.base.model.Result;
import mayfly.core.util.JsonUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import java.lang.reflect.Method;

/**
 * @author meilin.huang
 * @version 1.0
 * @date 2018-12-18 11:03 AM
 */
@RestControllerAdvice
public class DefaultGlobalExceptionHandlerAndResultAdvice implements ResponseBodyAdvice<Object> {

    private static final Logger LOG = LoggerFactory.getLogger(DefaultGlobalExceptionHandlerAndResultAdvice.class);

    /**
     * 若方法或方法声明类有{@link Response2Result}注解，则将其reponse body封装为{@link Result}统一结果对象
     *
     * @param methodParameter method parameter
     * @param converterType   converterType
     * @return ture: 调用beforeBodyWrite false: 不调用
     */
    @Override
    public boolean supports(MethodParameter methodParameter, Class<? extends HttpMessageConverter<?>> converterType) {
        Method method;
        return methodParameter.getDeclaringClass().isAnnotationPresent(Response2Result.class)
                || ((method = methodParameter.getMethod()) != null && method.isAnnotationPresent(Response2Result.class));
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
            return JsonUtils.toJSONString(Result.success(body));
        }
        return Result.success(body);
    }


    /************  统一异常处理 ***********/

    @ExceptionHandler(BizRuntimeException.class)
    public Result<?> handleBusinessRuntimeException(BizRuntimeException e) {
        // 如果是业务逻辑异常则无需记录日志，错误提示即可
        return Result.error(e.getErrorCode(), e.getMessage());
    }

    @ExceptionHandler(BizException.class)
    public Result<?> handleBusinessException(BizException e) {
        // 如果是业务逻辑异常则无需记录日志，错误提示即可
        return Result.error(e.getErrorCode(), e.getMessage());
    }

    /**
     * 参数校验错误异常
     *
     * @param e 异常
     * @return 结果
     */
    @ExceptionHandler(BindException.class)
    public Object validExceptionHandler(BindException e) {
        FieldError fieldError = e.getBindingResult().getFieldError();
        if (fieldError == null) {
            return Result.serverError();
        }
        return Result.paramError(fieldError.getDefaultMessage());
    }

    /**
     * 参数校验错误异常
     *
     * @param e 异常
     * @return 结果
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Object validExceptionHandler(MethodArgumentNotValidException e) {
        FieldError fieldError = e.getBindingResult().getFieldError();
        if (fieldError == null) {
            return Result.serverError();
        }
        return Result.paramError(fieldError.getDefaultMessage());
    }

    @ExceptionHandler(Exception.class)
    public Result<?> handleException(Exception e) {
        if (e instanceof HttpRequestMethodNotSupportedException) {
            return Result.serverError("request method error");
        }
        if (e instanceof MethodArgumentTypeMismatchException) {
            return Result.paramError("param type mismatch");
        }
        if (e instanceof MissingServletRequestParameterException) {
            return Result.paramError("param not present");
        }
        if (e instanceof HttpMediaTypeNotSupportedException) {
            return Result.paramError(e.getMessage());
        }
        if (e instanceof HttpMessageNotReadableException) {
            LOG.error("参数解析错误：", e);
            return Result.paramError("param parse error");
        }
        // 记录未知异常日志
        LOG.error("系统异常：", e);
        return Result.serverError();
    }

}
