package mayfly.sys.module.machine.service.impl;

import com.jcraft.jsch.Session;
import lombok.Data;
import lombok.experimental.Accessors;
import mayfly.core.base.service.impl.BaseServiceImpl;
import mayfly.core.exception.BizAssert;
import mayfly.core.exception.BizRuntimeException;
import mayfly.core.util.ArrayUtils;
import mayfly.core.util.BracePlaceholder;
import mayfly.core.util.IOUtils;
import mayfly.core.util.ResourceUtils;
import mayfly.core.util.bean.BeanUtils;
import mayfly.sys.common.utils.ssh.SSHException;
import mayfly.sys.common.utils.ssh.SSHUtils;
import mayfly.sys.common.utils.ssh.SessionInfo;
import mayfly.sys.module.machine.controller.form.MachineForm;
import mayfly.sys.module.machine.entity.MachineDO;
import mayfly.sys.module.machine.mapper.MachineMapper;
import mayfly.sys.module.machine.service.MachineFileService;
import mayfly.sys.module.machine.service.MachineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author meilin.huang
 * @version 1.0
 * @date 2019-11-04 3:04 下午
 */
@Service
public class MachineServiceImpl extends BaseServiceImpl<MachineMapper, Long, MachineDO> implements MachineService {

    private static final Map<String, String> SHELL_CACHE = new ConcurrentHashMap<>();

    @Autowired
    private MachineFileService machineFileService;

    @Override
    public void create(MachineForm form) {
        // 校验机器是否存在以及是否可以登录
        try {
            Session session = SSHUtils.openSession(SessionInfo.builder(form.getIp()).port(form.getPort())
                    .password(form.getPassword()).username(form.getUsername()).build());
            session.disconnect();
        } catch (SSHException e) {
            throw BizAssert.newBizRuntimeException("信息不正确：" + e.getMessage());
        }

        MachineDO machine = BeanUtils.copyProperties(form, MachineDO.class);
        insert(machine);
    }

    @Override
    public String runShell(Long machineId, String shellFileName, Object... param) {
        String shellContent = getShellContent(shellFileName);
        if (!ArrayUtils.isEmpty(param)) {
            shellContent = String.format(shellContent, param);
        }
        BizAssert.notEmpty(shellContent, "不存在该shell脚本");
        return exec(machineId, shellContent);
    }

    @Override
    public TopInfo getTopInfo(Long machineId) {
        String res = exec(machineId, "top -b -n 1 | head -5");
        Map<String, String> re = new HashMap<>();
        BracePlaceholder.reverTemplate(TopInfo.TOP_TEMP, res, re);

        //17:14:07 up 5 days,  6:30,  2
        String timeUpAndUserStr = re.get("upAndUsers");
        String[] timeUpAndUser = timeUpAndUserStr.split("up");
        String time = timeUpAndUser[0].trim();
        String[] upAndUsers = timeUpAndUser[1].split(",");
        String up = upAndUsers[0].trim() + upAndUsers[1];
        int users = Integer.parseInt(upAndUsers[2].trim().split(" ")[0]);
        // 0.03, 0.04, 0.05
        String[] loadavgs = re.get("loadavg").split(",");
        float oneMinLa = Float.parseFloat(loadavgs[0].trim());
        float fiveMinLa = Float.parseFloat(loadavgs[1].trim());
        float fifMinLa = Float.parseFloat(loadavgs[2].trim());
        return new TopInfo().setUp(up).setTime(time).setNowUsers(users).setOneMinLoadavg(oneMinLa).setFiveMinLoadavg(fiveMinLa).setFifteenMinLoadavg(fifMinLa)
                .setTotalTask(Integer.parseInt(re.get("totalTask")))
                .setTotalMem(Integer.parseInt(re.get("totalMem")))
                .setTotalSwap(Integer.parseInt(re.get("totalSwap")))
                .setFreeMem(Integer.parseInt(re.get("freeMem")))
                .setFreeSwap(Integer.parseInt(re.get("freeSwap")))
                .setAvailMem(Integer.parseInt(re.get("availMem")))
                .setUsedMem(Integer.parseInt(re.get("usedMem")))
                .setRunningTask(Integer.parseInt(re.get("runningTask")))
                .setSleepingTask(Integer.parseInt(re.get("sleepingTask")))
                .setStoppedTask(Integer.parseInt(re.get("stoppedTask")))
                .setZombieTask(Integer.parseInt(re.get("zombieTask")))
                .setCacheMem(Integer.parseInt(re.get("cacheMem")))
                .setCpuHi(Float.parseFloat(re.get("cpuHi")))
                .setCpuId(Float.parseFloat(re.get("cpuId")))
                .setCpuNi(Float.parseFloat(re.get("cpuNi")))
                .setCpuSi(Float.parseFloat(re.get("cpuSi")))
                .setCpuSt(Float.parseFloat(re.get("cpuSt")))
                .setCpuSy(Float.parseFloat(re.get("cpuSy")))
                .setCpuUs(Float.parseFloat(re.get("cpuUs")))
                .setCpuWa(Float.parseFloat(re.get("cpuWa")));
    }

