package mayfly.core.mail;

import mayfly.core.util.MapUtils;
import mayfly.core.util.StringUtils;

import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Properties;

/**
 * @author meilin.huang
 * @date 2020-07-23 3:11 下午
 */
public class MailAccount {
    private static final String MAIL_PROTOCOL = "mail.transport.protocol";
    private static final String SMTP_HOST = "mail.smtp.host";
    private static final String SMTP_PORT = "mail.smtp.port";
    private static final String SMTP_AUTH = "mail.smtp.auth";
    private static final String SMTP_CONNECTION_TIMEOUT = "mail.smtp.connectiontimeout";
    private static final String SMTP_TIMEOUT = "mail.smtp.timeout";

    private static final String STARTTLS_ENABLE = "mail.smtp.starttls.enable";
    private static final String SSL_ENABLE = "mail.smtp.ssl.enable";
    private static final String SOCKET_FACTORY = "mail.smtp.socketFactory.class";
    private static final String SOCKET_FACTORY_FALLBACK = "mail.smtp.socketFactory.fallback";
    private static final String SOCKET_FACTORY_PORT = "smtp.socketFactory.port";

    private static final String MAIL_DEBUG = "mail.debug";
    private static final String SPLIT_LONG_PARAMS = "mail.mime.splitlongparameters";

    /**
     * SMTP服务器域名
     */
    private String host;
    /**
     * SMTP服务端口
     */
    private Integer port;
    /**
     * 是否需要用户名密码验证
     */
    private Boolean auth;
    /**
     * 用户名
     */
    private String user;
    /**
     * 密码
     */
    private String pass;
    /**
     * 发送方，遵循RFC-822标准
     */
    private String from;

    /**
     * 是否打开调试模式，调试模式会显示与邮件服务器通信过程，默认不开启
     */
    private boolean debug;
    /**
     * 编码用于编码邮件正文和发送人、收件人等中文
     */
    private Charset charset = StandardCharsets.UTF_8;
    /**
     * 对于超长参数是否切分为多份，默认为false（国内邮箱附件不支持切分的附件名）
     */
    private boolean splitlongparameters;

    /**
     * 使用 STARTTLS安全连接，STARTTLS是对纯文本通信协议的扩展。它将纯文本连接升级为加密连接（TLS或SSL）， 而不是使用一个单独的加密通信端口。
     */
    private boolean starttlsEnable;
    /**
     * 使用 SSL安全连接
     */
    private Boolean sslEnable;
    /**
     * 指定实现javax.net.SocketFactory接口的类的名称,这个类将被用于创建SMTP的套接字
     */
    private String socketFactoryClass = "javax.net.ssl.SSLSocketFactory";
    /**
     * 如果设置为true,未能创建一个套接字使用指定的套接字工厂类将导致使用java.net.Socket创建的套接字类, 默认值为true
     */
    private boolean socketFactoryFallback;
    /**
     * 指定的端口连接到在使用指定的套接字工厂。如果没有设置,将使用默认端口
     */
    private int socketFactoryPort = 465;

    /**
     * SMTP超时时长，单位毫秒，缺省值不超时
     */
    private long timeout;
    /**
     * Socket连接超时值，单位毫秒，缺省值不超时
     */
    private long connectionTimeout;

    private Properties smtpProperties;

    // -------------------------------------------------------------- Constructor start

    /**
     * 构造,所有参数需自行定义或保持默认值
     */
    private MailAccount() {
    }

    public static Builder builder() {
        return new Builder();
    }

    // -------------------------------------------------------------- Constructor end

