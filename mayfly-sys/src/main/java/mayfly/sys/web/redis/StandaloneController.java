package mayfly.sys.web.redis;

import io.lettuce.core.api.sync.RedisCommands;
import mayfly.common.result.Result;
import mayfly.common.validation.annotation.Valid;
import mayfly.sys.redis.commands.KeyCommand;
import mayfly.sys.redis.commands.ServerCommand;
import mayfly.sys.service.redis.RedisService;
import mayfly.sys.web.redis.form.ScanForm;
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

    @GetMapping("/{id}/scan")
    public Result scan(@PathVariable Integer id, @Valid ScanForm scanForm) {
        RedisCommands<String, byte[]> cmds = redisService.getCmds(id);
        KeyScanVO scan = KeyCommand.scan(cmds, scanForm.getCursor(), scanForm.getCount(),  scanForm.getMatch());
        scan.setDbsize(ServerCommand.dbsize(cmds));
        return Result.success().withData(scan);
    }
}
