package mayfly.sys.module.db.controller.vo;

import lombok.Data;

/**
 * @author meilin.huang
 * @date 2021-01-07 4:30 下午
 */
@Data
public class ColumnVO {

    private String name;

    private String type;

    private String size;

    private String comment;
}
