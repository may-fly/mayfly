package mayfly.sys.devops.module.machine.entity;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author meilin.huang
 * @version 1.0
 * @date 2019-11-04 3:02 下午
 */
@Data
public class Machine {

    private Integer id;

    private String name;

    private String ip;

    private Integer port;

    private String username;

    private String password;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;
}