    /**
     * 获得SMTP相关信息
     *
     * @return {@link Properties}
     */
    public Properties getSmtpProps() {
        if (smtpProperties != null) {
            return smtpProperties;
        }

        //全局系统参数
        System.setProperty(SPLIT_LONG_PARAMS, String.valueOf(this.splitlongparameters));

        final Properties p = new Properties();
        p.put(MAIL_PROTOCOL, "smtp");
        p.put(SMTP_HOST, this.host);
        p.put(SMTP_PORT, String.valueOf(this.port));
        p.put(SMTP_AUTH, String.valueOf(this.auth));
        if (this.timeout > 0) {
            p.put(SMTP_TIMEOUT, String.valueOf(this.timeout));
        }
        if (this.connectionTimeout > 0) {
            p.put(SMTP_CONNECTION_TIMEOUT, String.valueOf(this.connectionTimeout));
        }

        p.put(MAIL_DEBUG, String.valueOf(this.debug));

        if (this.starttlsEnable) {
            //STARTTLS是对纯文本通信协议的扩展。它将纯文本连接升级为加密连接（TLS或SSL）， 而不是使用一个单独的加密通信端口。
            p.put(STARTTLS_ENABLE, "true");

            if (this.sslEnable == null) {
                //为了兼容旧版本，当用户没有此项配置时，按照starttlsEnable开启状态时对待
                this.sslEnable = true;
            }
        }

        // SSL
        if (this.sslEnable != null && this.sslEnable) {
            p.put(SSL_ENABLE, "true");
            p.put(SOCKET_FACTORY, socketFactoryClass);
            p.put(SOCKET_FACTORY_FALLBACK, String.valueOf(this.socketFactoryFallback));
            p.put(SOCKET_FACTORY_PORT, String.valueOf(this.socketFactoryPort));
        }

        smtpProperties = p;
        return p;
    }

    /**
     * 如果某些值为null，使用默认值
     *
     * @return this
     */
    public MailAccount defaultIfEmpty() {
        if (StringUtils.isEmpty(this.from)) {
            this.from = this.user;
        }
        // 去掉发件人的姓名部分
        final String fromAddress = Mail.parseFirstAddress(this.from, this.charset).getAddress();

        if (StringUtils.isEmpty(this.host)) {
            // 如果SMTP地址为空，默认使用smtp.<发件人邮箱后缀>
            this.host = String.format("smtp.%s", fromAddress.substring(fromAddress.indexOf('@') + 1));
        }
        if (StringUtils.isEmpty(user)) {
            // 如果用户名为空，默认为发件人邮箱前缀
            this.user = fromAddress.substring(fromAddress.indexOf('@'));
        }
        if (this.auth == null) {
            // 如果密码非空白，则使用认证模式
            this.auth = !StringUtils.isEmpty(pass);
        }
        if (this.port == null) {
            // 端口在SSL状态下默认与socketFactoryPort一致，非SSL状态下默认为25
            this.port = (this.sslEnable != null && this.sslEnable) ? this.socketFactoryPort : 25;
        }
        if (this.charset == null) {
            // 默认UTF-8编码
            this.charset = StandardCharsets.UTF_8;
        }
        return this;
    }


    public static class Builder {

        private final MailAccount mailAccount;

        public Builder() {
            this.mailAccount = new MailAccount();
        }

        public Builder fromProperties(Properties properties, String prefix) {
            Builder builder = host(properties.getProperty(prefix + ".host"))
                    .port(MapUtils.getInteger(properties, prefix + ".port"))
                    .user(properties.getProperty(prefix + ".user"))
                    .auth(MapUtils.getBoolean(properties, prefix + ".auth", true))
                    .pass(properties.getProperty(prefix + ".pass"))
                    .from(properties.getProperty(prefix + ".from"))
                    .debug(MapUtils.getBoolean(properties, prefix + ".debug", false))
                    .starttlsEnable(MapUtils.getBoolean(properties, prefix + ".starttlsEnable", false))
                    .sslEnable(MapUtils.getBoolean(properties, prefix + ".sslEnable", false))
                    .socketFactoryClass(properties.getProperty(prefix + ".socketFactoryClass"))
                    .socketFactoryFallback(MapUtils.getBoolean(properties, prefix + ".socketFactoryFallback", true));

            Long timeout = MapUtils.getLong(properties, prefix + ".timeout");
            Long connectionTimeout = MapUtils.getLong(properties, prefix + ".connectionTimeout");
            if (timeout != null) {
                builder.timeout(timeout);
            }
            if (connectionTimeout != null) {
                builder.connectionTimeout(connectionTimeout);
            }
            return builder;
        }

        /**
         * 设置SMTP服务器域名
         *
         * @param host SMTP服务器域名
         * @return this
         */
        public Builder host(String host) {
            mailAccount.host = host;
            return this;
        }

        /**
         * 设置SMTP服务端口
         *
         * @param port SMTP服务端口
         * @return this
         */
        public Builder port(Integer port) {
            mailAccount.port = port;
            return this;
        }

        /**
         * 设置是否需要用户名密码验证
         *
         * @param isAuth 是否需要用户名密码验证
         * @return this
         */
        public Builder auth(boolean isAuth) {
            mailAccount.auth = isAuth;
            return this;
        }

