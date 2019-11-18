package mayfly.sys.web.machine;

import mayfly.core.result.Result;
import mayfly.core.util.websocket.SessionNoFoundException;
import mayfly.core.util.websocket.WebSocketUtils;
import mayfly.core.validation.annotation.Valid;
import mayfly.sys.common.utils.BeanUtils;
import mayfly.sys.common.websocket.MessageTypeEnum;
import mayfly.sys.service.machine.MachineService;
import mayfly.sys.web.machine.form.MachineForm;
import mayfly.sys.web.machine.vo.LsVO;
import mayfly.sys.web.machine.vo.MachineVO;
import mayfly.sys.web.socket.SysMsgWebSocket;
import mayfly.sys.web.socket.TestSocket;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

/**
 * @author meilin.huang
 * @version 1.0
 * @date 2019-11-04 3:07 下午
 */
//@Permission(code = "machine:")
@RestController
@RequestMapping("/sys/machines")
public class MachineController {

    @Autowired
    private MachineService machineService;

    @GetMapping()
    public Result list() {
        try {
            WebSocketUtils.sendText(TestSocket.URI, 1, "获取机器列表");
        } catch (SessionNoFoundException e) {
            //
        }
        return Result.success(BeanUtils.copyProperties(machineService.listAll(), MachineVO.class));
    }

    @PostMapping()
    public Result save(@RequestBody @Valid MachineForm form) {
        machineService.saveMachine(form);
        return Result.success();
    }

    @DeleteMapping("/{machineId}")
    public Result delete(@PathVariable Integer id) {
        machineService.deleteById(id);
        return Result.success();
    }

    @GetMapping("/{machineId}/ls")
    public Result ls(@PathVariable Integer machineId, String path) {
        List<LsVO> ls = new ArrayList<>(16);
        machineService.exec(machineId, "ls -Alh " + path, (lineNum, lineContent) -> {
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
            vo.setPath(path);
            vo.setType(typeAndPermission[0]);
            vo.setName(strs[strs.length - 1]);
//            vo.setSize(Long.parseLong(strs[4]));
            ls.add(vo);
        });
        WebSocketUtils.broadcastText(SysMsgWebSocket.URI, MessageTypeEnum.SYS_NOTIFY.toMsg("机器id：" + machineId + "执行了ls操作"));
        return Result.success(ls);
    }

    @GetMapping("/{machineId}/cat/{fileId}")
    public Result cat(@PathVariable Integer machineId, @PathVariable Integer fileId, String path) {
        return Result.success(machineService.exec(machineId, "cat " + path));
    }

    @PostMapping("/{machineId}/upload")
    public Result upload(@PathVariable Integer machineId, String path, MultipartFile file) {
        return machineService.sftpOperate(machineId, sftp -> {
            try {
                sftp.put(file.getInputStream(), path);
                return Result.success();
            } catch (Exception e) {
                return Result.serverError();
            }
        });
    }

    @DeleteMapping("/{machineId}/rm")
    public Result rm(@PathVariable Integer machineId, String path) {
        machineService.exec(machineId, "rm -rf " + path);
        return Result.success();
    }
}
