package mayfly.sys.devops.module.db.entity;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author meilin.huang
 * @version 1.0
 * @date 2020-01-02 6:04 下午
 */
@Data
public class Db {

    private Integer id;

    /**
     * 数据库类型
     */
    private Integer type;

    private String url;

    private Integer port;

    private String username;

    private String password;

    /**
     * 库名
     */
    private String name;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;
}
