package mayfly.sys.web.redis;

import mayfly.core.result.Result;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author meilin.huang
 * @version 1.0
 * @date 2019-04-02 9:42 AM
 */
@RequestMapping("/open/machines")
@RestController
public class MachineController {

    @PostMapping()
    public Result save(){
        return Result.success();
    }
}
