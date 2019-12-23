package mayfly.core.util.ssh;


import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.ChannelShell;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import mayfly.core.exception.BusinessRuntimeException;
import mayfly.core.util.IOUtils;
import mayfly.core.util.StringUtils;
import mayfly.core.util.cache.Cache;
import mayfly.core.util.cache.CacheBuilder;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * @author meilin.huang
 * @version 1.0
 * @date 2019-11-08 2:07 下午
 */
public class SSHUtils {


    /**
     * session缓存，最多允许15个session同时连接，移除session时候执行close操作
     */
    private static Cache<String, Session> sessionCache = CacheBuilder.<String, Session>newTimedBuilder(30, TimeUnit.MINUTES)
            .capacity(15).removeCallback(SSHUtils::close).build();


    /**
     * 获得一个SSH主机会话，重用已经使用的会话（若在指定时间内一直未使用该session，则会关闭该session）
     *
     * @param sessionInfo 会话信息
     * @return session
     */
    public static Session getSession(SessionInfo sessionInfo) throws SSHException {
        return getSession(sessionInfo.getId(), () -> sessionInfo);
    }

    /**
     * 获得一个SSH主机会话，重用已经使用的会话（若在指定时间内一直未使用该session，则会关闭该session）
     *
     * @param id                  session cache key
     * @param sessionInfoSupplier sessionInfo{@link SessionInfo}生产者
     *                            （如果不存在该session缓存，则从sessionInfoSupplier中获取sessionInfo。主要考虑该信息可能从数据库等其他地方获取）
     * @return session
     */
    public static Session getSession(String id, Supplier<SessionInfo> sessionInfoSupplier) {
        return sessionCache.get(id, () -> {
            try {
                return openSession(sessionInfoSupplier.get());
            } catch (SSHException e) {
                throw new BusinessRuntimeException("连接失败，请重试");
            }
        }, true);
    }

    /**
     * 打开一个新的SSH会话
     *
     * @param info 会话信息
     * @return SSH会话
     */
    public static Session openSession(SessionInfo info) throws SSHException {
        final Session session = createSession(info);
        try {
            session.connect();
        } catch (JSchException e) {
            throw new SSHException(e);
        }
        return session;
    }

    /**
     * 新建一个新的SSH会话，此方法并不打开会话（即不调用connect方法）
     *
     * @param info 会话信息
     * @return SSH会话
     */
    public static Session createSession(SessionInfo info) throws SSHException {
        JSch jsch = new JSch();
        Session session;
        try {
            session = jsch.getSession(info.getUsername(), info.getHost(), info.getPort());
        } catch (JSchException e) {
            throw new SSHException(e);
        }
        // 设置第一次登录的时候提示，可选值：(ask | yes | no)
        session.setConfig("StrictHostKeyChecking", "no");
        if (!StringUtils.isEmpty(info.getPassword())) {
            session.setPassword(info.getPassword());
        }
        return session;
    }

    /**
     * 创建并打开Channel连接，并没有connect channel（因为exec类型的channel需要先设置cmd后才能connect）
     *
     * @param session     Session会话
     * @param channelType 通道类型，可以是shell或sftp等，见{@link ChannelType}
     * @return {@link Channel}
     */
    public static Channel openChannel(Session session, ChannelType channelType) throws SSHException {
        try {
            if (!session.isConnected()) {
                session.connect();
            }
            return session.openChannel(channelType.getValue());
        } catch (JSchException e) {
            throw new SSHException(e);
        }
    }

    /**
     * 打开Shell通道
     *
     * @param session Session会话
     * @return {@link ChannelShell}
     * @since 4.0.3
     */
    public static ChannelShell openChannelShell(Session session) throws SSHException {
        return (ChannelShell) openChannel(session, ChannelType.SHELL);
    }

    /**
     * 打开Shell通道
     *
     * @param info 会话信息
     * @return {@link ChannelShell}
     * @since 4.0.3
     */
    public static ChannelShell openChannelShell(SessionInfo info) throws SSHException {
        return (ChannelShell) openChannel(getSession(info), ChannelType.SHELL);
    }

    /**
     * 打开sftp通道，并connect
     *
     * @param info 会话信息
     * @return {@link ChannelShell}
     */
    public static ChannelSftp openChannelSftp(SessionInfo info) throws SSHException {
        return (ChannelSftp) openChannel(getSession(info), ChannelType.SFTP);
    }

    /**
     * 打开sftp通道
     *
     * @param session Session会话
     * @return {@link ChannelShell}
     * @since 4.0.3
     */
    public static ChannelSftp openChannelSftp(Session session) throws SSHException {
        return (ChannelSftp) openChannel(session, ChannelType.SFTP);
    }

