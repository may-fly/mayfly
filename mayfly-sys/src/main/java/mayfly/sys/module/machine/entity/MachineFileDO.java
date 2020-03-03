package mayfly.sys.module.machine.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
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
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@Table("tb_machine_file")
@NoColumn(fields = {"updateTime", "update_account", "update_account_id"})
public class MachineFileDO extends BaseDO {
    private Integer machineId;

    private String name;

    private String path;

    private Integer type;
}
