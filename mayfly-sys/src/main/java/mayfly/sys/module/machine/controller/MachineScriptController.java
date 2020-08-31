package mayfly.sys.module.machine.controller;

import mayfly.core.base.model.Result;
import mayfly.core.exception.BizAssert;
import mayfly.core.util.enums.EnumUtils;
import mayfly.sys.module.machine.enums.MonitorTimeEnum;
import mayfly.sys.module.machine.service.MachineMonitorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author meilin.huang
 * @version 1.0
 * @date 2020-01-15 4:11 下午
 */
@RequestMapping("/devops/machines")
@RestController
public class MachineScriptController {

    @Autowired
    private MachineMonitorService machineMonitorService;

    @GetMapping("/{id}/{type}/monitors")
    public Result<?> monitors(@PathVariable Long id, @PathVariable Integer type) {
        MonitorTimeEnum timeValue = EnumUtils.getEnumByValue(MonitorTimeEnum.values(), type);
        BizAssert.notNull(timeValue, "type错误");
        String[] beginAndEndTime = timeValue.getBeginAndEndTime();
        return Result.success(machineMonitorService.listByQuery(id, beginAndEndTime[0], beginAndEndTime[1]));
    }
}
