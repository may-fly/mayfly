package mayfly.sys.module.db.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import mayfly.core.base.mapper.annotation.Table;
import mayfly.core.model.BaseDO;

/**
 * @author meilin.huang
 * @date 2021-01-08 4:15 下午
 */
@Getter
@Setter
@ToString
@Table("t_db_sql")
public class DbSqlDO extends BaseDO {

    private Long dbId;

    private String sql;
}
