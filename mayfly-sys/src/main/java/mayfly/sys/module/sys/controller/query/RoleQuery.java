package mayfly.sys.module.sys.controller.query;

import lombok.Getter;
import lombok.Setter;
import mayfly.core.base.model.PageQuery;

/**
 * @author meilin.huang
 * @date 2020-03-29 10:50 上午
 */
@Getter
@Setter
public class RoleQuery extends PageQuery {

    private String name;
}
