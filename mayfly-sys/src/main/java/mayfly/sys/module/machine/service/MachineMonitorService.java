package mayfly.sys.module.machine.service;

import mayfly.core.base.service.BaseService;
import mayfly.sys.module.machine.entity.MachineMonitorDO;

import java.util.List;
import java.util.Map;

/**
 * @author meilin.huang
 * @version 1.0
 * @date 2019-11-04 3:04 下午
 */
public interface MachineMonitorService extends BaseService<MachineMonitorDO> {

    /**
     * 获取并保存监控信息
     */
    void getAndSaveMonitor();

    List<Map<String, Object>> listByQuery(Long machienId, String beginTime, String endTime);
}
