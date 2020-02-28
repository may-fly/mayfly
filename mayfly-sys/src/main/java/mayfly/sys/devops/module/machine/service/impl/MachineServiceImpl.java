package mayfly.sys.devops.module.machine.service.impl;

import com.jcraft.jsch.Session;
import mayfly.core.exception.BusinessRuntimeException;
import mayfly.core.util.ssh.SSHException;
import mayfly.core.util.ssh.SSHUtils;
import mayfly.core.util.ssh.SessionInfo;
import mayfly.sys.devops.module.machine.mapper.MachineMapper;
import mayfly.sys.devops.module.machine.entity.Machine;
import mayfly.sys.common.utils.BeanUtils;
import mayfly.sys.common.base.service.impl.BaseServiceImpl;
import mayfly.sys.devops.module.machine.service.MachineFileService;
import mayfly.sys.devops.module.machine.service.MachineService;
import mayfly.sys.devops.module.machine.controller.form.MachineForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
 * @author meilin.huang
 * @version 1.0
 * @date 2019-11-04 3:04 下午
 */
@Service
public class MachineServiceImpl extends BaseServiceImpl<MachineMapper, Machine> implements MachineService {

    @Autowired
    private MachineFileService machineFileService;

    @Override
    public void saveMachine(MachineForm form) {
        // 校验机器是否存在以及是否可以登录
        try {
            Session session = SSHUtils.openSession(SessionInfo.builder(form.getIp()).port(form.getPort())
                    .password(form.getPassword()).username(form.getUsername()).build());
            session.disconnect();
        } catch (SSHException e) {
            throw new BusinessRuntimeException("信息不正确：" + e.getMessage());
        }

        Machine machine = BeanUtils.copyProperties(form, Machine.class);
        LocalDateTime now = LocalDateTime.now();
        machine.setCreateTime(now);
        machine.setUpdateTime(now);
        insert(machine);
    }


}