    /**
     * 获取类路径下的shell脚本内容
     *
     * @param shellFileName shell文件名
     * @return 文件内容
     */
    private String getShellContent(String shellFileName) {
        return SHELL_CACHE.computeIfAbsent(shellFileName, key -> {
            List<URL> resources = ResourceUtils.getResources("shell/" + key + ".sh");
            for (URL url : resources) {
                try {
                    return IOUtils.read(url.openStream());
                } catch (Exception e) {
                    throw BizAssert.newBizRuntimeException("读取shell文件失败");
                }
            }
            return null;
        });
    }

    @Accessors(chain = true)
    @Data
    public static class TopInfo {
        public static final String TOP_TEMP = "top - {upAndUsers},  load average: {loadavg}\n" +
                "Tasks:{totalTask} total,{runningTask} running,{sleepingTask} sleeping,{stoppedTask} stopped,{zombieTask} zombie\n" +
                "%Cpu(s):{cpuUs} us,{cpuSy} sy,{cpuNi} ni,{cpuId} id,{cpuWa} wa,{cpuHi} hi,{cpuSi} si,{cpuSt} st\n" +
                "KiB Mem :{totalMem} total,{freeMem} free,{usedMem} used,{cacheMem} buff/cache\n" +
                "KiB Swap:{totalSwap} total,{freeSwap} free,{usedSwap} used. {availMem} avail Mem";

        private String time;
        // 从本次开机到现在经过的时间
        private String up;
        // 当前有几个用户登录到该机器
        private int nowUsers;
        // load average: 0.03, 0.04, 0.05 (系统1分钟、5分钟、15分钟内的平均负载值)
        private float oneMinLoadavg;
        private float fiveMinLoadavg;
        private float fifteenMinLoadavg;
        // 进程总数
        private int totalTask;
        // 正在运行的进程数，对应状态TASK_RUNNING
        private int runningTask;
        private int sleepingTask;
        private int stoppedTask;
        private int zombieTask;
        // 进程在用户空间（user）消耗的CPU时间占比，不包含调整过优先级的进程
        private float cpuUs;
        // 进程在内核空间（system）消耗的CPU时间占比
        private float cpuSy;
        // 调整过用户态优先级的（niced）进程的CPU时间占比
        private float cpuNi;
        // 空闲的（idle）CPU时间占比
        private float cpuId;
        // 等待（wait）I/O完成的CPU时间占比
        private float cpuWa;
        // 处理硬中断（hardware interrupt）的CPU时间占比
        private float cpuHi;
        // 处理硬中断（hardware interrupt）的CPU时间占比
        private float cpuSi;
        // 当Linux系统是在虚拟机中运行时，等待CPU资源的时间（steal time）占比
        private float cpuSt;

        private int totalMem;
        private int freeMem;
        private int usedMem;
        private int cacheMem;

        private int totalSwap;
        private int freeSwap;
        private int usedSwap;
        private int availMem;
    }
}
