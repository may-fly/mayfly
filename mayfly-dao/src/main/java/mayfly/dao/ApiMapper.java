package mayfly.dao;

import mayfly.dao.base.BaseMapper;
import mayfly.entity.Permission;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @Description: permission Mapper
 * @author: hml
 * @date: 2018/6/25 下午5:27
 */
public interface ApiMapper extends BaseMapper<Permission> {

    @Select("<script>" +
            "SELECT id, description, method, uri_pattern AS uriPattern, status, create_time AS createTime, update_time AS updateTime " +
            "FROM tb_api " +
            "WHERE 1 = 1 " +
            "<if test = \"description != null\">" +
                "AND description LIKE CONCAT('%', #{description}, '%') " +
            "</if>" +
            "</script>")
    List<Permission> selectByCondition(Permission permission);

    @Select("SELECT a.code, a.method, a.uri_pattern AS uriPattern " +
            "FROM tb_api a " +
            "WHERE a.status = 1 AND " +
            "a.id IN " +
            "(SELECT DISTINCT(rmb.menu_btn_id) " +
            "FROM tb_permission p JOIN tb_role r ON p.role_Id = r.id AND p.user_id = #{userId} AND r.status = 1 " +
            "JOIN tb_role_menu_btn rmb ON rmb.role_id = r.id AND rmb.type = 2)")
    List<Permission> selectByUserId(Integer user);
}
