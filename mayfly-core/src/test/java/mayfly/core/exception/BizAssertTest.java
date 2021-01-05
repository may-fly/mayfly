package mayfly.core.exception;

import mayfly.core.model.result.CodeMessage;
import org.junit.Test;

public class BizAssertTest {

    enum ErrorCode implements CodeMessage {
        SERVER_ERROR(500, "服务器异常: %s"),

        NOT_FOUND(404, "资源未找到");

        private final Integer code;

        private final String name;

        ErrorCode(Integer code, String name) {
            this.code = code;
            this.name = name;
        }

        @Override
        public String getMessage() {
            return this.name;
        }

        @Override
        public Integer getCode() {
            return this.code;
        }
    }

    @Test
    public void notNull() {
        String test = null;
//        BizAssert.notNull(test, "test不能为空");
//        BizAssert.notNull(test, () -> "test不能为空");
        BizAssert.notNull(test, ErrorCode.SERVER_ERROR, "server error");
    }
}