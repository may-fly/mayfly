package mayfly.core.exception;

import mayfly.core.base.model.Result;
import mayfly.core.util.enums.NameValueEnum;
import org.junit.Test;

import static org.junit.Assert.*;

public class BizAssertTest {

    enum ErrorCode implements NameValueEnum<Integer> {
        SERVER_ERROR(500, "服务器异常"),

        NOT_FOUND(404, "资源未找到");

        private final Integer code;

        private final String name;

        ErrorCode(Integer code, String name) {
            this.code = code;
            this.name = name;
        }

        @Override
        public String getName() {
            return this.name;
        }

        @Override
        public Integer getValue() {
            return this.code;
        }
    }

    @Test
    public void notNull() {
        String test = null;
//        BizAssert.notNull(test, "test不能为空");
//        BizAssert.notNull(test, () -> "test不能为空");
        BizAssert.notNull(test, ErrorCode.SERVER_ERROR);
    }
}