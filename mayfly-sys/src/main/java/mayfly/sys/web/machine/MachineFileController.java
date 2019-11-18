package mayfly.sys.web.machine;

import mayfly.core.exception.BusinessAssert;
import mayfly.core.log.MethodLog;
import mayfly.core.result.Result;
import mayfly.core.validation.annotation.Valid;
import mayfly.sys.service.machine.MachineFileService;
import mayfly.sys.web.machine.form.MachineConfContentForm;
import mayfly.sys.web.machine.form.MachineFileForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author meilin.huang
 * @version 1.0
 * @date 2019-11-12 3:16 下午
 */
@MethodLog
@RestController
@RequestMapping("/sys/machines")
public class MachineFileController {

    @Autowired
    private MachineFileService machineFileService;

    @GetMapping("/{machineId}/files")
    public Result confs(@PathVariable Integer machineId) {
        return Result.success(machineFileService.listByMachineId(machineId));
    }

    @GetMapping("/files/{id}/ls")
    public Result ls(@PathVariable Integer id, String path) {
        BusinessAssert.notNull(path, "path不能为空");
        return Result.success(machineFileService.ls(id, path));
    }

    @GetMapping("/files/{id}/cat")
    public Result cat(@PathVariable Integer id, String path) {
        return Result.success(machineFileService.getFileContent(id, path));
    }

    @PutMapping("/files/{id}")
    public Result setConf(@PathVariable Integer id, @RequestBody @Valid MachineConfContentForm form) {
        machineFileService.updateFileContent(id, form.getPath(), form.getContent());
        return Result.success();
    }

    @PostMapping("/{machineId}/files")
    public Result addConf(@PathVariable Integer machineId, @RequestBody @Valid MachineFileForm form) {
        return Result.success(machineFileService.addFile(machineId, form));
    }

    @DeleteMapping("/files/{id}")
    public Result delConf(@PathVariable Integer machineId, @PathVariable Integer id) {
        machineFileService.deleteById(id);
        return Result.success();
    }
}
