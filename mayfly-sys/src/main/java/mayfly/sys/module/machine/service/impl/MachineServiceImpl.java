package mayfly.sys.module.machine.service.impl;

import com.jcraft.jsch.Session;
import mayfly.core.base.service.impl.BaseServiceImpl;
import mayfly.core.exception.BusinessRuntimeException;
import mayfly.core.util.bean.BeanUtils;
import mayfly.sys.common.utils.ssh.SSHException;
import mayfly.sys.common.utils.ssh.SSHUtils;
import mayfly.sys.common.utils.ssh.SessionInfo;
import mayfly.sys.module.machine.controller.form.MachineForm;
import mayfly.sys.module.machine.entity.MachineDO;
import mayfly.sys.module.machine.mapper.MachineMapper;
import mayfly.sys.module.machine.service.MachineFileService;
import mayfly.sys.module.machine.service.MachineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author meilin.huang
 * @version 1.0
 * @date 2019-11-04 3:04 下午
 */
@Service
public class MachineServiceImpl extends BaseServiceImpl<MachineMapper, MachineDO> implements MachineService {

    @Autowired
    private MachineMapper machineMapper;
    @Autowired
    private MachineFileService machineFileService;

    @Autowired
    @Override
    protected void setBaseMapper() {
        super.baseMapper = machineMapper;
    }

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

        MachineDO machine = BeanUtils.copyProperties(form, MachineDO.class);
        insert(machine);
    }


}
