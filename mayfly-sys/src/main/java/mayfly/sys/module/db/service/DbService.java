package mayfly.sys.module.db.service;

import mayfly.core.base.service.BaseService;
import mayfly.sys.module.db.connection.DbConn;
import mayfly.sys.module.db.controller.vo.ColumnVO;
import mayfly.sys.module.db.controller.vo.TableVO;
import mayfly.sys.module.db.entity.DbDO;

import java.util.List;
import java.util.Map;

/**
 * @author meilin.huang
 * @version 1.0
 * @date 2020-01-03 9:19 上午
 */
public interface DbService extends BaseService<DbDO> {

    /**
     * 获取数据库所有表信息
     *
     * @param dbId db id
     * @return 表信息
     */
    List<TableVO> listTable(Long dbId);

    /**
     * 获取所有表的所有列信息
     *
     * @param dbId      db id
     * @param tableName 表名
     * @return 列信息
     */
    List<ColumnVO> listColumn(Long dbId, String tableName);

    /**
     * 执行查询数据
     *
     * @param dbId db id
     * @param sql  sql
     * @return 结果
     */
    List<Map<String, Object>> selectData(Long dbId, String sql);

    /**
     * 获取sql提示
     *
     * @param dbId db id
     * @return 表名及字段名
     */
    Map<String, List<String>> hintTables(Long dbId);

    /**
     * 获取数据库连接
     *
     * @param dbId db id
     * @return connection
     */
    default DbConn getConnection(Long dbId) {
        return DbConn.getConnection(dbId, () -> getById(dbId));
    }
}
