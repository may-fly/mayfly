package mayfly.sys.module.redis.machine;

import java.util.Map;

/**
 * @author meilin.huang
 * @version 1.0
 * @date 2019-01-23 5:15 PM
 */
public class MachineStats {
    private long id;

    private long hostId;

    private String ip;

    private String cpuUsage;

    private String load;

    private String traffic;

    private String memoryUsageRatio;

    private String memoryFree;

    private int memoryAllocated;

    private String memoryTotal;

    private MachineInfo info;

    /**
     * 挂载点 --> 使用百分比
     */
    private Map<String, String> diskUsageMap;
}
