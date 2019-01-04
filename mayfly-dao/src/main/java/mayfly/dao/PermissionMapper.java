package mayfly.dao;

import mayfly.dao.base.BaseMapper;
import mayfly.entity.Permission;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @Description: 权限Mapper
 * @author: hml
 * @date: 2018/6/27 下午2:36
 */
public interface PermissionMapper extends BaseMapper<Permission> {

    @Select("SELECT p.code, p.status " +
            "FROM tb_permission p " +
            "WHERE p.id IN " +
            "(SELECT DISTINCT(rmb.resource_id) " +
            "FROM tb_role_user p JOIN tb_role r ON p.role_Id = r.id AND p.user_id = #{userId} AND r.status = 1 " +
            "JOIN tb_role_resource rmb ON rmb.role_id = r.id AND rmb.type = 2)")
    List<Permission> selectByUserId(Integer user);
}
