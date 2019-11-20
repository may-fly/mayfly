package mayfly.sys.web.machine;

import mayfly.core.result.Result;
import mayfly.core.util.websocket.SessionNoFoundException;
import mayfly.core.util.websocket.WebSocketUtils;
import mayfly.core.validation.annotation.Valid;
import mayfly.sys.common.utils.BeanUtils;
import mayfly.sys.service.machine.MachineService;
import mayfly.sys.web.machine.form.MachineForm;
import mayfly.sys.web.machine.vo.MachineVO;
import mayfly.sys.web.socket.TestSocket;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
