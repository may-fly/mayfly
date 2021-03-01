package mayfly.sys.module.db.entity;

import lombok.Data;
import mayfly.core.base.mapper.annotation.Table;
import mayfly.core.model.BaseDO;

import java.time.LocalDateTime;

/**
 * @author meilin.huang
 * @version 1.0
 * @date 2020-01-02 6:04 下午
 */
@Data
@Table("t_db")
public class DbDO extends BaseDO {

    /**
     * 数据库类型
     */
    private Integer type;

    private String host;

    private Integer port;

    private String username;

    private String password;

    private String jdbcUrl;

    /**
     * 名称
     */
    private String name;

    private String database;
}
