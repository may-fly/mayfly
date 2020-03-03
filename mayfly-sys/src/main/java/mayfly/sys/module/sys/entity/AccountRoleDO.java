package mayfly.sys.module.sys.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import mayfly.core.base.mapper.annotation.Table;
import mayfly.core.base.model.BaseDO;

/**
 *  权限
 *
 * @author  hml
 * @date 2018/6/27 下午2:35
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table("tb_account_role")
public class AccountRoleDO extends BaseDO {
    private Integer accountId;

    private Integer roleId;
}
