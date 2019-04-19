package mayfly.common.ssh;

import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.UserInfo;
import mayfly.common.utils.Assert;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * @author meilin.huang
 * @version 1.0
 * @date 2019-01-07 1:36 PM
 */
public class SSHUtils {


    public static final String DEFAULT_HOST = "localhost";
    /**
     * 默认端口号
     */
    public static final int DEFAULT_PORT = 22;
    /**
     * 默认用户名
     */
    public static final String DEFAULT_USERNAME = "root";

    /**
     * timeout for session connection
     */
    private static final Integer SESSION_TIMEOUT = 30000;

    /**
     * timeout for channel connection
     */
    private static final Integer CHANNEL_TIMEOUT = 30000;

    /**
     * the interval for acquiring ret
     */
    private static final Integer CYCLE_TIME = 100;

    private static JSch jsch = new JSch();

    /**
     * SSH 方式登录远程主机，执行命令，使用默认端口和默认用户名登录，方法内部会关闭所有资源。
     * @param ip  主机ip
     * @param password  密码
     * @param command   命令
     * @return
     * @throws SSHException
     */
    public static String execute(String ip, String password, String command) throws SSHException{
        return execute(ip, DEFAULT_PORT, DEFAULT_USERNAME, password, command);
    }

    /**
     * SSH 方式登录远程主机，执行命令,方法内部关闭所有资源。
     *
     * @param ip       主机ip
     * @param username 用户名
     * @param password 密码
     * @param command  要执行的命令
     */
    public static String execute(String ip, int port, String username, String password, String command) throws SSHException {
        Assert.notEmpty(ip, "ip不能为空！");
        Assert.notEmpty(command, "command不能为空");
        Assert.notEmpty(username, "username不能为空！");
        Assert.notEmpty(password, "password不能为空！");

        Session session = null;
        ChannelExec channel = null;
        BufferedReader read = null;
        StringBuffer sb = new StringBuffer();
        try {
            session = jsch.getSession(username, ip, port);
            session.setPassword(password);
            session.setUserInfo(new MyUserInfo());
            // It must not be recommended, but if you want to skip host-key checker,
            // invoke following,
            session.setConfig("StrictHostKeyChecking", "no");
            session.connect(SESSION_TIMEOUT);

            channel = (ChannelExec)session.openChannel("exec");
            channel.setCommand(command);
            channel.connect(CHANNEL_TIMEOUT);
            channel.setErrStream(System.err);
            //stdout
            read = new BufferedReader(new InputStreamReader(channel.getInputStream()));
            String line;
            int lineNumber = 1;
            while ((line = read.readLine()) != null) {
                if (lineNumber++ > 1) {
                    sb.append(System.lineSeparator());
                }
                sb.append(line);
            }
            return sb.toString();
        } catch (Exception e) {
            throw new SSHException("SSH远程执行command: " + command + " 出现错误: " + e.getMessage(), e);
        } finally {
            if (null != read) {
                try {
                    read.close();
                } catch (IOException e) {
                }
            }
            if (session != null) {
                session.disconnect();
            }
            if (channel != null) {
                channel.disconnect();
            }
        }
    }


//    /**
//     * 打印ssh错误信息
//     * @param session
//     */
//    private static void printSessionStdErr(String ip, String command, Session session) {
//        if (session == null) {
//            return;
//        }
//        StringBuffer sshErrorMsg = new StringBuffer();
//        BufferedReader read = null;
//        try {
//            read = new BufferedReader(new InputStreamReader(new StreamGobbler(session.getStderr())));
//            String line = null;
//            while ((line = read.readLine()) != null) {
//                sshErrorMsg.append(line);
//            }
//            if (StringUtils.isNotBlank(sshErrorMsg.toString())) {
//                logger.error("ip {} execute command:({}), sshErrorMsg:{}", ip, command, sshErrorMsg.toString());
//            }
//        } catch (IOException e) {
//            logger.error(e.getMessage(), e);
//        } finally {
//            if (read != null) {
//                try {
//                    read.close();
//                } catch (IOException e) {
//                    logger.error(e.getMessage(), e);
//                }
//            }
//        }
//    }


    /**
     * customized userinfo
     */
    private static class MyUserInfo implements UserInfo {
        @Override
        public String getPassphrase() {
            return null;
        }

        @Override
        public String getPassword() {
            return null;
        }

        @Override
        public boolean promptPassword(String s) {
            return false;
        }

        @Override
        public boolean promptPassphrase(String s) {
            return false;
        }

        @Override
        public boolean promptYesNo(String s) {
            return true;
        }

        @Override
        public void showMessage(String s) {
            System.out.println("showMessage:" + s);
        }
    }
}
