package mayfly.sys.module.machine.service.impl;

import mayfly.core.base.service.impl.BaseServiceImpl;
import mayfly.core.exception.BusinessAssert;
import mayfly.core.permission.LoginAccount;
import mayfly.core.thread.GlobalThreadPool;
import mayfly.core.util.bean.BeanUtils;
import mayfly.sys.common.utils.ssh.ShellCmd;
import mayfly.sys.common.websocket.MessageTypeEnum;
import mayfly.sys.common.websocket.SessionNoFoundException;
import mayfly.sys.common.websocket.WebSocketUtils;
import mayfly.sys.module.machine.controller.form.MachineFileForm;
import mayfly.sys.module.machine.controller.vo.LsVO;
import mayfly.sys.module.machine.entity.MachineFile;
import mayfly.sys.module.machine.enums.MachineFileTypeEnum;
import mayfly.sys.module.machine.mapper.MachineFileMapper;
import mayfly.sys.module.machine.service.MachineFileService;
import mayfly.sys.module.machine.service.MachineService;
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
public class MachineFileServiceImpl extends BaseServiceImpl<MachineFileMapper, MachineFile> implements MachineFileService {

    public static char file = '-';
    public static char directory = 'd';
    public static char link = 'l';


    @Autowired
    private MachineFileMapper machineFileMapper;
    @Autowired
    private MachineService machineService;

    @Autowired
    @Override
    protected void setBaseMapper() {
        super.baseMapper = machineFileMapper;
    }

    @Override
    public List<MachineFile> listByMachineId(Integer machineId) {
        return this.listByCondition(MachineFile.builder().machineId(machineId).build());
    }

    @Override
    public String getFileContent(Integer fileId, String path) {
        MachineFile file = getById(fileId);
        checkPath(path, file);

        return machineService.exec(file.getMachineId(), "cat " + path);

    }

    @Override
    public void updateFileContent(Integer confId, String path, String content) {
        BusinessAssert.notEmpty(content, "内容不能为空");
        MachineFile file = getById(confId);
        checkPath(path, file);

        machineService.exec(file.getMachineId(), "echo '" + content + "' >" + path);
    }

    @Override
    public MachineFile addFile(Integer machineId, MachineFileForm form) {
        boolean isFile = Objects.equals(form.getType(), MachineFileTypeEnum.FILE.getValue());
        String res = machineService.exec(machineId, isFile ? ShellCmd.fileExist(form.getPath()) : ShellCmd.directoryExist(form.getPath()));
        BusinessAssert.equals(res, "1\n", () -> isFile ? "该文件不存在" : "该目录不存在");

        MachineFile file = BeanUtils.copyProperties(form, MachineFile.class);
        file.setMachineId(machineId);
        file.setCreateTime(LocalDateTime.now());
        insert(file);
        return file;
    }

    @Override
    public List<LsVO> ls(Integer fileId, String path) {
        MachineFile machineFile = getById(fileId);
        checkPath(path, machineFile);

        List<LsVO> ls = new ArrayList<>(16);
        String pathPrefix = path.endsWith("/") ? path : path + "/";
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
    public void uploadFile(Integer fileId, String filePath, InputStream inputStream) {
        MachineFile file = getById(fileId);
        checkPath(filePath, file);

        Integer userId = LoginAccount.<Integer>get().getId();
        // 异步上传，成功与否都webscoket通知上传者
        GlobalThreadPool.execute(() -> {
            machineService.sftpOperate(file.getMachineId(), sftp -> {
                try {
                    sftp.put(inputStream, filePath);
                    WebSocketUtils.sendText(SysMsgWebSocket.URI, userId, MessageTypeEnum.SUCCESS.toMsg(filePath + "文件上传成功"));
                } catch (Exception e) {
                    try {
                        WebSocketUtils.sendText(SysMsgWebSocket.URI, userId, MessageTypeEnum.ERROR.toMsg("文件上传失败：" + e.getMessage()));
                    } catch (SessionNoFoundException se) {
                        //
                    }
                }
                return null;
            });
        });
    }

    @Override
    public void rmFile(Integer fileId, String path) {
        MachineFile file = getById(fileId);
        checkPath(path, file);

        machineService.exec(file.getMachineId(), "rm -rf " + path);
    }


    private boolean isFile(MachineFile file) {
        return Objects.equals(file.getType(), MachineFileTypeEnum.FILE.getValue());
    }

    private boolean isDirectory(MachineFile file) {
        return Objects.equals(file.getType(), MachineFileTypeEnum.DIRECTORY.getValue());
    }

    /**
     * 校验前端传过来的path，是否符合配置的路径。即为目录的话，该path必须为配置目录的子目录文件
     *
     * @param path
     * @param file
     */
    private void checkPath(String path, MachineFile file) {
        BusinessAssert.notNull(file, "配置信息不存在");
        if (isDirectory(file)) {
            // 访问的文件路径必须是在配置的子目录下
            BusinessAssert.state(path.startsWith(file.getPath()), "无法访问该文件");
        } else {
            BusinessAssert.equals(path, file.getPath(), "文件路径错误");
            BusinessAssert.state(isFile(file), "该路径为目录，非文件");
        }
    }
}
