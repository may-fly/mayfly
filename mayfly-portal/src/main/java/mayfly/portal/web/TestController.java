package mayfly.portal.web;

import mayfly.common.result.Result;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author meilin.huang
 * @version 1.0
 * @date 2018-12-04 2:37 PM
 */
@RestController
@RequestMapping("/test")
public class TestController {

    @GetMapping("/v1")
    public Result test() {
        return Result.success();
    }
}
