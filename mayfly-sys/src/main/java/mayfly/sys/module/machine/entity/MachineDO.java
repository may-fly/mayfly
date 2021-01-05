package mayfly.sys.module.machine.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import mayfly.core.base.mapper.annotation.Table;
import mayfly.core.model.BaseDO;

/**
 * @author meilin.huang
 * @version 1.0
 * @date 2019-11-04 3:02 下午
 */
@Getter
@Setter
@ToString(callSuper = true)
@Table("tb_machine")
public class MachineDO extends BaseDO {
    private String name;

    private String ip;

    private Integer port;

    private String username;

    private String password;

    /**
     * 是否需要监控机器信息
     */
    private Integer needMonitor;
}
