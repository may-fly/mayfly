package mayfly.sys.module.sys.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
import mayfly.core.base.mapper.annotation.NoColumn;
import mayfly.core.base.mapper.annotation.Table;
import mayfly.core.base.model.BaseDO;

/**
 * @author meilin.huang
 * @version 1.0
 * @date 2018-12-07 4:14 PM
 */
@Getter
@Setter
@ToString(callSuper = true)
@Accessors(chain = true)
@Table("tb_role_resource")
@NoColumn(fields = {BaseDO.UPDATE_TIME, BaseDO.UPDATE_ACCOUNT, BaseDO.UPDATE_ACCOUNT_ID})
public class RoleResourceDO extends BaseDO {

    private Long roleId;

    private Long resourceId;
}
