package mayfly.sys.module.redis.controller;

import mayfly.core.log.MethodLog;
import mayfly.core.permission.Permission;
import mayfly.core.result.Result;
import mayfly.core.util.bean.BeanUtils;
import mayfly.core.validation.annotation.Valid;
import mayfly.sys.module.redis.controller.form.RedisForm;
import mayfly.sys.module.redis.controller.vo.RedisConfParamVO;
import mayfly.sys.module.redis.entity.RedisDO;
import mayfly.sys.module.redis.service.RedisService;
import mayfly.sys.module.redis.commands.ServerCommand;
import mayfly.sys.module.redis.enums.RedisConfEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

    @MethodLog(value = "获取redis列表", level = MethodLog.LogLevel.DEBUG)
    @GetMapping()
    public Result<?> list(RedisForm query) {
        return Result.success(redisService.listByCondition(BeanUtils.copyProperties(query, RedisDO.class)));
    }

    @PostMapping()
    public Result<?> save(@RequestBody @Valid RedisForm redisForm) {
       redisService.saveNode(redisForm);
       return Result.success();
    }

    @PutMapping("/{id}")
    public Result<?> update(@PathVariable Integer id, @RequestBody @Valid RedisForm redisForm) {
        redisForm.setId(id);
        redisService.saveNode(redisForm);
        return Result.success();
    }

    @DeleteMapping("/{id}")
    public Result<?> remove(@PathVariable Integer id) {
       redisService.delete(id);
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
