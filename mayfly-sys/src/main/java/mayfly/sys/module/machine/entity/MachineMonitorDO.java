package mayfly.sys.module.machine.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
import mayfly.core.base.mapper.annotation.NoColumn;
import mayfly.core.base.mapper.annotation.Table;
import mayfly.core.model.BaseDO;

/**
 * @author meilin.huang
 * @date 2020-08-31 2:17 下午
 */
@Getter
@Setter
@ToString(callSuper = true)
@Accessors(chain = true)
@Table("t_machine_monitor")
@NoColumn(fields = {BaseDO.CREATOR, BaseDO.CREATOR_ID, BaseDO.UPDATE_TIME, BaseDO.MODIFIER, BaseDO.MODIFIER_ID})
public class MachineMonitorDO extends BaseDO {

    private Long machineId;

    private Float cpuRate;

    private Float memRate;

    private Float oneMinLoadavg;

    private Float fiveMinLoadavg;

    private Float fifMinLoadavg;
}
