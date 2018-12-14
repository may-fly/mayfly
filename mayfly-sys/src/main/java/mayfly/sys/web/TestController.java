package mayfly.sys.web;

import mayfly.common.log.MethodLog;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Description: 测试
 * @author: hml
 * @date: 2018/6/7 下午5:00
 */
@RestController
@RequestMapping("/sys")
public class TestController {

    @MethodLog
    @GetMapping("/v1/{id}")
    public String test(@PathVariable Integer id) {
        return id.toString();
    }

    @MethodLog
    @GetMapping("/v2/{id}")
    public String test2(@PathVariable Integer id) {
        return id.toString();
    }
}
