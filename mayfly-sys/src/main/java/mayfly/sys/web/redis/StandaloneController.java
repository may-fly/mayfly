package mayfly.sys.web.redis;

import io.lettuce.core.api.sync.RedisCommands;
import mayfly.common.result.Result;
import mayfly.sys.redis.commands.KeyCommand;
import mayfly.sys.redis.commands.ServerCommand;
import mayfly.sys.service.redis.RedisService;
import mayfly.sys.web.redis.vo.KeyScanVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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

    @PostMapping("/{id}/connect")
    public Result connect(@PathVariable Integer id) {
        redisService.connect(false, id);
        return Result.success().withData(ServerCommand.info(redisService.getCmds(id)));
    }

    @GetMapping("/{id}/scan")
    public Result scan(@PathVariable Integer id, String cursor, Integer count, String match) {
        RedisCommands<String, byte[]> cmds = redisService.getCmds(id);
        KeyScanVO scan = KeyCommand.scan(cmds, cursor,count,  match);
        scan.setDbsize(ServerCommand.dbsize(cmds));
        return Result.success().withData(scan);
    }
}
