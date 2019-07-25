package mayfly.sys.redis.machine;

import mayfly.common.exception.BusinessRuntimeException;
import mayfly.common.result.Result;
import mayfly.common.ssh.SSHTemplate;

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

    private static SSHTemplate template = SSHTemplate.getSshTemplate();

    public static String getMachineInfo(String ip, int port, String username, String password) {
        try {
//            String top = SSHUtils.execute(ip, port, username, password, COMMAND_TOP);
            return "top";
        } catch (Exception e) {
            throw new BusinessRuntimeException(e.getMessage());
        }
    }

    public static void main(String[] args) throws Exception{
        for (int i = 0; i < 50; i++) {
            new Thread(() -> {
                try {
                    Result<String> res = template.execute("118.24.26.101", 22, "root", "", session -> session.executeCommand("top -b -n 1 | head -5"));
                    System.out.println(res.getData());
                    System.out.println();
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
            }).start();

        }
    }

}
