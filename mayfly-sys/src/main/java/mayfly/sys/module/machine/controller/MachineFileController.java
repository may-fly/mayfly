package mayfly.sys.module.machine.controller;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import mayfly.core.exception.BizAssert;
import mayfly.core.log.annotation.Log;
import mayfly.core.log.annotation.NoNeedLogParam;
import mayfly.core.model.result.Response2Result;
import mayfly.core.permission.Permission;
import mayfly.core.util.FileUtils;
import mayfly.core.util.IOUtils;
import mayfly.core.web.WebUtils;
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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * @author meilin.huang
 * @version 1.0
 * @date 2019-11-12 3:16 下午
 */
@Response2Result
@RestController
@RequestMapping("/machines")
@Permission(code = "machine:file")
public class MachineFileController {

    @Autowired
    private MachineFileService machineFileService;
    @Autowired
    private MachineService machineService;

    @Log(value = "获取文件配置列表", level = Log.Level.DEBUG)
    @GetMapping("/{machineId}/files")
    public List<MachineFileDO> files(@PathVariable Long machineId) {
        return machineFileService.listByMachineId(machineId);
    }

    @GetMapping("/{machineId}/files/{id}/read-dir")
    public List<LsVO> ls(@PathVariable Long id, String path) {
        BizAssert.notNull(path, "path不能为空");
        return machineFileService.ls(id, path);
    }

    @GetMapping("/{machineId}/files/{id}/read")
    public String getFileContent(@PathVariable Long id, String path) throws Exception {
        MachineFileDO file = machineFileService.checkFile(id, path);
        return machineService.sftpOperate(file.getMachineId(), channelSftp -> {
            try {
                InputStream inputStream = channelSftp.get(path);
                return new String(IOUtils.readByte(inputStream, true));
            } catch (Exception e) {
                throw BizAssert.newException("写入文件内容失败");
            }
        });
    }

    @Permission(code = "machine:file:write")
    @Log("机器文件下载")
    @GetMapping("/{machineId}/files/{id}/download")
    public void download(@PathVariable Long id, String path, @NoNeedLogParam HttpServletResponse response) {
        MachineFileDO file = machineFileService.checkFile(id, path);
        machineService.sftpOperate(file.getMachineId(), channelSftp -> {
            try {
                WebUtils.downloadStream(response, FileUtils.getFileNameByPath(path), channelSftp.get(path));
            } catch (Exception e) {
                throw BizAssert.newException("写入文件内容失败");
            }
            return null;
        });
    }

    @Permission(code = "machine:file:write")
    @Log("修改文件内容")
    @PostMapping("/{machineId}/files/{id}/write")
    public void updateFileContent(@PathVariable Long id, @RequestBody @Valid MachineConfContentForm form) {
        machineFileService.updateFileContent(id, form.getPath(), form.getContent());
    }

    @Permission
    @Log("新增文件配置")
    @PostMapping("/{machineId}/files")
    public MachineFileDO addConf(@PathVariable Long machineId, @RequestBody @Valid MachineFileForm form) {
        return machineFileService.create(machineId, form);
    }

    @Log("删除文件配置")
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

    @Permission(code = "machine:file:rm")
    @DeleteMapping("/{machineId}/files/{fileId}/rm")
    public void rm(@PathVariable Long fileId, String path) {
        machineFileService.rmFile(fileId, path);
    }
}
