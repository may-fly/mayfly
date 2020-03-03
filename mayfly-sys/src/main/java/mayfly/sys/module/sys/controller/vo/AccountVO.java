package mayfly.sys.module.sys.controller.vo;

import lombok.Data;
import mayfly.sys.module.sys.entity.RoleDO;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author meilin.huang
 * @version 1.0
 * @date 2019-07-06 15:50
 */
@Data
public class AccountVO {

    private Integer id;

    private String username;

    private Integer status;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;

    private List<RoleDO> roles;
}
