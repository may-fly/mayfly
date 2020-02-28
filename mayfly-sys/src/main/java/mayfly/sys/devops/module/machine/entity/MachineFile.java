package mayfly.sys.devops.module.machine.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import mayfly.sys.common.base.model.BaseEntity;

import java.time.LocalDateTime;

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
public class MachineFile extends BaseEntity {
    private Integer machineId;

    private String name;

    private String path;

    private Integer type;

    private LocalDateTime createTime;
}
