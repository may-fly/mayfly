package mayfly.sys.module.machine.controller;

import mayfly.core.base.model.Response2Result;
import mayfly.core.exception.BizAssert;
import mayfly.core.util.enums.EnumUtils;
import mayfly.sys.module.machine.enums.MonitorTimeEnum;
import mayfly.sys.module.machine.service.MachineMonitorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * @author meilin.huang
 * @date 2020-08-31 4:57 下午
 */
@Response2Result
@RestController
@RequestMapping("/devops/machines/")
public class MachineMonitorController {

    @Autowired
    private MachineMonitorService machineMonitorService;

    @GetMapping("/{id}/{type}/monitors")
    public List<Map<String, Object>> monitors(@PathVariable Long id, @PathVariable Integer type) {
        MonitorTimeEnum timeValue = EnumUtils.getEnumByValue(MonitorTimeEnum.values(), type);
        BizAssert.notNull(timeValue, "type错误");
        String[] beginAndEndTime = timeValue.getBeginAndEndTime();
        return machineMonitorService.listByQuery(id, beginAndEndTime[0], beginAndEndTime[1]);
    }
}
