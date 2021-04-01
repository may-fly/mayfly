package mayfly.core.web;

import mayfly.core.model.result.CommonCodeEnum;
import mayfly.core.model.result.Result;
import mayfly.core.exception.BizException;
import mayfly.core.util.ThrowableUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

/**
 * @author meilin.huang
 * @version 1.0
 * @date 2018-12-18 11:03 AM
 */
@RestControllerAdvice
public class DefaultGlobalExceptionHandler {

    private static final Logger LOG = LoggerFactory.getLogger(DefaultGlobalExceptionHandler.class);

    /************  统一异常处理 ***********/
    
    @ExceptionHandler(BizException.class)
    public Result<?> handleBusinessException(BizException e) {
        // 只记录与本系统相关的类调用堆栈信息
        LOG.error("业务异常：{}", ThrowableUtils.getStackTraceByPn(e, "mayfly."));
        return Result.of(e.getErrorCode(), e.getMessage());
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
            return CommonCodeEnum.SERVER_ERROR.toResult();
        }
        return CommonCodeEnum.PARAM_ERROR.toResult(fieldError.getDefaultMessage());
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
            return CommonCodeEnum.SERVER_ERROR.toResult();
        }
        return CommonCodeEnum.PARAM_ERROR.toResult(fieldError.getDefaultMessage());
    }

    @ExceptionHandler(Exception.class)
    public Result<?> handleException(Exception e) {
        CommonCodeEnum paramError = CommonCodeEnum.PARAM_ERROR;
        if (e instanceof HttpRequestMethodNotSupportedException) {
            return paramError.toResult("request method error");
        }
        if (e instanceof MethodArgumentTypeMismatchException) {
            return paramError.toResult("param type mismatch");
        }
        if (e instanceof MissingServletRequestParameterException) {
            return paramError.toResult("param not present");
        }
        if (e instanceof HttpMediaTypeNotSupportedException) {
            return paramError.toResult(e.getMessage());
        }
        if (e instanceof HttpMessageNotReadableException) {
            LOG.error("参数解析错误：", e);
            return paramError.toResult("param parse error");
        }
        // 记录未知异常日志
        LOG.error("系统异常：", e);
        return CommonCodeEnum.SERVER_ERROR.toResult();
    }

}
