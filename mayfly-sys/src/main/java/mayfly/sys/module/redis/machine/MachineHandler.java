package mayfly.sys.module.redis.machine;

import mayfly.core.exception.BizAssert;
import mayfly.core.exception.BizRuntimeException;
import mayfly.sys.common.utils.ssh.SSHUtils;


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

//    private static SSHTemplate template = SSHTemplate.getSshTemplate();

    public static String getMachineInfo(String ip, int port, String username, String password) {
        try {
//            String top = SSHUtils.execute(ip, port, username, password, COMMAND_TOP);
            return "top";
        } catch (Exception e) {
            throw BizAssert.newBizRuntimeException(e.getMessage());
        }
    }

}
