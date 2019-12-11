package mayfly.sys.redis.enums;

import mayfly.core.util.StringUtils;

/**
 * @author meilin.huang
 * @version 1.0
 * @date 2019-01-19 10:51 AM
 */
public enum RedisConfEnum {

    /**
     * redis默认不是以守护进程的方式运行，可以通过该配置项修改，使用yes启用守护进程
     */
    DAEMONIZE("daemonize", "yes|no"),

    /**
     * 端口号
     */
    PORT("port", "\\d*"),

    /**
     * 当客户端闲置多长时间后关闭连接，如果指定为0，表示永不关闭
     */
    TIMEOUT("timeout", "\\d*"),

    /**
     * 绑定的主机地址
     */
    BIND("bind"),

    /**
     * 指定本地数据库存放目录
     */
    DIR("dir"),

    /**
     * 设置认证密码
     */
    REQUIREPASS("requirepass"),

    /**
     * 启用或停用集群
     */
    CLUSTER_ENABLED("cluster-enabled", "yes|no"),

    /**
     * 设置当本机为slave服务时，设置master服务的IP地址及端口，在redis启动时，它会自动从master进行数据同步
     * slaveof <masterip><masterport>
     */
    SLAVEOF("slaveof");

    public String parameter;

    public String valuePattern;

    RedisConfEnum(String parameter, String valuePattern) {
        this.parameter = parameter;
        this.valuePattern = valuePattern;
    }

    RedisConfEnum(String parameter) {
        this.parameter = parameter;
    }

    public static RedisConfEnum getByParam(String param) {
        if (StringUtils.isEmpty(param)) {
            return null;
        }
        for (RedisConfEnum confParam : RedisConfEnum.values()) {
            if (confParam.parameter.equals(param)) {
                return confParam;
            }
        }
        return null;
    }
}
