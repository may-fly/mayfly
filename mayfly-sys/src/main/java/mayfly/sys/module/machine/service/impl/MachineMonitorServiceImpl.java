package mayfly.sys.module.machine.service.impl;

import lombok.extern.slf4j.Slf4j;
import mayfly.core.base.service.impl.BaseServiceImpl;
import mayfly.core.util.BracePlaceholder;
import mayfly.sys.module.machine.entity.MachineDO;
import mayfly.sys.module.machine.entity.MachineMonitorDO;
import mayfly.sys.module.machine.mapper.MachineMonitorMapper;
import mayfly.sys.module.machine.service.MachineMonitorService;
import mayfly.sys.module.machine.service.MachineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * @author meilin.huang
 * @date 2020-08-31 2:25 下午
 */
@Slf4j
@Service
public class MachineMonitorServiceImpl extends BaseServiceImpl<MachineMonitorMapper, Long, MachineMonitorDO> implements MachineMonitorService {

    private static final String MONITOR_TEMP = "cpuRate:{cpuRate}%,memRate:{memRate}%,sysLoad:{sysLoad}\n";

    @Autowired
    private MachineService machineService;

    @Override
    public void getAndSaveMonitor() {
        List<MachineDO> allMachines = machineService.listAll();
        allMachines.forEach(m -> {
            if (Objects.equals(m.getNeedMonitor(), 1)) {
                try {
                    this.insert(getMonitor(m.getId()));
                } catch (Exception e) {
                    log.info("获取监控信息失败：", e);
                }
            }
        });
    }

    @Override
    public List<Map<String, Object>> listByQuery(Long machineId, String beginTime, String endTime) {
        return mapper.selectByDate(machineId, beginTime, endTime);
    }

    private MachineMonitorDO getMonitor(Long machineId) {
        String monitor = machineService.runShell(machineId, "monitor");
        Map<String, String> re = new HashMap<>();
        BracePlaceholder.reverTemplate(MONITOR_TEMP, monitor, re);
        String sysLoad = re.get("sysLoad");
        String[] loads = sysLoad.split(",");
        return new MachineMonitorDO().setMachineId(machineId)
                .setCpuRate(Float.parseFloat(re.get("cpuRate")))
                .setMemRate(Float.parseFloat(re.get("memRate")))
                .setOneMinLoadavg(Float.parseFloat(loads[0].trim()))
                .setFiveMinLoadavg(Float.parseFloat(loads[1].trim()))
                .setFifMinLoadavg(Float.parseFloat(loads[2].trim()));
    }
}
