package mayfly.sys.module.machine.controller;

import mayfly.core.model.result.Response2Result;
import mayfly.core.exception.BizAssert;
import mayfly.core.log.MethodLog;
import mayfly.core.permission.Permission;
import mayfly.sys.module.machine.controller.form.MachineConfContentForm;
import mayfly.sys.module.machine.controller.form.MachineFileForm;
import mayfly.sys.module.machine.controller.form.UploadForm;
import mayfly.sys.module.machine.controller.vo.LsVO;
import mayfly.sys.module.machine.entity.MachineFileDO;
import mayfly.sys.module.machine.service.MachineFileService;
import mayfly.sys.module.machine.service.MachineService;
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

import javax.validation.Valid;
import java.io.IOException;
import java.util.List;

/**
 * @author meilin.huang
 * @version 1.0
 * @date 2019-11-12 3:16 下午
 */
@Response2Result
@RestController
@RequestMapping("/devops/machines")
@Permission(code = "machineFile")
public class MachineFileController {

    @Autowired
    private MachineFileService machineFileService;
    @Autowired
    private MachineService machineService;

    @MethodLog(value = "获取文件配置列表", level = MethodLog.LogLevel.DEBUG)
    @GetMapping("/{machineId}/files")
    public List<MachineFileDO> files(@PathVariable Long machineId) {
        return machineFileService.listByMachineId(machineId);
    }

    @GetMapping("/files/{id}/ls")
    public List<LsVO> ls(@PathVariable Long id, String path) {
        BizAssert.notNull(path, "path不能为空");
        return machineFileService.ls(id, path);
    }

    @GetMapping("/files/{id}/cat")
    public String cat(@PathVariable Long id, String path) {
        return machineFileService.getFileContent(id, path);
    }

    @Permission
    @MethodLog("修改文件内容")
    @PutMapping("/files/{id}")
    public void updateFileContent(@PathVariable Long id, @RequestBody @Valid MachineConfContentForm form) {
        machineFileService.updateFileContent(id, form.getPath(), form.getContent());
    }

    @Permission
    @MethodLog("新增文件配置")
    @PostMapping("/{machineId}/files")
    public MachineFileDO addConf(@PathVariable Long machineId, @RequestBody @Valid MachineFileForm form) {
        return machineFileService.create(machineId, form);
    }

    @MethodLog("删除文件配置")
    @DeleteMapping("/files/{id}")
    public void delConf(@PathVariable Long id) {
        machineFileService.deleteById(id);
    }

    @Permission
    @PostMapping("/files/upload")
    public void upload(@Valid UploadForm form) {
        MultipartFile file = form.getFile();
        try {
            machineFileService.uploadFile(form.getFileId(), form.getPath() + "/" + file.getOriginalFilename(), file.getInputStream());
        } catch (IOException e) {
            throw BizAssert.newException(e.getMessage());
        }
    }

    @Permission
    @DeleteMapping("/files/{fileId}/rm")
    public void rm(@PathVariable Long fileId, String path) {
        machineFileService.rmFile(fileId, path);
    }
}