        /**
         * 设置用户名
         *
         * @param user 用户名
         * @return this
         */
        public Builder user(String user) {
            mailAccount.user = user;
            return this;
        }

        /**
         * 设置密码
         *
         * @param pass 密码
         * @return this
         */
        public Builder pass(String pass) {
            mailAccount.pass = pass;
            return this;
        }

        /**
         * 设置发送方，遵循RFC-822标准<br>
         * 发件人可以是以下形式：
         *
         * <pre>
         * 1. user@xxx.xx
         * 2.  name &lt;user@xxx.xx&gt;
         * </pre>
         *
         * @param from 发送方，遵循RFC-822标准
         * @return this
         */
        public Builder from(String from) {
            mailAccount.from = from;
            return this;
        }

        /**
         * 设置是否打开调试模式，调试模式会显示与邮件服务器通信过程，默认不开启
         *
         * @param debug 是否打开调试模式，调试模式会显示与邮件服务器通信过程，默认不开启
         * @return this
         */
        public Builder debug(boolean debug) {
            mailAccount.debug = debug;
            return this;
        }

        /**
         * 设置字符集编码
         *
         * @param charset 字符集编码
         * @return this
         */
        public Builder charset(Charset charset) {
            mailAccount.charset = charset;
            return this;
        }

        /**
         * 设置对于超长参数是否切分为多份，默认为false（国内邮箱附件不支持切分的附件名）
         *
         * @param splitlongparameters 对于超长参数是否切分为多份
         */
        public Builder splitlongparameters(boolean splitlongparameters) {
            mailAccount.splitlongparameters = splitlongparameters;
            return this;
        }

        /**
         * 设置是否使用STARTTLS安全连接，STARTTLS是对纯文本通信协议的扩展。它将纯文本连接升级为加密连接（TLS或SSL）， 而不是使用一个单独的加密通信端口。
         *
         * @param startttlsEnable 是否使用STARTTLS安全连接
         * @return this
         */
        public Builder starttlsEnable(boolean startttlsEnable) {
            mailAccount.starttlsEnable = startttlsEnable;
            return this;
        }

        /**
         * 设置是否使用SSL安全连接
         *
         * @param sslEnable 是否使用SSL安全连接
         * @return this
         */
        public Builder sslEnable(Boolean sslEnable) {
            mailAccount.sslEnable = sslEnable;
            return this;
        }

        /**
         * 如果设置为true,未能创建一个套接字使用指定的套接字工厂类将导致使用java.net.Socket创建的套接字类, 默认值为true
         *
         * @param socketFactoryFallback 如果设置为true,未能创建一个套接字使用指定的套接字工厂类将导致使用java.net.Socket创建的套接字类, 默认值为true
         * @return this
         */
        public Builder socketFactoryFallback(boolean socketFactoryFallback) {
            mailAccount.socketFactoryFallback = socketFactoryFallback;
            return this;
        }

        /**
         * 设置指定实现javax.net.SocketFactory接口的类的名称,这个类将被用于创建SMTP的套接字
         *
         * @param socketFactoryClass 指定实现javax.net.SocketFactory接口的类的名称,这个类将被用于创建SMTP的套接字
         * @return this
         */
        public Builder socketFactoryClass(String socketFactoryClass) {
            mailAccount.socketFactoryClass = socketFactoryClass;
            return this;
        }

        /**
         * 指定的端口连接到在使用指定的套接字工厂。如果没有设置,将使用默认端口
         *
         * @param socketFactoryPort 指定的端口连接到在使用指定的套接字工厂。如果没有设置,将使用默认端口
         * @return this
         */
        public Builder socketFactoryPort(int socketFactoryPort) {
            mailAccount.socketFactoryPort = socketFactoryPort;
            return this;
        }

        /**
         * 设置SMTP超时时长，单位毫秒，缺省值不超时
         *
         * @param timeout SMTP超时时长，单位毫秒，缺省值不超时
         * @return this
         */
        public Builder timeout(long timeout) {
            mailAccount.timeout = timeout;
            return this;
        }

        /**
         * 设置Socket连接超时值，单位毫秒，缺省值不超时
         *
         * @param connectionTimeout Socket连接超时值，单位毫秒，缺省值不超时
         * @return this
         */
        public Builder connectionTimeout(long connectionTimeout) {
            mailAccount.connectionTimeout = connectionTimeout;
            return this;
        }

        public MailAccount build() {
            return mailAccount.defaultIfEmpty();
        }
    }


