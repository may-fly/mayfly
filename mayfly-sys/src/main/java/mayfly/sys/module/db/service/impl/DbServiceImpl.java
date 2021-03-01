package mayfly.sys.module.db.service.impl;

import mayfly.core.base.service.impl.BaseServiceImpl;
import mayfly.core.exception.BizAssert;
import mayfly.core.util.StringUtils;
import mayfly.sys.module.db.connection.DbConn;
import mayfly.sys.module.db.controller.vo.ColumnVO;
import mayfly.sys.module.db.controller.vo.TableVO;
import mayfly.sys.module.db.entity.DbDO;
import mayfly.sys.module.db.mapper.DbMapper;
import mayfly.sys.module.db.service.DbService;
import org.springframework.stereotype.Service;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @author meilin.huang
 * @date 2021-01-07 4:46 下午
 */
@Service
public class DbServiceImpl extends BaseServiceImpl<DbMapper, Long, DbDO> implements DbService {
    @Override
    public List<TableVO> listTable(Long dbId) {
        DbConn dc = getConnection(dbId);
        List<TableVO> tables = new ArrayList<>();
        try {
            DatabaseMetaData metaData = dc.getConnection().getMetaData();
            ResultSet rs = metaData.getTables(dc.getDatabase(), null, null, new String[]{"TABLE"});
            while (rs.next()) {
                TableVO t = new TableVO();
                t.setName(rs.getString("TABLE_NAME"));
                t.setComment(rs.getString("REMARKS"));
                tables.add(t);
            }
            rs.close();
        } catch (Exception e) {
            throw BizAssert.newException("获取数据库表信息失败");
        }
        return tables;
    }

    @Override
    public List<ColumnVO> listColumn(Long dbId, String tableName) {
        DbConn dc = getConnection(dbId);
        List<ColumnVO> cs = new ArrayList<>();
        try {
            ResultSet rs = dc.getConnection().getMetaData().getColumns(dc.getDatabase(), null, tableName, null);
            while (rs.next()) {
                ColumnVO c = new ColumnVO();
                //列名
                c.setName(rs.getString("COLUMN_NAME"));
                //类型
                c.setType(rs.getString("TYPE_NAME"));
                //注释
                c.setComment(rs.getString("REMARKS"));
                c.setSize(rs.getString("COLUMN_SIZE"));
                cs.add(c);
            }
            rs.close();
        } catch (Exception e) {
            throw BizAssert.newException("获取数据表字段信息失败");
        }
        return cs;
    }

    @Override
    public List<Map<String, Object>> selectData(Long dbId, String sql) {
        sql = sql.trim();
        BizAssert.isTrue(sql.startsWith("SELECT") || sql.startsWith("select"), "该sql非查询语句");
        DbConn dc = getConnection(dbId);
        Connection connection = dc.getConnection();
        List<Map<String, Object>> res = new ArrayList<>();
        try {
            PreparedStatement ps = connection.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            int count = rs.getMetaData().getColumnCount();
            while (rs.next()) {
                Map<String, Object> r = new LinkedHashMap<>();
                for (int i = 1; i <= count; i++) {
                    r.put(rs.getMetaData().getColumnName(i), rs.getString(i));
                }
                res.add(r);
            }
            rs.close();
            ps.close();
        } catch (Exception e) {
            throw BizAssert.newException("执行sql失败：" + e.getMessage());
        }
        return res;
    }

    @Override
    public Map<String, List<String>> hintTables(Long dbId) {
        Map<String, List<String>> res = new HashMap<>();
        List<TableVO> tables = this.listTable(dbId);
        for (TableVO t : tables) {
            String tableName = t.getName();
            List<String> columns = new ArrayList<>();
            for (ColumnVO c : this.listColumn(dbId, tableName)) {
                String comment = c.getComment();
                if (StringUtils.isEmpty(comment)) {
                    columns.add(c.getName());
                } else {
                    columns.add(c.getName() + " /*" + comment + "*/");
                }
            }
            res.put(t.getName(), columns);
        }
        return res;
    }
}
