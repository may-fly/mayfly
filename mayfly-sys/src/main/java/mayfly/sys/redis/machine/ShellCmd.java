package mayfly.sys.redis.machine;

import mayfly.common.util.PlaceholderResolver;

/**
 * @author meilin.huang
 * @version 1.0
 * @date 2019-04-08 14:42
 */
public class ShellCmd {

    public static PlaceholderResolver resolver = PlaceholderResolver.getDefaultResolver();

    /**
     * 根据端口号kill进程
     * @param port
     * @return
     */
    public static String killByPort(int port) {
        String shell = "PORT=${port}\n" +
                "PID=`netstat -nlp | grep \":$PORT\" | awk '{print $7}' | awk -F\"/\" '{ print $1 }'`\n" +
                "if [ -z \"$PID\" ]; then\n" +
                "    echo \"端口为:$PORT的程序不存在!\"\n" +
                "    exit 1\n" +
                "fi\n" +
                "kill -9 $PID";
        return resolver.resolve(shell, String.valueOf(port));
    }

    /**
     * 根据进程名kill进程
     * @param name
     * @return
     */
    public static String killByName(String name) {
        String shell = "NAME=${name}\n" +
                "ID=`ps -ef | grep \"$NAME\" | grep -v \"$0\" | grep -v \"grep\" | awk '{print $2}'`\n" +
                "for id in $ID\n" +
                "do\n" +
                "kill -9 $id\n" +
                "echo \"killed $id\"\n" +
                "done\n";
        return resolver.resolve(shell, name);
    }
}
