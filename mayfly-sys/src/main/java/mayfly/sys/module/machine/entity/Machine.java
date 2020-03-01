package mayfly.sys.module.machine.entity;

import lombok.Getter;
import lombok.Setter;
import mayfly.core.base.model.BaseEntity;

/**
 * @author meilin.huang
 * @version 1.0
 * @date 2019-11-04 3:02 下午
 */
@Getter
@Setter
public class Machine extends BaseEntity {
    private String name;

    private String ip;

    private Integer port;

    private String username;

    private String password;
}
