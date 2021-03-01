package mayfly.sys.module.redis.commands;

import io.lettuce.core.api.sync.RedisCommands;
import io.lettuce.core.api.sync.RedisServerCommands;
import mayfly.core.exception.BizAssert;
import mayfly.core.util.Assert;
import mayfly.core.util.StringUtils;
import mayfly.sys.module.redis.connection.RedisConnectionRegistry;
import mayfly.sys.module.redis.enums.RedisConfEnum;
import mayfly.sys.module.redis.enums.RedisInfoEnum;
import mayfly.sys.module.redis.parser.RedisInfoParser;

import java.util.HashMap;
import java.util.Map;

/**
 * @author meilin.huang
 * @version 1.0
 * @date 2019-01-18 9:23 AM
 */
public class ServerCommand {

    private static RedisConnectionRegistry register = RedisConnectionRegistry.getInstance();

    /**
     * 获取redis命令操作
     *
     * @param redisId
     * @return
     */
    public static RedisCommands<String, byte[]> getCmds(long redisId) {
        return register.getCmds(redisId);
    }

    /**
     * 获取redis info信息
     *
     * @param serverCommands 命令操作对象
     * @return
     */
    public static Map<RedisInfoEnum, Map<String, Object>> info(RedisServerCommands serverCommands) {
        return RedisInfoParser.parse(serverCommands.info());
    }

    public static long dbsize(RedisServerCommands serverCommands) {
        return serverCommands.dbsize();
    }

    /**
     * 获取 redis.conf的配置信息
     *
     * @param redisId
     * @return
     */
    public static Map<String, String> getConf(RedisServerCommands<String, byte[]> commands) {
        Map<String, String> result = new HashMap<>();
//        for (RedisConfEnum confParam : RedisConfEnum.values()) {
//            result.putAll(cmds.configGet(confParam.parameter));
//        }

        return commands.configGet("*");
    }

    /**
     * 设置并保存redis指定配置
     *
     * @param redisId
     * @param param
     * @param value
     */
    public static void configSetAndRewrite(long redisId, RedisConfEnum param, String value) {
        Assert.notNull(param, "配置文件的参数值不能为空！");

        if (!StringUtils.isEmpty(param.valuePattern)) {
            if (!value.matches(param.valuePattern)) {
                throw BizAssert.newException("value值不正确！");
            }
        }
        RedisCommands cmds = getCmds(redisId);
        cmds.configSet(param.parameter, value);
        cmds.configRewrite();
    }

}
