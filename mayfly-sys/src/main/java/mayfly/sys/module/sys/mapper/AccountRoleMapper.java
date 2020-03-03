package mayfly.sys.module.sys.mapper;

import mayfly.core.base.mapper.BaseMapper;
import mayfly.sys.module.sys.entity.AccountRoleDO;
import mayfly.sys.module.sys.entity.RoleDO;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @author meilin.huang
 * @version 1.0
 * @date 2019-08-19 20:13
 */
public interface AccountRoleMapper extends BaseMapper<Integer, AccountRoleDO> {

    @Select("SELECT r.status, r.name FROM tb_role r JOIN tb_account_role ar ON r.id = ar.role_id AND ar.account_id = #{accountId}")
    List<RoleDO> selectRoleByAccountId(Integer accountId);
}
