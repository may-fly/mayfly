package mayfly.dao;

import mayfly.dao.base.BaseMapper;
import mayfly.entity.Resource;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @Description: 菜单Mapper
 * @author: hml
 * @date: 2018/6/27 下午2:36
 */
public interface ResourceMapper extends BaseMapper<Resource> {

//    @Select("SELECT m.id, m.pid, m.weight, m.name, m.type, m.code, " +
//            "m.api_id AS 'permission.id', a.method AS 'permission.method', a.status As 'permission.status', a.uri_pattern AS 'permission.uriPattern' " +
//            "FROM tb_menu m LEFT JOIN tb_api a ON m.api_id = a.id " +
//            "WHERE m.status = 1 AND m.id IN " +
//            "(SELECT DISTINCT(rm.menu_id) " +
//            "FROM tb_permission p JOIN tb_role r ON p.role_Id = r.id AND p.user_id = #{userId} AND r.status = 1 " +
//            "JOIN tb_role_menu rm ON r.id = rm.role_id)")
//    List<Resource> selectByUserId(Integer userId);

    @Select("SELECT m.id, m.pid, m.weight, m.name, m.code, m.icon, m.path, m.type, m.status " +
            "FROM tb_resource m WHERE " +
            "m.id IN " +
            "(SELECT DISTINCT(rmb.resource_id) " +
            "FROM tb_role_user p JOIN tb_role r ON p.role_Id = r.id AND p.user_id = #{userId} AND r.status = 1 " +
            "JOIN tb_role_resource rmb ON rmb.role_id = r.id AND rmb.type = 1)" +
            "ORDER BY m.pid ASC, m.weight DESC")
    List<Resource> selectByUserId(Integer userId);
}