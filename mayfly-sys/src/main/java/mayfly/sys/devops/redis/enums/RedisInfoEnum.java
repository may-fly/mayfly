package mayfly.sys.devops.redis.enums;

/**
 * @author meilin.huang
 * @version 1.0
 * @date 2019-01-19 11:22 AM
 */
public enum RedisInfoEnum {
    Stats("Stats"),
    Keyspace("Keyspace"),
    Commandstats("Commandstats"),
    Replication("Replication"),
    Clients("Clients"),
    CPU("CPU"),
    Memory("Memory"),
    Server("Server"),
    Persistence("Persistence"),
    CollectTime("CollectTime"),
    DIFF("diff");

    private String value;

    RedisInfoEnum(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return this.value;
    }

    public static RedisInfoEnum value(String input) {
        RedisInfoEnum[] constants = values();
        for (RedisInfoEnum constant : constants) {
            if (constant.value.equals(input)) {
                return constant;
            }
        }
        return null;
    }

    public String getValue() {
        return value;
    }

}
