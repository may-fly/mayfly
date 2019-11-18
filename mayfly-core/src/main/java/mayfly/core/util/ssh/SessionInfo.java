package mayfly.core.util.ssh;

import mayfly.core.util.Assert;
import mayfly.core.util.StringUtils;

/**
 * @author meilin.huang
 * @version 1.0
 * @date 2019-11-11 12:35 下午
 */
public class SessionInfo {

    /**
     * 缓存标识
     */
    private String id;

    /**
     * 主机
     */
    private String host;

    /**
     * 端口
     */
    private int port;

    /**
     * 用户名
     */
    private String username;

    /**
     * 密码
     */
    private String password;

    public static SessionInfoBuilder builder(String host) {
        return new SessionInfoBuilder(host);
    }

    public String getId() {
        return id;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getPort() {
        return port;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }


    public static class SessionInfoBuilder {

        private SessionInfo info;

        private SessionInfoBuilder(String host) {
            this.info = new SessionInfo();
            info.host = host;
        }

        public SessionInfoBuilder port(int port) {
            Assert.state(port > 0, "port必须大于0");
            this.info.port = port;
            return this;
        }

        public SessionInfoBuilder id(String id) {
            this.info.id = id;
            return this;
        }

        public SessionInfoBuilder username(String username) {
            this.info.username = username;
            return this;
        }

        public SessionInfoBuilder password(String password) {
            this.info.password = password;
            return this;
        }

        public SessionInfo build() {
            if (StringUtils.isEmpty(info.username)) {
                info.username = "root";
            }
            // 如果主机id为空，则host作为默认的缓存key
            if (StringUtils.isEmpty(info.id)) {
                info.id = info.host;
            }
            return info;
        }
    }
}
