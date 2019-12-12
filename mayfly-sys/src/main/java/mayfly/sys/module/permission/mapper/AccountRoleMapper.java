package mayfly.sys.module.permission.mapper;

import mayfly.sys.module.base.mapper.BaseMapper;
import mayfly.sys.module.permission.entity.AccountRole;
import mayfly.sys.module.permission.entity.Role;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @author meilin.huang
 * @version 1.0
 * @date 2019-08-19 20:13
 */
public interface AccountRoleMapper extends BaseMapper<AccountRole> {

    @Select("SELECT r.status, r.name FROM tb_role r JOIN tb_account_role ar ON r.id = ar.role_id AND ar.account_id = #{accountId}")
    List<Role> selectRoleByAccountId(Integer accountId);
}
