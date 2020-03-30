package mayfly.sys.module.sys.mapper;

import mayfly.core.base.mapper.BaseMapper;
import mayfly.sys.module.sys.entity.ResourceDO;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 *  菜单Mapper
 *
 * @author hml
 * @date 2018/6/27 下午2:36
 */
public interface ResourceMapper extends BaseMapper<Long, ResourceDO> {

    @Select("SELECT m.id, m.pid, m.weight, m.name, m.code, m.icon, m.type, m.status " +
            "FROM tb_resource m WHERE " +
            "m.id IN " +
            "(SELECT DISTINCT(rmb.resource_id) " +
            "FROM tb_account_role p JOIN tb_role r ON p.role_Id = r.id AND p.account_id = #{accountId} AND r.status = 1 " +
            "JOIN tb_role_resource rmb ON rmb.role_id = r.id)" +
            "ORDER BY m.pid ASC, m.weight ASC")
    List<ResourceDO> selectByAccountId(Long accountId);
}
