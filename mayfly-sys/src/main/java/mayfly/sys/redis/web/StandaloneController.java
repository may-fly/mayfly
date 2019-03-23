package mayfly.sys.redis.web;

import io.lettuce.core.KeyScanCursor;
import io.lettuce.core.api.sync.RedisCommands;
import mayfly.common.result.Result;
import mayfly.sys.redis.commands.KeyCommand;
import mayfly.sys.redis.commands.ServerCommand;
import mayfly.sys.redis.service.RedisService;
import mayfly.sys.redis.web.vo.KeyScanVO;
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
    public Result scan(@PathVariable Integer id, String match) {
        RedisCommands<String, byte[]> cmds = redisService.getCmds(id);
        KeyScanCursor<String> scan = KeyCommand.scan(cmds, 500,  match);
        KeyScanVO vo = KeyScanVO.builder().cursor(scan.getCursor()).keys(scan.getKeys()).dbsize(ServerCommand.dbsize(cmds)).build();
        return Result.success().withData(vo);
    }
}
