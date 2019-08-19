package mayfly.sys.web.permission.vo;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author meilin.huang
 * @version 1.0
 * @date 2019-07-06 15:50
 */
@Data
public class AdminVO {

    private Integer id;

    private String username;

    private Integer status;

    private LocalDateTime  createTime;

    private LocalDateTime updateTime;
}
