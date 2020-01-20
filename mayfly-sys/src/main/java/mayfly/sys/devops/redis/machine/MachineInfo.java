package mayfly.sys.devops.redis.machine;

/**
 * @author meilin.huang
 * @version 1.0
 * @date 2019-01-24 3:00 PM
 */
public class MachineInfo {
    /**
     * 机器id
     */
    private long id;

    /**
     * ssh用户名
     */
    private String sshUser;

    /**
     * ssh密码
     */
    private String sshPasswd;

    /**
     * ip地址
     */
    private String ip;

    /**
     * 机房
     */
    private String room;

    /**
     * 内存，单位G
     */
    private int mem;

    /**
     * cpu数量
     */
    private int cpu;

    /**
     * 是否虚机，0否，1是
     */
    private int virtual;

    /**
     * 宿主机ip
     */
    private String realIp;

}
