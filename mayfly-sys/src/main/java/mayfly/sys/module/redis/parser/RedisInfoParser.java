package mayfly.sys.module.redis.parser;

import mayfly.sys.module.redis.enums.RedisInfoEnum;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * redis info信息解析
 *
 * @author meilin.huang
 * @version 1.0
 * @date 2019-01-11 12:48 PM
 */
public final class RedisInfoParser {

    public static Map<RedisInfoEnum, Map<String, Object>> parse(String info) {
        Map<RedisInfoEnum, Map<String, Object>> redisStatMap = new HashMap<RedisInfoEnum, Map<String, Object>>(256);
        String[] data = info.split("\r\n");
        int i = 0;
        int length = data.length;
        while (i < length) {
            if (data[i].contains("#")) {
                String key = data[i].substring(data[i++].indexOf('#') + 1);
                RedisInfoEnum redisInfoEnum = RedisInfoEnum.value(key.trim());
                if (redisInfoEnum == null) {
                    continue;
                }
                Map<String, Object> sectionMap = new LinkedHashMap<>();
                while (i < length && data[i].contains(":")) {
                    String[] pair = data[i++].split(":");
                    if (pair.length != 2) {
                        continue;
                    }
                    sectionMap.put(pair[0], pair[1]);
                }
                redisStatMap.put(redisInfoEnum, sectionMap);
            } else {
                i++;
            }
        }
        return redisStatMap;
    }
}
