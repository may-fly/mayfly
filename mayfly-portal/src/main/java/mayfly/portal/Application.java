package mayfly.portal;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author meilin.huang
 * @version 1.0
 * @date 2018-12-04 2:39 PM
 */
@SpringBootApplication
@MapperScan("mayfly.dao")
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class);
    }
}
