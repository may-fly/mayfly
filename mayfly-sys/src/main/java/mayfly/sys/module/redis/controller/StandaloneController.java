package mayfly.sys.module.redis.controller;

import mayfly.sys.module.redis.service.RedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author meilin.huang
 * @version 1.0
 * @date 2019-01-19 3:37 PM
 */
@RequestMapping("/open/redis/")
@RestController
public class StandaloneController {

    @Autowired
    private RedisService redisService;

//    @MethodLog(value = "查询redis key")
//    @GetMapping("/{id}/scan")
//    public Result scan(@PathVariable Integer id, @Valid ScanForm scanForm) {
//        RedisCommands<String, byte[]> cmds = redisService.getCmds(id);
//        KeyScanVO scan = KeyValueCommand.scan(cmds, scanForm.getCursor(), scanForm.getCount(),  scanForm.getMatch());
//        return Result.success().with(scan);
//    }
//
//    @MethodLog(value = "查询redis value")
//    @GetMapping("/{id}/value")
//    public Result value(@PathVariable Integer id, String key) {
//        return Result.success().with(KeyValueCommand.value(redisService.getCmds(id), key));
//    }
//
//    @MethodLog(value = "新增key value")
//    @PostMapping("/{id}/value")
//    public Result addKeyValue(@PathVariable Integer id, @Valid KeyValueForm keyValue) {
//        KeyValueCommand.addKeyValue(redisService.getCmds(id), BeanUtils.copyProperties(keyValue, KeyInfo.class));
//        return Result.success();
//    }
}
