package mayfly.sys.module.machine.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
import mayfly.core.base.mapper.annotation.NoColumn;
import mayfly.core.base.mapper.annotation.Table;
import mayfly.core.base.model.BaseDO;

/**
 * 机器上配置文件位置
 *
 * @author meilin.huang
 * @version 1.0
 * @date 2019-11-05 10:07 上午
 */
@Getter
@Setter
@ToString(callSuper = true)
@Accessors(chain = true)
@Table("tb_machine_file")
@NoColumn(fields = {BaseDO.UPDATE_TIME, BaseDO.MODIFIER, BaseDO.MODIFIER_ID})
public class MachineFileDO extends BaseDO {
    private Long machineId;

    private String name;

    private String path;

    private Integer type;
}
