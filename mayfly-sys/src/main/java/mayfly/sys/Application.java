package mayfly.sys;

import mayfly.core.exception.BizAssert;
import mayfly.core.log.annotation.EnableLog;
import mayfly.sys.common.error.SysError;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @date 2018/6/7 下午5:21
 */
@EnableLog
@SpringBootApplication
@MapperScan(basePackages = "mayfly.**.mapper")
public class Application {
    public static void main(String[] args) {
        BizAssert.setDefaultErrorCode(SysError.ASSERT_ERROR.getCode());
        SpringApplication.run(Application.class);
    }
}
