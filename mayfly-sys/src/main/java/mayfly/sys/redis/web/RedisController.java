package mayfly.sys.redis.web;

import mayfly.common.result.Result;
import mayfly.entity.Redis;
import mayfly.sys.redis.connection.RedisInfo;
import mayfly.sys.redis.service.RedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author meilin.huang
 * @version 1.0
 * @date 2019-01-21 3:35 PM
 */
@RestController
@RequestMapping("/open/redis")
public class RedisController {
    @Autowired
    private RedisService redisService;

    @GetMapping("/list")
    public Result redisList() {
        return Result.success().withData(redisService.listByCondition(Redis.builder().clusterId(RedisInfo.STANDALONE).build()));
    }
}
