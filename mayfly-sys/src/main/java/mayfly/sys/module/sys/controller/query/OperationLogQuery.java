package mayfly.sys.module.sys.controller.query;

import lombok.Getter;
import lombok.Setter;
import mayfly.core.base.model.PageQuery;

/**
 * @author meilin.huang
 * @date 2020-03-05 4:06 下午
 */
@Getter
@Setter
public class OperationLogQuery extends PageQuery {

    private String createAccount;

    private Integer type;
}
