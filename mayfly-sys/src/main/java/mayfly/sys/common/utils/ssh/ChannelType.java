package mayfly.sys.common.utils.ssh;

import mayfly.core.util.enums.ValueEnum;

/**
 * Jsch支持的Channel类型
 */
public enum ChannelType implements ValueEnum<String> {
    /**
     * Session
     */
    SESSION("session"),
    /**
     * shell
     */
    SHELL("shell"),
    /**
     * exec
     */
    EXEC("exec"),
    /**
     * x11
     */
    X11("x11"),
    /**
     * agent forwarding
     */
    AGENT_FORWARDING("auth-agent@openssh.com"),
    /**
     * direct tcpip
     */
    DIRECT_TCPIP("direct-tcpip"),
    /**
     * forwarded tcpip
     */
    FORWARDED_TCPIP("forwarded-tcpip"),
    /**
     * sftp
     */
    SFTP("sftp"),
    /**
     * subsystem
     */
    SUBSYSTEM("subsystem");

    /**
     * channel值
     */
    private String value;

    /**
     * 构造
     *
     * @param value 类型值
     */
    ChannelType(String value) {
        this.value = value;
    }

    /**
     * 获取值
     *
     * @return 值
     */
    @Override
    public String getValue() {
        return this.value;
    }
}