    /**
     * 执行sftp操作（已处理channel的关闭）
     *
     * @param info     会话信息
     * @param function 操作channel
     * @param <T>      操作返回值
     * @return 执行结果
     * @throws SSHException ssh异常
     */
    public static <T> T sftpOperate(SessionInfo info, Function<ChannelSftp, T> function) throws SSHException {
        return sftpOperate(getSession(info), function);
    }

    /**
     * 执行sftp操作（已处理channel的关闭）
     *
     * @param session  会话
     * @param function 操作channel
     * @param <T>      操作返回值
     * @return 执行结果
     * @throws SSHException ssh异常
     */
    public static <T> T sftpOperate(Session session, Function<ChannelSftp, T> function) throws SSHException {
        ChannelSftp sftp = null;
        try {
            sftp = openChannelSftp(session);
            sftp.connect();
            return function.apply(sftp);
        } catch (JSchException e) {
            throw new SSHException(e);
        } finally {
            close(sftp);
        }
    }

    /**
     * 执行Shell命令
     *
     * @param cmd 命令
     * @return {@link ChannelExec}
     */
    public static String exec(SessionInfo info, String cmd) throws SSHException {
        return exec(getSession(info), cmd, null, null);
    }

    /**
     * 执行Shell命令
     *
     * @param cmd           命令
     * @param lineProcessor 行处理器
     * @return 执行结果。如果有lineProcessor {@link mayfly.core.util.IOUtils.LineProcessor}，则返回null(结果自行从lineProcessor解析处理取得)
     */
    public static String exec(SessionInfo info, String cmd, IOUtils.LineProcessor lineProcessor)
            throws SSHException {
        return exec(getSession(info), cmd, null, lineProcessor);
    }


    /**
     * 执行Shell命令
     *
     * @param session Session会话
     * @param cmd     命令
     * @return 执行结果
     */
    public static String exec(Session session, String cmd) throws SSHException {
        return exec(session, cmd, null, null);
    }

    /**
     * 执行Shell命令
     *
     * @param session Session会话
     * @param cmd     命令
     * @return 执行结果。如果有lineProcessor {@link mayfly.core.util.IOUtils.LineProcessor}，则返回null(结果自行从lineProcessor解析处理取得)
     */
    public static String exec(Session session, String cmd, IOUtils.LineProcessor lineProcessor) throws SSHException {
        return exec(session, cmd, null, lineProcessor);
    }

    /**
     * 执行Shell命令
     *
     * @param session       Session会话
     * @param cmd           命令
     * @param charset       发送和读取内容的编码
     * @param lineProcessor 行处理器（解析每行的数据结果）
     * @return 执行结果。如果有lineProcessor {@link mayfly.core.util.IOUtils.LineProcessor}，则返回null(结果自行从lineProcessor解析处理取得)
     */
    public static String exec(Session session, String cmd, Charset charset, IOUtils.LineProcessor lineProcessor) throws SSHException {
        if (charset == null) {
            charset = StandardCharsets.UTF_8;
        }
        ChannelExec channel = (ChannelExec) openChannel(session, ChannelType.EXEC);
        channel.setCommand(cmd.getBytes(charset));
        // 设置错误信息输出流
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        channel.setErrStream(outputStream);
        InputStream in = null;
        try {
            // 连接channel，需要先设置cmd，后连接才行
            channel.connect();
            in = channel.getInputStream();
            // 如果行处理器不为空，则调用行处理器
            if (lineProcessor != null) {
                IOUtils.processReadLine(in, lineProcessor);
                return null;
            } else {
                String result = IOUtils.read(in);
                // 如果结果为null，则有可能含有错误信息
                if (StringUtils.isEmpty(result)) {
                    byte[] err = outputStream.toByteArray();
                    if (err.length != 0) {
                        throw new SSHException(new String(err));
                    }
                }
                return result;
            }
        } catch (JSchException | IOException e) {
            sessionCache.removeByValue(session);
            throw new SSHException(e);
        } finally {
            IOUtils.close(in);
            close(channel);
        }
    }

    /**
     * 关闭SSH连接会话
     *
     * @param session SSH会话
     */
    public static void close(Session session) {
        if (session != null && session.isConnected()) {
            session.disconnect();
        }
    }

    /**
     * 关闭会话通道
     *
     * @param channel 会话通道
     */
    public static void close(Channel channel) {
        if (channel != null && channel.isConnected()) {
            channel.disconnect();
        }
    }
}
