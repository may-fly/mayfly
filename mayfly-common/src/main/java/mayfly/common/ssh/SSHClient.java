package mayfly.common.ssh;


import com.jcraft.jsch.*;
import mayfly.common.utils.Assert;

import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.TimeUnit;

/**
 * @author meilin.huang
 * @version 1.0
 * @date 2019-01-24 10:03 AM
 */
public class SSHClient {

    public static final String DEFAULT_HOST = "localhost";

    public static final int DEFAULT_PORT = 22;

    public static final String DEFAULT_USERNAME = "root";

    /**
     * timeout for session connection
     */
    private final Integer SESSION_TIMEOUT = 30000;

    /**
     * timeout for channel connection
     */
    private final Integer CHANNEL_TIMEOUT = 30000;

    /**
     * the interval for acquiring ret
     */
    private final Integer CYCLE_TIME = 100;

    /**
     * Server Host IP Address，default value is localhost
     */
    private String host = DEFAULT_HOST;

    /**
     * Server SSH Port，default value is 22
     */
    private Integer port = DEFAULT_PORT;

    /**
     * SSH Login Username
     */
    private String username = DEFAULT_USERNAME;

    /**
     * SSH Login Password
     */
    private String password;

    /**
     * JSch
     */
    private static JSch jsch = new JSch();

    /**
     * ssh session
     */
    private Session session;

    /**
     * ssh channel
     */
//    private Channel channel;

    private SSHClient(String host, int port, String username, String password) {
        this.host = host;
        this.port = port;
        this.username = username;
        this.password = password;
    }


    public static SSHClient getClient(String host, int port, String username, String password) {
        return new SSHClient(host, port, username, password);
    }


    /**
     * login to server
     *
     * @param username
     * @param password
     */
    public void login(String username, String password) {
        Assert.notEmpty(username, "uesrname不能为空！");
        Assert.notEmpty(password, "password不能为空！");

        this.username = username;
        this.password = password;
        try {
            if (session == null || !session.isConnected()) {
                session = jsch.getSession(this.username, this.host, this.port);
                session.setPassword(this.password);
                session.setUserInfo(new MyUserInfo());
                // It must not be recommended, but if you want to skip host-key checker,
                // invoke following,
                session.setConfig("StrictHostKeyChecking", "no");
            }
            session.connect(SESSION_TIMEOUT);
        } catch (JSchException e) {
            this.logout();
        }
    }

    public void login() {
        login(this.username, this.password);
    }

    /**
     * logout of server
     */
    public void logout() {
        session.disconnect();
    }

    /**
     * send command through the ssh session,return the ret of the channel
     *
     * @return
     */
    public String sendCmd(String command) {
        String ret = "";

        // judge whether the session or channel is connected
        if (!session.isConnected()) {
            this.login();
        }
        // open channel for sending command
        ChannelExec channel = null;
        try {
            channel = (ChannelExec)session.openChannel("exec");
            channel.setCommand(command);
            channel.connect(CHANNEL_TIMEOUT);

            // no output stream
            channel.setInputStream(null);

            channel.setErrStream(System.err);

            InputStream in = channel.getInputStream();
//            StringBuilder sb = new StringBuilder();
//            //stdout
//            BufferedReader read = new BufferedReader(new InputStreamReader(in));
//            String line = "";
//            int lineNumber = 1;
//            while ((line = read.readLine()) != null) {
////                sb.append(line).append(BR);
//                if (lineNumber++ > 1) {
//                    sb.append(System.lineSeparator());
//                }
//                sb.append(line);
//            }
//            return sb.toString();
            // acquire for ret
            byte[] tmp = new byte[1024];
            while (true) {
                while (in.available() > 0) {
                    int i = in.read(tmp, 0, 1024);
                    if (i < 0) {
                        break;
                    }
                    ret = new String(tmp, 0, i);
                }
                // quit the process of waiting for ret
                if (channel.isClosed()) {
                    if (in.available() > 0) {
                        continue;
                    }
                    break;
                }
                // wait every 100ms
                try {
                    TimeUnit.MILLISECONDS.sleep(CYCLE_TIME);
                } catch (Exception ee) {

                }
            }
        } catch (JSchException e) {
            e.printStackTrace();
            System.err.println(e);
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println(e);
        } finally {
            // close channel
            channel.disconnect();
            logout();
        }

        return ret;
    }



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
