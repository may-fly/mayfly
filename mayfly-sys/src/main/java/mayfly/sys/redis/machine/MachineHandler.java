package mayfly.sys.redis.machine;

import mayfly.core.exception.BusinessRuntimeException;
import mayfly.core.ssh.SSHTemplate;
import mayfly.core.validation.ParamValidErrorException;
import mayfly.core.validation.ValidationHandler;
import mayfly.core.validation.annotation.NotBlank;
import mayfly.core.validation.annotation.NotNull;
import mayfly.core.validation.annotation.Size;

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

    static class User{

        @NotBlank
        private String name;

        @NotNull(message = "性别不能为空")
        @Size(max = 100, min = 1)
        private Integer sex;

        @NotBlank
        private String name1;

        @NotNull(message = "性别不能为空")
        @Size(max = 100, min = 1)
        private Integer sex1;

        @NotBlank
        private String name22;

        @NotNull(message = "性别不能为空")
        @Size(max = 100, min = 1)
        private Integer sex2;

        @NotBlank
        private String name3;

        @NotNull(message = "性别不能为空")
        @Size(max = 100, min = 1)
        private Integer sex4;

        @NotBlank
        private String name5;

        @NotNull(message = "性别不能为空")
        @Size(max = 100, min = 1)
        private Integer sex6;


        private Integer method;
    }

    public static void main(String[] args) throws Exception {
//        for (int i = 0; i < 50; i++) {
//            new Thread(() -> {
//                try {
//                    Result<String> res = template.execute("118.24.26.101", 22, "root", "", session -> session.executeCommand("top -b -n 1 | head -5"));
//                    System.out.println(res.getData());
//                    System.out.println();
//                } catch (Exception e) {
//                    System.out.println(e.getMessage());
//                }
//            }).start();
//
//        }

        User user = new User();
        user.name = "1";
        user.sex = 100;
        user.sex6 = 1001;
        long start = System.currentTimeMillis();
        for (int i = 0; i < 100000; i++) {
            try {
                ValidationHandler.getInstance().validate(user);
            } catch (ParamValidErrorException e) {
                System.out.println(e.getMessage());
            }
        }
        long end = System.currentTimeMillis();
        System.out.println(end - start);
    }

}
