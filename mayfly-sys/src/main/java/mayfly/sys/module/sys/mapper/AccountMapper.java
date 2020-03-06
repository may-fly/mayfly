package mayfly.sys.module.sys.mapper;

import mayfly.core.base.mapper.BaseMapper;
import mayfly.sys.module.sys.controller.query.AccountQuery;
import mayfly.sys.module.sys.entity.AccountDO;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 管理员Mapper
 *
 * @author hml
 * @date 2018/6/27 下午2:35
 */
public interface AccountMapper extends BaseMapper<Integer, AccountDO> {

    @Select("<script>" +
            "SELECT id, username, status, create_time AS createTime, update_time AS updateTime, create_account AS createAccount" +
            ", create_account_id AS createAccountId, update_account AS updateAccount, update_account_id AS updateAccountId " +
            "FROM tb_account " +
            "<if test='username != null'>" +
                "WHERE username LIKE CONCAT(#{username}, '%')" +
            "</if>" +
            "</script>")
    List<AccountDO> selectByQuery(AccountQuery accountQuery);
}
