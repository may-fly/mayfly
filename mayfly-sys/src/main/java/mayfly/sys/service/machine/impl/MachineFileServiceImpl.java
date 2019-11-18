package mayfly.sys.service.machine.impl;

import mayfly.core.exception.BusinessAssert;
import mayfly.core.util.ssh.ShellCmd;
import mayfly.core.util.websocket.WebSocketUtils;
import mayfly.dao.MachineFileMapper;
import mayfly.entity.MachineFile;
import mayfly.sys.common.enums.MachineFileTypeEnum;
import mayfly.sys.common.utils.BeanUtils;
import mayfly.sys.common.websocket.MessageTypeEnum;
import mayfly.sys.service.base.impl.BaseServiceImpl;
import mayfly.sys.service.machine.MachineFileService;
import mayfly.sys.service.machine.MachineService;
import mayfly.sys.web.machine.form.MachineFileForm;
import mayfly.sys.web.machine.vo.LsVO;
import mayfly.sys.web.socket.SysMsgWebSocket;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    @Autowired
    private MachineService machineService;

    @Autowired
    private MachineFileMapper machineFileMapper;

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

        machineService.exec(file.getMachineId(), "echo '" + content +  "' >" + path);
    }

    @Override
    public MachineFile addFile(Integer machineId, MachineFileForm form) {
        boolean isFile = Objects.equals(form.getType(), MachineFileTypeEnum.FILE.getValue());
        String res = machineService.exec(machineId, isFile ? ShellCmd.fileExist(form.getPath()) : ShellCmd.directoryExist(form.getPath()));
        BusinessAssert.state(Objects.equals(res, "1"), () -> isFile ? "该文件不存在" : "该目录不存在");

        MachineFile file = BeanUtils.copyProperties(form, MachineFile.class);
        file.setMachineId(machineId);
        file.setCreateTime(LocalDateTime.now());
        save(file);
        return file;
    }

    @Override
    public List<LsVO> ls(Integer fileId, String path) {
        MachineFile machineFile = getById(fileId);
        BusinessAssert.notNull(machineFile, "配置信息不存在");
        // 访问的路径只能是以配置的路径为前缀
        BusinessAssert.state(path.startsWith(machineFile.getPath()), "非法路径");

        List<LsVO> ls = new ArrayList<>(16);
        String pathPrefix = path.endsWith("/") ? path : path + "/";
        machineService.exec(machineFile.getMachineId(), "ls -Alh " + path, (lineNum, lineContent) -> {
            if (lineNum == 1) {
                return;
            }
            String[] strs = lineContent.split("\\s+");
            char[] typeAndPermission =  strs[0].toCharArray();
            char type = typeAndPermission[0];
            if (type == 'l') {
                return;
            }
            LsVO vo = new LsVO();
            String fileName = strs[strs.length - 1];
            vo.setPath(pathPrefix + fileName);
            vo.setType(typeAndPermission[0]);
            vo.setName(fileName);
//            vo.setSize(Long.parseLong(strs[4]));
            ls.add(vo);
        });
        WebSocketUtils.broadcastText(SysMsgWebSocket.URI, MessageTypeEnum.SYS_NOTIFY.toMsg("机器执行了ls操作"));
        return ls;
    }


    private boolean isFile(MachineFile file) {
        return Objects.equals(file.getType(), MachineFileTypeEnum.FILE.getValue());
    }

    private boolean isDirectory(MachineFile file) {
        return Objects.equals(file.getType(), MachineFileTypeEnum.DIRECTORY.getValue());
    }

    /**
     * 校验前端传过来的path，是否符合配置的路径。即为目录的话，该path必须为配置目录的子目录文件
     * @param path
     * @param file
     */
    private void checkPath(String path, MachineFile file) {
        BusinessAssert.notNull(file, "配置信息不存在");
        if (isDirectory(file)) {
            // 访问的文件路径必须是在配置的子目录下
            BusinessAssert.state(path.startsWith(file.getPath()), "无法访问该文件");
        } else {
            BusinessAssert.state(Objects.equals(path, file.getPath()), "文件路径错误");
            BusinessAssert.state(isFile(file), "该路径为目录，非文件");
        }
    }
}
