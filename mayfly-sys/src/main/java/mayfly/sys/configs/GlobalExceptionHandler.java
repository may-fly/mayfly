package mayfly.sys.configs;

import mayfly.common.exception.BusinessException;
import mayfly.common.result.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
        log.error("异常：", e);
        if (e instanceof BusinessException) {
            return Result.paramError(e.getMessage());
        }

        return Result.serverError();
    }
}
