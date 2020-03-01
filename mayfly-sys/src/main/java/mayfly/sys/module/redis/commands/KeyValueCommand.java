package mayfly.sys.module.redis.commands;

import io.lettuce.core.KeyScanCursor;
import io.lettuce.core.ScanArgs;
import io.lettuce.core.ScanCursor;
import io.lettuce.core.api.sync.*;
import mayfly.core.exception.BusinessRuntimeException;
import mayfly.core.util.enums.EnumUtils;
import mayfly.core.util.JsonUtils;
import mayfly.core.util.StringUtils;
import mayfly.sys.module.redis.enums.RedisValueTypeEnum;
import mayfly.sys.module.redis.controller.vo.KeyScanVO;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * @author meilin.huang
 * @version 1.0
 * @date 2019-01-21 5:34 PM
 */
public class KeyValueCommand {

    private static Map<RedisKeyCommands, ScanCursor> clusterScan = new ConcurrentHashMap<>(32);

    /**
     * 单机scan
     *
     * @param commands
     * @param cursor
     * @param count
     * @param match
     * @return
     */
    public static KeyScanVO scan(RedisKeyCommands<String, byte[]> commands, String cursor, int count, String match) {
        if (StringUtils.isEmpty(match)) {
            match = "*";
        }
        ScanArgs args = ScanArgs.Builder.limit(count).match(match);
        ScanCursor c = StringUtils.isEmpty(cursor) ? ScanCursor.INITIAL : ScanCursor.of(cursor);
        KeyScanCursor<String> result = commands.scan(c, args);
        return KeyScanVO.builder().keys(result.getKeys().stream()
                .map(k -> KeyInfo.builder().key(k)
                        .ttl(commands.ttl(k))
                        .type(EnumUtils.getValueByName(RedisValueTypeEnum.values(), commands.type(k)))
                        .build()).collect(Collectors.toList()))
                .cursor(result.getCursor()).dbsize(((RedisServerCommands) commands).dbsize()).build();
    }

    /**
     * 集群scan,需要复用上一个ScanCursor
     *
     * @param commands
     * @param count
     * @param match
     * @return
     */
    public static KeyScanVO clusterScan(RedisKeyCommands<String, byte[]> commands, int count, String match) {
        if (StringUtils.isEmpty(match)) {
            match = "*";
        }
        ScanArgs args = ScanArgs.Builder.limit(count).match(match);
        Long dbsize = castServerCommands(commands).dbsize();
        Set<String> keys = getClusterKeys(commands, args);
        int i = 0;
        while (keys.size() < dbsize && keys.size() < count) {
            keys.addAll(getClusterKeys(commands, args));
            if (++i == 3) {
                break;
            }
        }
        return KeyScanVO.builder().keys(keys.stream()
                .map(k -> KeyInfo.builder().key(k)
                        .ttl(commands.ttl(k))
                        .type(EnumUtils.getValueByName(RedisValueTypeEnum.values(), commands.type(k)))
                        .build()).collect(Collectors.toList())).dbsize(dbsize).build();
    }

    private static Set<String> getClusterKeys(RedisKeyCommands<String, byte[]> commands, ScanArgs args) {
        ScanCursor c = clusterScan.get(commands);
        if (c == null || c.isFinished()) {
            c = ScanCursor.INITIAL;
        }
        KeyScanCursor<String> result = commands.scan(c, args);
        // 缓存上一次的结果
        clusterScan.put(commands, result);
        return new HashSet<>(result.getKeys());
    }

    /**
     * key剩余有效期  单位：秒
     *
     * @param commands
     * @param key
     * @return
     */
    public static long ttl(RedisKeyCommands<String, byte[]> commands, String key) {
        return commands.ttl(key);
    }

    /**
     * 设置key的过期时间
     *
     * @param commands
     * @param key
     * @param time
     */
    public static void expire(RedisKeyCommands<String, byte[]> commands, String key, long time) {
        commands.expire(key, time);
    }

    public static String type(RedisKeyCommands<String, byte[]> commands, String key) {
        return commands.type(key);
    }

    public static Long del(RedisKeyCommands<String, byte[]> commands, String... keys) {
        return commands.del(keys);
    }

    /**
     * 获取key的值
     *
     * @param commands
     * @param key      key
     * @return
     */
    @SuppressWarnings("unchecked")
    public static KeyInfo value(RedisKeyCommands<String, byte[]> commands, String key) {
        long ttl = ttl(commands, key);
        Integer type = EnumUtils.getValueByName(RedisValueTypeEnum.values(), type(commands, key));
        KeyInfo ki = KeyInfo.builder().type(type)
                .ttl(ttl).key(key).build();

        if (RedisValueTypeEnum.STRING.getValue().equals(type)) {
            String value = new String(((RedisStringCommands<String, byte[]>) commands).get(key));
            ki.setValue(value);
            return ki;
        }
        if (RedisValueTypeEnum.SET.getValue().equals(type)) {
            Set<String> members = castSetCommands(commands).smembers(key).stream().map(String::new).collect(Collectors.toSet());
            ki.setValue(JsonUtils.toJSONString(members));
            return ki;
        }

        return ki;
    }

    /**
     * 添加key-value
     *
     * @param commands
     * @param keyInfo
     */
    public static void addKeyValue(BaseRedisCommands<String, byte[]> commands, KeyInfo keyInfo) {
        if (RedisValueTypeEnum.STRING.getValue().equals(keyInfo.getType())) {
            setValue(castStringCommands(commands), keyInfo);
            return;
        }
    }

    /**
     * String类型添加
     *
     * @param commands
     * @param keyValue
     */
    public static void setValue(RedisStringCommands<String, byte[]> commands, KeyInfo<String> keyValue) {
        if (!RedisValueTypeEnum.STRING.getValue().equals(keyValue.getType())) {
            throw new BusinessRuntimeException("value类型不为string类型!");
        }
        commands.set(keyValue.getKey(), keyValue.getValue().getBytes());
        checkAndSetExpire(castKeyCommands(commands), keyValue);
    }

    public static void sadd(RedisSetCommands<String, byte[]> commands, KeyInfo<Set<String>> keyInfo) {
        if (!RedisValueTypeEnum.SET.getValue().equals(keyInfo.getType())) {
            throw new BusinessRuntimeException("value类型不为set类型!");
        }

//        commands.sadd(keyInfo.getKey(), keyInfo.getValue().toArray());
    }

    /**
     * 检查并且设置过期时间
     *
     * @param commands
     * @param keyInfo
     */
    private static void checkAndSetExpire(RedisKeyCommands<String, byte[]> commands, KeyInfo keyInfo) {
        Long expire = keyInfo.getTtl();
        if (expire != null && !expire.equals(-1L)) {
            expire(commands, keyInfo.getKey(), expire);
        }
    }


    @SuppressWarnings("unchecked")
    private static RedisKeyCommands<String, byte[]> castKeyCommands(Object commands) {
        return RedisKeyCommands.class.cast(commands);
    }

    @SuppressWarnings("unchecked")
    private static RedisServerCommands<String, byte[]> castServerCommands(Object commands) {
        return RedisServerCommands.class.cast(commands);
    }

    @SuppressWarnings("unchecked")
    private static RedisStringCommands<String, byte[]> castStringCommands(Object commands) {
        return RedisStringCommands.class.cast(commands);
    }

    @SuppressWarnings("unchecked")
    private static RedisSetCommands<String, byte[]> castSetCommands(Object commands) {
        return RedisSetCommands.class.cast(commands);
    }
}
