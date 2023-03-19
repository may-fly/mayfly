package mayfly.sys.module.db.controller.form;

import lombok.Getter;
import lombok.Setter;

import jakarta.validation.constraints.NotEmpty;

/**
 * @author meilin.huang
 * @date 2021-01-08 4:34 下午
 */
@Getter
@Setter
public class DbSqlSaveForm {
    @NotEmpty(message = "sql不能为空")
    private String sql;
}
