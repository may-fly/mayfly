package mayfly.sys.redis.machine;

import mayfly.common.exception.BusinessRuntimeException;
import mayfly.common.ssh.SSHException;
import mayfly.common.ssh.SSHUtils;

/**
 * @author meilin.huang
 * @version 1.0
 * @date 2019-01-23 5:16 PM
 */
public class MachineHandler {
    private final static String COMMAND_TOP = "top -b -n 1 | head -5";
    private final static String COMMAND_DF_LH = "df -lh";
    private final static String LOAD_AVERAGE_STRING = "load average: ";
    private final static String CPU_USAGE_STRING = "Cpu(s):";
    private final static String MEM_USAGE_STRING = "Mem:";
    private final static String SWAP_USAGE_STRING = "Swap:";

    public static String getMachineInfo(String ip, int port, String username, String password) {
        try {
            String top = SSHUtils.execute(ip, port, username, password, COMMAND_TOP);
            return top;
        } catch (SSHException e) {
            throw new BusinessRuntimeException(e.getMessage());
        }
    }



    public static void main(String[] args) throws Exception{
        String host = "94.191.96.31";
        int port = 22;
        String username = "root";
        String password = "hml,.111049";
        System.out.println(getMachineInfo(host, port, username, password));
    }
}
