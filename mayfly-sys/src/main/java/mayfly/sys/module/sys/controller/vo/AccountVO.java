package mayfly.sys.module.sys.controller.vo;

import lombok.Data;

import java.time.LocalDateTime;

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

    /**
     * 创建账号id
     */
    private Integer createAccountId;

    /**
     * 创建账号名
     */
    private String createAccount;

    /**
     * 最后更新账号id
     */
    private Integer updateAccountId;

    /**
     * 最后更新账号名
     */
    private String updateAccount;
}
