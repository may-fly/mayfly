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
public interface AccountMapper extends BaseMapper<Long, AccountDO> {

    @Select("<script>" +
            "SELECT id, username, status, create_time AS createTime, update_time AS updateTime, creator" +
            ", creator_id AS creatorId, modifier, modifier_id AS modifierId, last_login_time AS lastLoginTime " +
            "FROM t_account " +
            "<if test='username != null'>" +
                "WHERE username LIKE CONCAT(#{username}, '%')" +
            "</if>" +
            "</script>")
    List<AccountDO> selectByQuery(AccountQuery accountQuery);
}
