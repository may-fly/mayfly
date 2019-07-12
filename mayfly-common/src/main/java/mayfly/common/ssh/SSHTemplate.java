package mayfly.common.ssh;

import ch.ethz.ssh2.Connection;
import ch.ethz.ssh2.Session;
import ch.ethz.ssh2.StreamGobbler;
import mayfly.common.result.Result;
import mayfly.common.util.ThreadFactoryBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.concurrent.*;
import java.util.function.Function;

/**
 * @author meilin.huang
 * @version 1.0
 * @date 2019-07-09 12:47
 */
public class SSHTemplate {

    private static final Logger logger = LoggerFactory.getLogger(SSHTemplate.class);

    private static final int CONNECT_TIMEOUT = 6000;

    private static final int OP_TIMEOUT = 12000;

    private static ThreadPoolExecutor taskPool = new ThreadPoolExecutor(
            4, 4, 0L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<>(1000),
            ThreadFactoryBuilder.nameFormat("SSH-%d").daemon(true).build());


    private static SSHTemplate sshTemplate = new SSHTemplate();

    public static SSHTemplate getSshTemplate() {
        return sshTemplate;
    }


    public Result execute(String ip, Function<SSHSession, Result<String>> callback) throws SSHException{
        return execute(ip, 22, "root",
                "", callback);
    }

    /**
     * 通过回调执行命令
     * @param ip
     * @param port
     * @param username
     * @param password
     * @param callback 可以使用Session执行多个命令
     * @throws SSHException
     */
    public Result<String> execute(String ip, int port, String username, String password,
                                  Function<SSHSession, Result<String>> callback) throws SSHException{
        Connection conn = null;
        try {
            conn = getConnection(ip, port, username, password);
            return callback.apply(new SSHSession(conn, ip + ":" + port));
        } catch (Exception e) {
            throw new SSHException("SSH err: " + e.getMessage(), e);
        } finally {
            close(conn);
        }
    }

    /**
     * 获取连接并校验
     * @param ip
     * @param port
     * @param username
     * @param password
     * @return Connection
     * @throws Exception
     */
    private Connection getConnection(String ip, int port,
                                     String username, String password) throws Exception {
        Connection conn = new Connection(ip, port);
        conn.connect(null, CONNECT_TIMEOUT, CONNECT_TIMEOUT);
        boolean isAuthenticated = conn.authenticateWithPassword(username, password);
        if (!isAuthenticated) {
            throw new SSHException("SSH authentication failed with [ userName: " +
                    username + ", password: " + password + "]");
        }
        return conn;
    }

    /**
     * 获取调用命令后的返回结果
     * @param is 输入流
     * @return 如果获取结果有异常或者无结果，那么返回null
     */
    private String getResult(InputStream is) {
        final StringBuilder buffer = new StringBuilder();
        processStream(is, ((line, lineNum) -> {
            if (lineNum > 1) {
                buffer.append(System.lineSeparator());
            }
            buffer.append(line);
        }));
        return buffer.length() > 0 ? buffer.toString() : null;
    }

    /**
     * 从流中获取内容
     * @param is
     */
    private void processStream(InputStream is, LineProcessor lineProcessor) {
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new InputStreamReader(new StreamGobbler(is)));
            String line;
            int lineNum = 1;
            while ((line = reader.readLine()) != null) {
                try {
                    lineProcessor.process(line, lineNum);
                } catch (Exception e) {
                    logger.error("err line:" + line, e);
                }
                lineNum++;
            }
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        } finally {
            close(reader);
        }
    }

    private void close(BufferedReader read) {
        if (read != null) {
            try {
                read.close();
            } catch (IOException e) {
                logger.error(e.getMessage(), e);
            }
        }
    }

    private void close(Connection conn) {
        if (conn != null) {
            try {
                conn.close();
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
            }
        }
    }

    private static void close(Session session) {
        if (session != null) {
            try {
                session.close();
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
            }
        }
    }

    /**
     * 可以调用多次executeCommand， 并返回结果
     */
    public class SSHSession{

        private String address;
        private Connection conn;

        private SSHSession(Connection conn, String address) {
            this.conn = conn;
            this.address = address;
        }

        /**
         * 执行命令并返回结果，可以执行多次
         * @param cmd
         * @return 执行成功Result为true，并携带返回信息,返回信息可能为null
         *         执行失败Result为false，并携带失败信息
         *         执行异常Result为false，并携带异常
         */
        public Result<String> executeCommand(String cmd) {
            return executeCommand(cmd, OP_TIMEOUT);
        }

        public Result<String> executeCommand(String cmd, int timeoutMillis) {
            return executeCommand(cmd, null, timeoutMillis);
        }

        public Result<String> executeCommand(String cmd, LineProcessor lineProcessor) {
            return executeCommand(cmd, lineProcessor, OP_TIMEOUT);
        }

        /**
         * 执行命令并返回结果，可以执行多次
         * @param cmd
         * @param lineProcessor 回调处理行
         * @return 如果lineProcessor不为null,那么永远返回Result.success
         */
        public Result<String> executeCommand(String cmd, LineProcessor lineProcessor, int timeoutMillis) {
            Session session = null;
            try {
                session = conn.openSession();
                return executeCommand(session, cmd, timeoutMillis, lineProcessor);
            } catch (Exception e) {
                logger.error("execute ip:" + conn.getHostname() + " cmd:" + cmd, e);
                return Result.<String>error().with(e.getMessage());
            } finally {
                close(session);
            }
        }

        public Result<String> executeCommand(final Session session, final String cmd,
                                     final int timeoutMillis, final LineProcessor lineProcessor) throws Exception{
            Future<Result<String>> future = taskPool.submit(() -> {
                session.execCommand(cmd);
                // 如果客户端需要进行行处理，则直接进行回调
                if(lineProcessor != null) {
                    processStream(session.getStdout(), lineProcessor);
                } else {
                    // 获取标准输出
                    String rst = getResult(session.getStdout());
                    if(rst != null) {
                        return Result.<String>success().with(rst);
                    }
                    // 返回为null代表可能有异常，需要检测标准错误输出，以便记录日志
                    Result<String> errResult = tryLogError(session.getStderr(), cmd);
                    if(errResult != null) {
                        return errResult;
                    }
                }
                return Result.success();
            });
            try {
                Result<String> rst = future.get(timeoutMillis, TimeUnit.MILLISECONDS);
                future.cancel(true);
                return rst;
            } catch (TimeoutException e) {
                logger.error("exec ip:{} {} timeout:{}", conn.getHostname(), cmd, timeoutMillis);
                throw new SSHException(e);
            }
        }

        private Result<String> tryLogError(InputStream is, String cmd) {
            String errInfo = getResult(is);
            if(errInfo != null) {
                logger.error("address " + address + " execute cmd:({}), err:{}", cmd, errInfo);
                return Result.<String>error().with(errInfo);
            }
            return null;
        }
    }

    /**
     * 从流中直接解析数据
     */
    @FunctionalInterface
    public interface LineProcessor{
        /**
         * 处理行
         * @param line  内容
         * @param lineNum   行号，从1开始
         * @throws Exception
         */
        void process(String line, int lineNum) throws Exception;
    }
}
