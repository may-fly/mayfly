package mayfly.dao;

import mayfly.dao.base.BaseMapper;
import mayfly.entity.Operation;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @author meilin.huang
 * @version 1.0
 * @date 2018-12-10 1:30 PM
 */
public interface OperationMapper extends BaseMapper<Operation> {

    @Select("SELECT o.id, o.name, o.code, " +
            "a.method AS 'api.method', a.uri_pattern AS 'api.uriPattern' " +
            "FROM tb_operation o JOIN tb_api a ON o.api_id = a.id AND a.status = 1 " +
            "WHERE o.status = 1 AND " +
            "o.id IN " +
            "(SELECT DISTINCT(rmb.menu_btn_id) " +
            "FROM tb_permission p JOIN tb_role r ON p.role_Id = r.id AND p.user_id = #{userId} AND r.status = 1 " +
            "JOIN tb_role_menu_btn rmb ON rmb.role_id = r.id AND rmb.type = 2)")
    List<Operation> selectByUserId(Integer userId);
}
