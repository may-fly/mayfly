package mayfly.core.util.ssh;

import com.jcraft.jsch.ChannelSftp;
import mayfly.core.util.JsonUtils;
import org.junit.Test;

import java.util.Vector;

public class SSHUtilsTest {

    private static SessionInfo info = SessionInfo.builder("118.24.26.101").port(22)
            .password("").username("root").build();

    @Test
    public void exec() {
        try {
            String res = SSHUtils.exec(info, ShellCmd.directoryExist("/usr/local/java"));
            System.out.println(res);
        } catch (SSHException e) {
            System.out.println(e.getMessage());
        }
    }

    @Test
    public void test() throws Exception {
        ChannelSftp sftp = SSHUtils.openChannelSftp(SSHUtils.getSession(info));
        sftp.connect();
        Vector<ChannelSftp.LsEntry> ls = sftp.ls("/");
//        sftp.disconnect();
        System.out.println(JsonUtils.toJSONString(ls));
//        ChannelShell shell = SSHUtils.openChannelShell(SSHUtils.getSession("118.24.26.101", 22, "root", "hml,.111049"));
//        shell.setInputStream(System.in, true);
//        shell.setOutputStream(System.out);
//        shell.connect();
    }

    public static void main(String[] args) {
        try {
            System.out.println(SSHUtils.exec(info, "ls -al /usr/local"));
        } catch (SSHException e) {
            System.out.println(e.getMessage());
        }
    }
}