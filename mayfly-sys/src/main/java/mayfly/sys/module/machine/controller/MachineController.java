package mayfly.sys.module.machine.controller;

import mayfly.core.log.Log;
import mayfly.core.model.PageQuery;
import mayfly.core.model.result.PageResult;
import mayfly.core.model.result.Response2Result;
import mayfly.core.permission.Permission;
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

/**
 * @author meilin.huang
 * @version 1.0
 * @date 2019-11-04 3:07 下午
 */
@Response2Result
@Permission(code = "machine")
@RestController
@RequestMapping("/machines")
public class MachineController {

    @Autowired
    private MachineService machineService;

    @Log(value = "获取机器列表", level = Log.LogLevel.DEBUG)
    @GetMapping()
    public PageResult<MachineVO> list(PageQuery query) {
        return PageResult.withPageHelper(query, () -> machineService.listAll() , MachineVO.class);
    }

    @GetMapping("/{id}/sysinfo")
    public String info(@PathVariable Long id) {
        return machineService.runShell(id, "system_info");
    }

    @GetMapping("/{id}/top")
    public MachineServiceImpl.TopInfo top(@PathVariable Long id) {
        return machineService.getTopInfo(id);
    }

    @Log("机器管理：新增机器")
    @PostMapping()
    public void add(@RequestBody @Valid MachineForm form) {
        machineService.create(form);
    }

    @Log("机器管理：更新机器")
    @PutMapping("/{id}")
    public void update(@PathVariable Long id, @RequestBody @Valid MachineForm form) {
        form.setId(id);
        machineService.create(form);
    }

    @Log("机器管理：删除机器")
    @DeleteMapping("/{machineId}")
    public void del(@PathVariable Long machineId) {
        machineService.deleteById(machineId);
    }
}
