package mayfly.sys.module.sys.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import mayfly.core.base.mapper.annotation.NoColumn;
import mayfly.core.base.mapper.annotation.Table;
import mayfly.core.base.model.BaseDO;

/**
 * @author meilin.huang
 * @version 1.0
 * @date 2018-12-07 4:14 PM
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table("tb_role_resource")
@NoColumn(fields = {"updateTime", "update_account", "update_account_id"})
public class RoleResourceDO extends BaseDO {

    private Integer roleId;

    private Integer resourceId;
}
