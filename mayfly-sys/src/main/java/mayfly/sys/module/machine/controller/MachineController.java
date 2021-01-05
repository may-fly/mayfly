package mayfly.sys.module.machine.controller;

import mayfly.core.model.result.Response2Result;
import mayfly.core.log.MethodLog;
import mayfly.core.permission.Permission;
import mayfly.core.util.bean.BeanUtils;
import mayfly.sys.module.machine.controller.form.MachineForm;
import mayfly.sys.module.machine.controller.vo.MachineVO;
import mayfly.sys.module.machine.service.MachineService;
import mayfly.sys.module.machine.service.impl.MachineServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

/**
 * @author meilin.huang
 * @version 1.0
 * @date 2019-11-04 3:07 下午
 */
@Response2Result
@Permission(code = "machine")
@RestController
@RequestMapping("/devops/machines")
public class MachineController {

    @Autowired
    private MachineService machineService;

    @MethodLog(value = "获取机器列表", level = MethodLog.LogLevel.DEBUG)
    @GetMapping()
    public List<MachineVO> list() {
        return BeanUtils.copyProperties(machineService.listAll(), MachineVO.class);
    }

    @GetMapping("/{id}/sysinfo")
    public String info(@PathVariable Long id) {
        return machineService.runShell(id, "system_info");
    }

    @GetMapping("/{id}/top")
    public MachineServiceImpl.TopInfo top(@PathVariable Long id) {
        return machineService.getTopInfo(id);
    }

    @MethodLog("机器管理：新增机器")
    @PostMapping()
    public void save(@RequestBody @Valid MachineForm form) {
        machineService.create(form);
    }

    @MethodLog("机器管理：更新机器")
    @PutMapping("/{id}")
    public void update(@PathVariable Long id, @RequestBody @Valid MachineForm form) {
        form.setId(id);
        machineService.create(form);
    }

    @MethodLog("机器管理：删除机器")
    @DeleteMapping("/{machineId}")
    public void delete(@PathVariable Long machineId) {
        machineService.deleteById(machineId);
    }
}
