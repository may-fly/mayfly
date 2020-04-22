package mayfly.sys.module.sys.controller.query;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import mayfly.core.base.model.PageQuery;

/**
 * @author meilin.huang
 * @version 1.0
 * @date 2019-07-06 14:59
 */
@Getter
@Setter
@ToString(callSuper = true)
public class AccountQuery extends PageQuery {

    private String username;
}
