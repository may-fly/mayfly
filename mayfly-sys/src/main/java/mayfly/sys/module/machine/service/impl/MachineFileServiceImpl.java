package mayfly.sys.module.machine.service.impl;

import mayfly.core.base.service.impl.BaseServiceImpl;
import mayfly.core.exception.BizAssert;
import mayfly.core.permission.LoginAccount;
import mayfly.core.thread.GlobalThreadPool;
import mayfly.core.util.bean.BeanUtils;
import mayfly.sys.common.utils.ssh.ShellCmd;
import mayfly.sys.common.websocket.MessageTypeEnum;
import mayfly.sys.common.websocket.SessionNoFoundException;
import mayfly.sys.common.websocket.WebSocketUtils;
import mayfly.sys.module.machine.controller.form.MachineFileForm;
import mayfly.sys.module.machine.controller.vo.LsVO;
import mayfly.sys.module.machine.entity.MachineFileDO;
import mayfly.sys.module.machine.enums.MachineFileTypeEnum;
import mayfly.sys.module.machine.mapper.MachineFileMapper;
import mayfly.sys.module.machine.service.MachineFileService;
import mayfly.sys.module.machine.service.MachineService;
import mayfly.sys.module.sys.enums.LogTypeEnum;
import mayfly.sys.module.sys.service.OperationLogService;
import mayfly.sys.module.websocket.SysMsgWebSocket;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @author meilin.huang
 * @version 1.0
 * @date 2019-11-04 3:04 下午
 */
@Service
public class MachineFileServiceImpl extends BaseServiceImpl<MachineFileMapper, Long, MachineFileDO> implements MachineFileService {

    public static char file = '-';
    public static char directory = 'd';
    public static char link = 'l';


    @Autowired
    private MachineFileMapper machineFileMapper;
    @Autowired
    private MachineService machineService;
    @Autowired
    private OperationLogService operationLogService;


    @Override
    public List<MachineFileDO> listByMachineId(Long machineId) {
        return this.listByCondition(new MachineFileDO().setMachineId(machineId));
    }

    @Override
    public String getFileContent(Long fileId, String path) {
        MachineFileDO file = getById(fileId);
        checkPath(path, file);
        return machineService.runShell(file.getMachineId(), "read_text_file", path);

    }

    @Override
    public void updateFileContent(Long confId, String path, String content) {
        BizAssert.notEmpty(content, "内容不能为空");
        MachineFileDO file = getById(confId);
        checkPath(path, file);
        machineService.exec(file.getMachineId(), "echo '" + content + "' >" + path);
    }

    @Override
    public MachineFileDO create(Long machineId, MachineFileForm form) {
        boolean isFile = Objects.equals(form.getType(), MachineFileTypeEnum.FILE.getValue());
        String res = machineService.exec(machineId, isFile ? ShellCmd.fileExist(form.getPath()) : ShellCmd.directoryExist(form.getPath()));
        BizAssert.equals(res, "1\n", () -> isFile ? "该文件不存在" : "该目录不存在");

        MachineFileDO file = BeanUtils.copyProperties(form, MachineFileDO.class);
        file.setMachineId(machineId);
        file.setCreateTime(LocalDateTime.now());
        insert(file);
        return file;
    }

    @Override
    public List<LsVO> ls(Long fileId, String path) {
        MachineFileDO machineFile = getById(fileId);
        checkPath(path, machineFile);

        List<LsVO> ls = new ArrayList<>(16);
        String pathPrefix = path.endsWith("/") ? path : path + "/";
        // -rw-r--r-- 1 root root      433 Mar  3 14:07 Dockerfile 解析该文本格式
        machineService.exec(machineFile.getMachineId(), "ls -Alh " + path, (lineNum, lineContent) -> {
            if (lineNum == 1) {
                return;
            }
            String[] strs = lineContent.split("\\s+");
            char[] typeAndPermission = strs[0].toCharArray();
            char type = typeAndPermission[0];
            if (type == link) {
                return;
            }
            LsVO vo = new LsVO();
            String fileName = strs[strs.length - 1];
            vo.setPath(pathPrefix + fileName);
            vo.setType(typeAndPermission[0]);
            vo.setName(fileName);
            // 如果是文件，则设置文件大小
            if (type == file) {
                vo.setSize(strs[4]);
            }
            ls.add(vo);
        });
        return ls;
    }


    @Override
    public void uploadFile(Long fileId, String filePath, InputStream inputStream) {
        MachineFileDO file = getById(fileId);
        checkPath(filePath, file);

        LoginAccount account = LoginAccount.getFromContext();
        // 异步上传，成功与否都webscoket通知上传者
        GlobalThreadPool.execute("machineFileUploadNotify", () -> {
            machineService.sftpOperate(file.getMachineId(), sftp -> {
                try {
                    sftp.put(inputStream, filePath);
                    // 记录日志并websocket通知客户端
                    String msg = filePath + "文件上传成功";
                    operationLogService.asyncLog(msg, LogTypeEnum.UPDATE, account);
                    WebSocketUtils.sendText(SysMsgWebSocket.URI, account.getId(), MessageTypeEnum.SUCCESS.toMsg(msg));
                } catch (Exception e) {
                    try {
                        String msg = "文件上传失败：" + e.getMessage();
                        operationLogService.asyncLog(msg, LogTypeEnum.ERR_LOG, account);
                        WebSocketUtils.sendText(SysMsgWebSocket.URI, account.getId(), MessageTypeEnum.ERROR.toMsg(msg));
                    } catch (SessionNoFoundException se) {
                        //
                    }
                }
                return null;
            });
        });
    }

    @Override
    public void rmFile(Long fileId, String path) {
        MachineFileDO file = getById(fileId);
        checkPath(path, file);
        machineService.exec(file.getMachineId(), "rm -rf " + path);
    }


    private boolean isFile(MachineFileDO file) {
        return Objects.equals(file.getType(), MachineFileTypeEnum.FILE.getValue());
    }

    private boolean isDirectory(MachineFileDO file) {
        return Objects.equals(file.getType(), MachineFileTypeEnum.DIRECTORY.getValue());
    }

    /**
     * 校验前端传过来的path，是否符合配置的路径。即为目录的话，该path必须为配置目录的子目录文件
     *
     * @param path
     * @param file
     */
    private void checkPath(String path, MachineFileDO file) {
        BizAssert.notNull(file, "配置信息不存在");
        if (isDirectory(file)) {
            // 访问的文件路径必须是在配置的子目录下
            BizAssert.isTrue(path.startsWith(file.getPath()), "无法访问该文件");
        } else {
            BizAssert.equals(path, file.getPath(), "文件路径错误");
            BizAssert.isTrue(isFile(file), "该路径为目录，非文件");
        }
    }
}
