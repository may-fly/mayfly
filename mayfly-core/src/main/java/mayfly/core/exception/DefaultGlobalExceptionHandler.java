package mayfly.core.exception;

import mayfly.core.base.model.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @author meilin.huang
 * @version 1.0
 * @date 2018-12-18 11:03 AM
 */
@RestControllerAdvice
public class DefaultGlobalExceptionHandler {

    private static final Logger LOG = LoggerFactory.getLogger(DefaultGlobalExceptionHandler.class);

    @ExceptionHandler(BusinessRuntimeException.class)
    public Result<?> handleBusinessRuntimeException(BusinessRuntimeException e) {
        // 如果是业务逻辑异常则无需记录日志，错误提示即可
        return Result.error(e.getErrorCode(), e.getMessage());
    }

    @ExceptionHandler(BusinessException.class)
    public Result<?> handleBusinessException(BusinessException e) {
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
        // 记录未知异常日志
        LOG.error("系统异常：", e);
        return Result.serverError();
    }

}
