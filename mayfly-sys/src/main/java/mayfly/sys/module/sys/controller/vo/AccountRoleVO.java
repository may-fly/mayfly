package mayfly.sys.module.sys.controller.vo;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * @author meilin.huang
 * @date 2020-03-29 3:16 下午
 */
@Getter
@Setter
public class AccountRoleVO {

    private String name;

    private LocalDateTime createTime;

    private String createAccount;
}