    /**
     * 获得SMTP服务器域名
     *
     * @return SMTP服务器域名
     */
    public String getHost() {
        return host;
    }

    /**
     * 获得SMTP服务端口
     *
     * @return SMTP服务端口
     */
    public Integer getPort() {
        return port;
    }


    /**
     * 是否需要用户名密码验证
     *
     * @return 是否需要用户名密码验证
     */
    public Boolean isAuth() {
        return auth;
    }


    /**
     * 获取用户名
     *
     * @return 用户名
     */
    public String getUser() {
        return user;
    }

    /**
     * 获取密码
     *
     * @return 密码
     */
    public String getPass() {
        return pass;
    }


    /**
     * 获取发送方，遵循RFC-822标准
     *
     * @return 发送方，遵循RFC-822标准
     */
    public String getFrom() {
        return from;
    }


    /**
     * 是否打开调试模式，调试模式会显示与邮件服务器通信过程，默认不开启
     *
     * @return 是否打开调试模式，调试模式会显示与邮件服务器通信过程，默认不开启
     */
    public boolean isDebug() {
        return debug;
    }

    /**
     * 获取字符集编码
     *
     * @return 编码
     */
    public Charset getCharset() {
        return charset;
    }

    public void setCharset(Charset charset) {
        this.charset = charset;
    }

    /**
     * 对于超长参数是否切分为多份，默认为false（国内邮箱附件不支持切分的附件名）
     *
     * @return 对于超长参数是否切分为多份
     */
    public boolean isSplitlongparameters() {
        return splitlongparameters;
    }


    /**
     * 是否使用 STARTTLS安全连接，STARTTLS是对纯文本通信协议的扩展。它将纯文本连接升级为加密连接（TLS或SSL）， 而不是使用一个单独的加密通信端口。
     *
     * @return 是否使用 STARTTLS安全连接
     */
    public boolean isStarttlsEnable() {
        return this.starttlsEnable;
    }


    /**
     * 是否使用 SSL安全连接
     *
     * @return 是否使用 SSL安全连接
     */
    public Boolean isSslEnable() {
        return this.sslEnable;
    }


    /**
     * 获取指定实现javax.net.SocketFactory接口的类的名称,这个类将被用于创建SMTP的套接字
     *
     * @return 指定实现javax.net.SocketFactory接口的类的名称, 这个类将被用于创建SMTP的套接字
     */
    public String getSocketFactoryClass() {
        return socketFactoryClass;
    }

    /**
     * 如果设置为true,未能创建一个套接字使用指定的套接字工厂类将导致使用java.net.Socket创建的套接字类, 默认值为true
     *
     * @return 如果设置为true, 未能创建一个套接字使用指定的套接字工厂类将导致使用java.net.Socket创建的套接字类, 默认值为true
     */
    public boolean isSocketFactoryFallback() {
        return socketFactoryFallback;
    }


    /**
     * 获取指定的端口连接到在使用指定的套接字工厂。如果没有设置,将使用默认端口
     *
     * @return 指定的端口连接到在使用指定的套接字工厂。如果没有设置,将使用默认端口
     */
    public int getSocketFactoryPort() {
        return socketFactoryPort;
    }

    /**
     * 获取用户密码认证器
     *
     * @return 认证器
     */
    public UserPassAuthenticator getUserPassAuthenticator() {
        return new UserPassAuthenticator(this.user, this.pass);
    }

    /**
     * 用户密码认证器
     */
    public static class UserPassAuthenticator extends Authenticator {

        private final String user;
        private final String pass;

        /**
         * 构造
         *
         * @param user 用户名
         * @param pass 密码
         */
        public UserPassAuthenticator(String user, String pass) {
            super();
            this.user = user;
            this.pass = pass;
        }

        @Override
        protected PasswordAuthentication getPasswordAuthentication() {
            return new PasswordAuthentication(this.user, this.pass);
        }
    }

    @Override
    public String toString() {
        return "MailAccount [host=" + host + ", port=" + port + ", auth=" + auth + ", user=" + user + ", pass=" + (StringUtils.isEmpty(this.pass) ? "" : "******") + ", from=" + from + ", startttlsEnable="
                + starttlsEnable + ", socketFactoryClass=" + socketFactoryClass + ", socketFactoryFallback=" + socketFactoryFallback + ", socketFactoryPort=" + socketFactoryPort + "]";
    }

}
