package mayfly.sys.config;

import mayfly.core.exception.BusinessException;
import mayfly.core.exception.BusinessRuntimeException;
import mayfly.core.result.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @author meilin.huang
 * @version 1.0
 * @date 2018-12-18 11:03 AM
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    private static Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(Exception.class)
    public Result handleException(Exception e) {
        // 如果是业务逻辑异常则无需记录日志
        if (e instanceof BusinessException || e instanceof BusinessRuntimeException) {
            return Result.error(e.getMessage());
        }
        if (e instanceof HttpRequestMethodNotSupportedException) {
            return Result.serverError("url请求方法错误！");
        }

        log.error("系统异常：", e);
        return Result.serverError();
    }
}
