package mayfly.sys.redis.commands;

import io.lettuce.core.KeyScanCursor;
import io.lettuce.core.ScanArgs;
import io.lettuce.core.ScanCursor;
import io.lettuce.core.api.sync.RedisKeyCommands;
import mayfly.common.utils.StringUtils;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author meilin.huang
 * @version 1.0
 * @date 2019-01-21 5:34 PM
 */
public class KeyCommand {

    private static Map<RedisKeyCommands, ScanCursor> scanCursorMap = new ConcurrentHashMap<>();

    public static KeyScanCursor<String> scan(RedisKeyCommands<String, byte[]> commands, int count, String match) {
        if (StringUtils.isEmpty(match)) {
            match = "*";
        }
        ScanArgs args = ScanArgs.Builder.limit(count).match(match);
        ScanCursor c = scanCursorMap.get(commands);
        if (c == null || c.isFinished()) {
            c = ScanCursor.INITIAL;
        }
        KeyScanCursor<String> result = commands.scan(c, args);
        scanCursorMap.put(commands, result);
        return result;
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
