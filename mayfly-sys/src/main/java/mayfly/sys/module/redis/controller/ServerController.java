package mayfly.sys.module.redis.controller;

import mayfly.core.log.MethodLog;
import mayfly.core.permission.Permission;
import mayfly.core.result.Result;
import mayfly.core.validation.annotation.Valid;
import mayfly.sys.module.redis.entity.Redis;
import mayfly.sys.common.utils.BeanUtils;
import mayfly.sys.redis.commands.ServerCommand;
import mayfly.sys.redis.enums.RedisConfEnum;
import mayfly.sys.module.redis.service.RedisService;
import mayfly.sys.module.redis.controller.form.RedisForm;
import mayfly.sys.module.redis.controller.vo.RedisConfParamVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author meilin.huang
 * @version 1.0
 * @date 2018-12-19 2:27 PM
 */
@Permission(code = "redis:")
@MethodLog
@RestController
@RequestMapping("/devops/redis")
public class ServerController {
    @Autowired
    private RedisService redisService;

    private AtomicInteger tempId = new AtomicInteger(0);

    @GetMapping()
    public Result list(RedisForm query) {
        return Result.success(redisService.listByCondition(BeanUtils.copyProperties(query, Redis.class)));
    }

    @PostMapping("/redis")
    public Result saveConnect(@Valid RedisForm redisForm) {
        Redis redis = BeanUtils.copyProperties(redisForm, Redis.class);
        LocalDateTime now = LocalDateTime.now();
        redis.setCreateTime(now);
        redis.setUpdateTime(now);
        redisService.save(redis);
        // 连接该redis
//        redisService.connect(false, redis.getId());
        return Result.success().with(redis);
    }

    @DeleteMapping("/{id}/remove")
    public Result remove(@PathVariable Integer id) {
//        RedisHandler.remove(id);
        return Result.success();
    }

    @PostMapping("/{id}/close")
    public Result close(@PathVariable Integer id) {
//        RedisHandler.close(id);
        return Result.success();
    }

    @PostMapping("/{id}/flushdb")
    public Result flushdb(@PathVariable Integer id) {
//        RedisHandler.getCommands(id).flushdb();
        return Result.success();
    }

    @GetMapping("/{id}/info")
    public Result info(@PathVariable Integer id) {
        return Result.success(ServerCommand.info(redisService.getCmds(id)));
    }

    @GetMapping("/{id}/conf")
    public Result getConf(@PathVariable Integer id) {
        return Result.success(ServerCommand.getConf(redisService.getCmds(id)));
    }

    @PutMapping("/{id}/conf")
    public Result setConf(@PathVariable Integer id, RedisConfParamVO redisConfParam) {
        ServerCommand.configSetAndRewrite(id, RedisConfEnum.getByParam(redisConfParam.getParam()), redisConfParam.getValue());
        return Result.success();
    }

}
