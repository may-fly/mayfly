package mayfly.sys.module.sys.controller.vo;

import lombok.Getter;
import lombok.Setter;
import mayfly.core.base.model.BaseDO;

import java.time.LocalDateTime;

/**
 * @author meilin.huang
 * @version 1.0
 * @date 2019-07-06 15:50
 */
@Getter
@Setter
public class AccountVO extends BaseDO {

    private Long id;

    private String username;

    private Integer status;

    private LocalDateTime lastLoginTime;
}
