package mayfly.sys.module.sys.controller.vo;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author meilin.huang
 * @version 1.0
 * @date 2019-07-27 10:52
 */
@Data
public class ResourceDetailVO {
    private Integer id;

    private Integer type;

    private String name;

    private String icon;

    private String code;

    private Integer status;

    private Integer weight;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;

    private String createAccount;

    private String updateAccount;
}
