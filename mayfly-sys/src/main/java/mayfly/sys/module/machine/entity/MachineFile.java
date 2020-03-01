package mayfly.sys.module.machine.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import mayfly.core.base.mapper.annotation.NoColumn;
import mayfly.core.base.model.BaseEntity;

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
@NoColumn(fields = {"updateTime"})
public class MachineFile extends BaseEntity {
    private Integer machineId;

    private String name;

    private String path;

    private Integer type;
}
