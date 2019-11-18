package mayfly.sys.web.machine.vo;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author meilin.huang
 * @version 1.0
 * @date 2019-11-04 3:09 下午
 */
@Data
public class MachineVO {

    private Integer id;

    private String name;

    private String ip;

    private Integer port;

    private String username;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;
}
