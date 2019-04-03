package mayfly.sys.web.redis;

import mayfly.common.result.Result;
import mayfly.common.validation.annotation.Valid;
import mayfly.entity.Redis;
import mayfly.sys.common.utils.BeanUtils;
import mayfly.sys.redis.commands.ServerCommand;
import mayfly.sys.redis.enumration.RedisConfEnum;
import mayfly.sys.service.redis.RedisService;
import mayfly.sys.web.redis.form.RedisForm;
import mayfly.sys.web.redis.vo.RedisConfParamVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author meilin.huang
 * @version 1.0
 * @date 2018-12-19 2:27 PM
 */
@RestController
@RequestMapping("/open")
public class ServerController {
    @Autowired
    private RedisService redisService;

    private AtomicInteger tempId = new AtomicInteger(0);

    @PostMapping("/redis")
    public Result saveConnect(@Valid RedisForm redisForm) {
        Redis redis = BeanUtils.copyProperties(redisForm, Redis.class);
        LocalDateTime now = LocalDateTime.now();
        redis.setCreateTime(now);
        redis.setUpdateTime(now);
        redisService.save(redis);
        // 连接该redis
//        redisService.connect(false, redis.getId());
        return Result.success().withData(redis);
    }

    @DeleteMapping("/redis/{id}/remove")
    public Result remove(@PathVariable Integer id) {
//        RedisHandler.remove(id);
        return Result.success();
    }

    @PostMapping("/redis/{id}/close")
    public Result close(@PathVariable Integer id) {
//        RedisHandler.close(id);
        return Result.success();
    }

    @PostMapping("/redis/{id}/flushdb")
    public Result flushdb(@PathVariable Integer id) {
//        RedisHandler.getCommands(id).flushdb();
        return Result.success();
    }

    @GetMapping("/redis/{id}/info")
    public Result info(@PathVariable Integer id) {
        return Result.success().withData(ServerCommand.info(redisService.getCmds(id)));
    }

    @GetMapping("/redis/{id}/conf")
    public Result getConf(@PathVariable Integer id) {
        return Result.success().withData(ServerCommand.getConf(id));
    }

    @PutMapping("/redis/{id}/conf")
    public Result setConf(@PathVariable Integer id, RedisConfParamVO redisConfParam) {
        ServerCommand.configSetAndRewrite(id, RedisConfEnum.getByParam(redisConfParam.getParam()), redisConfParam.getValue());
        return Result.success();
    }

}
