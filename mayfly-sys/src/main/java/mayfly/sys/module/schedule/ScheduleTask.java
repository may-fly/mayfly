package mayfly.sys.module.schedule;

import lombok.extern.slf4j.Slf4j;
import mayfly.sys.module.machine.service.MachineMonitorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * @author meilin.huang
 * @date 2020-08-31 2:38 下午
 */
@Slf4j
@Component
public class ScheduleTask {
    @Autowired
    private MachineMonitorService machineMonitorService;

    /**
     * 每分钟执行一次
     */
    @Scheduled(cron = "0 */1 * * * ?")
    public void saveMonitor() {
        try {
            machineMonitorService.getAndSaveMonitor();
        } catch (Exception e) {
            log.error("定时获取机器监控信息失败：", e);
        }
    }

}
