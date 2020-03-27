package mayfly.sys.module.sys.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import mayfly.core.base.mapper.annotation.NoColumn;
import mayfly.core.base.mapper.annotation.Table;
import mayfly.core.base.model.BaseDO;

/**
 *  权限
 *
 * @author  hml
 * @date 2018/6/27 下午2:35
 */
@Getter
@Setter
@Accessors(chain = true)
@Table("tb_account_role")
@NoColumn(fields = {BaseDO.UPDATE_TIME, BaseDO.UPDATE_ACCOUNT, BaseDO.UPDATE_ACCOUNT_ID})
public class AccountRoleDO extends BaseDO<Integer> {
    private Integer accountId;

    private Integer roleId;
}
