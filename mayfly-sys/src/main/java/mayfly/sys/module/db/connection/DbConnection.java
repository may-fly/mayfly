package mayfly.sys.module.db.connection;

import mayfly.core.exception.BizAssert;
import mayfly.core.exception.BizRuntimeException;
import mayfly.core.util.BracePlaceholder;
import mayfly.core.cache.Cache;
import mayfly.core.cache.CacheBuilder;
import mayfly.sys.module.db.entity.Db;
import mayfly.sys.module.db.enums.DbTypeEnum;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

/**
 * @author meilin.huang
 * @version 1.0
 * @date 2020-01-02 5:49 下午
 */
public class DbConnection {

    /**
     * mysql jdbc url模板
     */
    private static final String MYSQL_URL_TEMP = "jdbc:mysql://{url}:{port}/{name}?useUnicode=true&autoReconnect=true&useSSL=false";

    /**
     * connection缓存，最多允许15个connection同时连接，移除connection时候执行close操作
     */
    private static Cache<Integer, Connection> connectionCache = CacheBuilder.<Integer, Connection>newTimedBuilder(30, TimeUnit.MINUTES)
            .capacity(15).removeCallback(DbConnection::close).build();


    public static Connection getConnection(Integer dbId, Supplier<Db> dbSupplier) {
        return connectionCache.get(dbId, () -> {
            Db db = dbSupplier.get();
            return getConnection(getJdbcUrl(db), db.getUsername(), db.getPassword());
        });
    }

    private static String getJdbcUrl(Db db) {
        if (Objects.equals(db.getType(), DbTypeEnum.MYSQL.getValue())) {
            return BracePlaceholder.resolveByObject(MYSQL_URL_TEMP, db);
        }
        throw BizAssert.newBizRuntimeException("数据库类型错误");
    }

    public static Connection getConnection(String url, String username, String password) {
        try {
            return DriverManager.getConnection(url, username, password);
        } catch (SQLException e) {
           throw BizAssert.newBizRuntimeException("连接失败");
        }
    }

    public static void close(Connection connection) {
        try {
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }



    //输出列名、类型、注释
    public static void printColumnInfo(DatabaseMetaData databaseMetaData)throws Exception{
        ResultSet rs = databaseMetaData.getColumns(null, "%", "tb_account_role", "%");
        while(rs.next()){
            //列名
            String columnName = rs.getString("COLUMN_NAME");
            //类型
            String typeName = rs.getString("TYPE_NAME");
            //注释
            String remarks = rs.getString("REMARKS");
            System.out.println(columnName + "--" + typeName + "--" + remarks);
        }
    }

    //输出表名
    public static void printTableNames(DatabaseMetaData databaseMetaData)throws Exception{
        //获取表名的结果集
        ResultSet rs = databaseMetaData.getTables("mayfly", null, null, new String[]{"TABLE"});
        while(rs.next()){
            String tableName = rs.getString("TABLE_NAME");
            String mask = rs.getString("REMARKS");
            System.out.println(tableName + "--" + mask);
        }
    }


    public static void main(String[] args) throws Exception {
        //获取表名的结果集
        DatabaseMetaData metaData = getConnection(1, () -> {
            Db db = new Db();
            db.setUrl("localhost");
            db.setPort(3306);
            db.setUsername("root");
            db.setPassword("111049");
            db.setName("mayfly");
            db.setType(DbTypeEnum.MYSQL.getValue());
            return db;
        }).getMetaData();
        printColumnInfo(metaData);
        printTableNames(metaData);
//        System.out.println(metaData);
//        ResultSet rs = metaData.getTables(null, null, null, new String[]{"TABLE"});
//        while(rs.next()){
//            String tableName = rs.getString("TABLE_NAME");
//            System.out.println(tableName);
//        }
    }
}
