package mayfly.sys.web;

import mayfly.common.result.Result;
import mayfly.sys.service.OperationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author meilin.huang
 * @version 1.0
 * @date 2018-12-12 4:11 PM
 */
@RestController
@RequestMapping("/sys")
public class OperationController {

    @Autowired
    private OperationService operationService;

    @GetMapping("/v1/operations")
    public Result getOperations(String token, Integer menuId) {
        Integer userId = 1;
        return null;
    }
}
