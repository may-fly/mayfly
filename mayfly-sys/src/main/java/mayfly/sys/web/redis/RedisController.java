package mayfly.sys.web.redis;

import mayfly.common.result.Result;
import mayfly.entity.Redis;
import mayfly.sys.common.utils.BeanUtils;
import mayfly.sys.service.redis.RedisService;
import mayfly.sys.web.redis.form.RedisForm;
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

    @GetMapping()
    public Result redisList(RedisForm query) {
        return Result.success().withData(redisService.listByCondition(BeanUtils.copyProperties(query, Redis.class)));
    }
}
