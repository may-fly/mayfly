package mayfly.sys.module.machine.service;

import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.Session;
import mayfly.core.base.service.BaseService;
import mayfly.core.exception.BusinessAssert;
import mayfly.core.exception.BusinessRuntimeException;
import mayfly.core.util.IOUtils;
import mayfly.sys.common.utils.ssh.SSHException;
import mayfly.sys.common.utils.ssh.SSHUtils;
import mayfly.sys.common.utils.ssh.SessionInfo;
import mayfly.sys.module.machine.controller.form.MachineForm;
import mayfly.sys.module.machine.entity.MachineDO;

import java.util.Objects;
import java.util.function.Function;

/**
 * @author meilin.huang
 * @version 1.0
 * @date 2019-11-04 3:04 下午
 */
public interface MachineService extends BaseService<Integer, MachineDO> {

    /**
     * 保存新增的机器信息
     *
     * @param form 机器信息
     */
    void create(MachineForm form);


    /**
     * 执行指定机器的命令
     *
     * @param machineId 机器id
     * @param cmd       命令
     */
    default String exec(Integer machineId, String cmd) {
        try {
            return SSHUtils.exec(getSession(machineId), cmd);
        } catch (SSHException e) {
            throw new BusinessRuntimeException(e.getMessage());
        }
    }

    /**
     * 执行指定机器的命令
     *
     * @param machineId     机器id
     * @param cmd           命令
     * @param lineProcessor 行处理器
     */
    default void exec(Integer machineId, String cmd, IOUtils.LineProcessor lineProcessor) {
        try {
            SSHUtils.exec(getSession(machineId), cmd, lineProcessor);
        } catch (SSHException e) {
            throw new BusinessRuntimeException(e.getMessage());
        }
    }

    /**
     * 获取sftp 并对channel执行操作
     *
     * @param machineId 机器id
     */
    default void sftpOperate(Integer machineId, Function<ChannelSftp, Void> function) {
        try {
            SSHUtils.sftpOperate(getSession(machineId), function);
        } catch (SSHException e) {
            throw new BusinessRuntimeException(e.getMessage());
        }
    }

    /**
     * 获取指定机器的session
     *
     * @param machineId 机器id
     * @return session
     */
    default Session getSession(Integer machineId) {
        return SSHUtils.getSession(Objects.toString(machineId), () -> {
            MachineDO machine = getById(machineId);
            BusinessAssert.notNull(machine, "机器不存在");
            return SessionInfo.builder(machine.getIp()).port(machine.getPort())
                    .password(machine.getPassword()).username(machine.getUsername()).build();
        });
    }
}
