package mayfly.sys.module.db.controller.vo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author meilin.huang
 * @version 1.0
 * @date 2020-01-03 9:22 上午
 */
@Getter
@Setter
@ToString
public class TableVO {
    /**
     * 表名
     */
    private String name;

    /**
     * 备注
     */
    private String comment;
}
