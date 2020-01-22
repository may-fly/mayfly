package mayfly.sys.devops.module.machine.controller;

import mayfly.core.exception.BusinessAssert;
import mayfly.core.log.MethodLog;
import mayfly.core.permission.Permission;
import mayfly.core.result.Result;
import mayfly.core.validation.annotation.Valid;
import mayfly.sys.devops.module.machine.service.MachineFileService;
import mayfly.sys.devops.module.machine.service.MachineService;
import mayfly.sys.devops.module.machine.controller.form.MachineConfContentForm;
import mayfly.sys.devops.module.machine.controller.form.MachineFileForm;
import mayfly.sys.devops.module.machine.controller.form.UploadForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * @author meilin.huang
 * @version 1.0
 * @date 2019-11-12 3:16 下午
 */
@RestController
@RequestMapping("/devops/machines")
@Permission(code = "machineFile:")
public class MachineFileController {

    @Autowired
    private MachineFileService machineFileService;
    @Autowired
    private MachineService machineService;

    @GetMapping("/{machineId}/files")
    public Result<?> files(@PathVariable Integer machineId) {
        return Result.success(machineFileService.listByMachineId(machineId));
    }

    @GetMapping("/files/{id}/ls")
    public Result<?> ls(@PathVariable Integer id, String path) {
        BusinessAssert.notNull(path, "path不能为空");
        return Result.success(machineFileService.ls(id, path));
    }

    @GetMapping("/files/{id}/cat")
    public Result<?> cat(@PathVariable Integer id, String path) {
        return Result.success(machineFileService.getFileContent(id, path));
    }

    @MethodLog("修改文件内容")
    @PutMapping("/files/{id}")
    public Result<?> updateFileContent(@PathVariable Integer id, @RequestBody @Valid MachineConfContentForm form) {
        machineFileService.updateFileContent(id, form.getPath(), form.getContent());
        return Result.success();
    }

    @MethodLog("新增文件配置")
    @PostMapping("/{machineId}/files")
    public Result<?> addConf(@PathVariable Integer machineId, @RequestBody @Valid MachineFileForm form) {
        return Result.success(machineFileService.addFile(machineId, form));
    }

    @MethodLog("删除文件配置")
    @DeleteMapping("/files/{id}")
    public Result<?> delConf(@PathVariable Integer id) {
        machineFileService.deleteById(id);
        return Result.success();
    }

    @PostMapping("/files/upload")
    public Result<?> upload(@Valid UploadForm form) {
        MultipartFile file = form.getFile();
        try {
            machineFileService.uploadFile(form.getFileId(), form.getPath() + "/" + file.getOriginalFilename(), file.getInputStream());
            return Result.success();
        } catch (IOException e) {
            return Result.serverError(e.getMessage());
        }
    }

    @DeleteMapping("/files/{fileId}/rm")
    public Result<?> rm(@PathVariable Integer fileId, String path) {
        machineFileService.rmFile(fileId, path);
        return Result.success();
    }
}
