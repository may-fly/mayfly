package mayfly.sys.module.db.controller.vo;

import lombok.Data;

/**
 * @author meilin.huang
 * @date 2021-01-08 11:17 上午
 */
@Data
public class DbVO {

    private Long id;

    private String name;

    private String database;

    private Integer type;
}
