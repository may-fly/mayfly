package mayfly.sys.module.db.connection;

import lombok.Data;
import mayfly.core.exception.BizAssert;
import mayfly.core.util.BracePlaceholder;
import mayfly.core.cache.Cache;
import mayfly.core.cache.CacheBuilder;
import mayfly.core.util.StringUtils;
import mayfly.sys.module.db.entity.DbDO;
import mayfly.sys.module.db.enums.DbTypeEnum;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

/**
 * 数据库连接
 *
 * @author meilin.huang
 * @version 1.0
 * @date 2020-01-02 5:49 下午
 */
@Data
public class DbConn {

    /**
     * mysql jdbc url模板
     */
    private static final String MYSQL_URL_TEMP = "jdbc:mysql://{host}:{port}/{database}?useUnicode=true&autoReconnect=true&useSSL=false";

    /**
     * connection缓存，最多允许15个connection同时连接，移除connection时候执行close操作
     */
    private static Cache<Long, DbConn> connectionCache = CacheBuilder.<Long, DbConn>newTimedBuilder(30, TimeUnit.MINUTES)
            .capacity(15).removeCallback(DbConn::close).build();

    private Long id;

    private String database;

    private Connection connection;


    public static DbConn getConnection(Long dbId, Supplier<DbDO> dbSupplier) {
        return connectionCache.get(dbId, () -> {
            DbDO db = dbSupplier.get();
            DbConn di = new DbConn();
            di.setDatabase(db.getDatabase());
            di.setId(db.getId());
            di.setConnection(genConnection(getJdbcUrl(db), db.getUsername(), db.getPassword()));
            return di;
        });
    }

    private static String getJdbcUrl(DbDO db) {
        if (Objects.equals(db.getType(), DbTypeEnum.MYSQL.getValue())) {
            return BracePlaceholder.resolveByObject(MYSQL_URL_TEMP, db);
        }
        if (DbTypeEnum.ORACLE.getValue().equals(db.getType())) {
            if (!StringUtils.isEmpty(db.getJdbcUrl())) {
                return db.getJdbcUrl();
            }
        }
        throw BizAssert.newException("数据库类型错误");
    }

    public static Connection genConnection(String url, String username, String password) {
        try {
            return DriverManager.getConnection(url, username, password);
        } catch (SQLException e) {
            throw BizAssert.newException("连接失败");
        }
    }

    public void close() {
        try {
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
