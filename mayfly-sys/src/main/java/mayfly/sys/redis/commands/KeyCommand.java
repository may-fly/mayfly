package mayfly.sys.redis.commands;

import io.lettuce.core.KeyScanCursor;
import io.lettuce.core.ScanArgs;
import io.lettuce.core.ScanCursor;
import io.lettuce.core.api.sync.RedisKeyCommands;
import mayfly.common.utils.StringUtils;
import mayfly.sys.web.redis.vo.KeyInfo;
import mayfly.sys.web.redis.vo.KeyScanVO;

import java.util.stream.Collectors;

/**
 * @author meilin.huang
 * @version 1.0
 * @date 2019-01-21 5:34 PM
 */
public class KeyCommand {

    public static KeyScanVO scan(RedisKeyCommands<String, byte[]> commands, String cursor, int count, String match) {
        if (StringUtils.isEmpty(match)) {
            match = "*";
        }
        ScanArgs args = ScanArgs.Builder.limit(count).match(match);
        ScanCursor c;
        if (StringUtils.isEmpty(cursor)) {
            c = ScanCursor.INITIAL;
        } else {
            c = ScanCursor.of(cursor);
        }
        KeyScanCursor<String> result = commands.scan(c, args);
        return KeyScanVO.builder().keys(result.getKeys().stream()
                .map(k -> KeyInfo.builder().key(k)
                        .ttl(commands.ttl(k))
                        .type(commands.type(k)).build())
                .collect(Collectors.toList())).cursor(result.getCursor()).build();
    }


    public static long ttl(RedisKeyCommands commands, String key) {
        return commands.ttl(key);
    }

    public static String type(RedisKeyCommands commands, String key) {
        return commands.type(key);
    }

    public static Long del(RedisKeyCommands commands, String... keys) {
        return commands.del(keys);
    }
}
