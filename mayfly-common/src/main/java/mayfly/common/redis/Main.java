package mayfly.common.redis;

import io.lettuce.core.RedisClient;
import io.lettuce.core.RedisURI;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.api.async.RedisAsyncCommands;

import java.time.Duration;

/**
 * @author meilin.huang
 * @version 1.0
 * @date 2018-12-19 5:59 PM
 */
public class Main {
    public static void main(String[] args) throws Exception {
        RedisURI uri = new RedisURI("localhost", 6379, Duration.ZERO);
        RedisClient client = RedisClient.create(uri);
        StatefulRedisConnection<String, String> connection = client.connect();
        //
        RedisAsyncCommands<String, String> commands = connection.async();
        long value = commands.dbsize().get();
        System.out.print(value);
    }
}
