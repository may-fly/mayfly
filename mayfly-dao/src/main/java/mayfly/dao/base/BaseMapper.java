package mayfly.dao.base;


import mayfly.core.util.ReflectionUtils;
import mayfly.core.util.StringUtils;
import mayfly.core.util.annotation.AnnotationUtils;
import mayfly.dao.base.annotation.NoColumn;
import mayfly.dao.base.annotation.Primary;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.builder.annotation.ProviderContext;
import org.apache.ibatis.jdbc.SQL;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Stream;

/**
 * meilin.huang
 * @param <Entity>
 */
public interface BaseMapper<Entity> {

    /**
     * 插入新对象,并返回主键id值
     * @param entity 实体对象
     * @return
     */
    @InsertProvider(type = InsertSqlProvider.class, method = "sql")
    @Options(useGeneratedKeys = true)
    Integer insert(Entity entity);

//    @InsertProvider(type = BatchInsertSqlProvider.class, method = "sql")
//    @Options(useGeneratedKeys = true)
//    Integer batchInsert(List<Entity> entities);

    /**
     * 根据主键id更新实体，若实体field为null，则对应数据库的字段也更新为null
     * @param entity
     * @return
     */
    @UpdateProvider(type = UpdateSqlProvider.class, method = "sql")
    Integer updateByPrimaryKey(Entity entity);

    /**
     * 根据主键id更新实体，若实体field为null，则对应数据库的字段不更新
     * @param entity
     * @return
     */
    @UpdateProvider(type = UpdateSelectiveSqlProvider.class, method = "sql")
    Integer updateByPrimaryKeySelective(Entity entity);

    @DeleteProvider(type = DeleteSqlProvider.class, method = "sql")
    Integer deleteByPrimaryKey(Integer id);

    @DeleteProvider(type = DeleteByCriteriaSqlProvider.class, method = "sql")
    Integer deleteByCriteria(Entity criteria);

    @SelectProvider(type = SelectOneSqlProvider.class, method = "sql")
    Entity selectByPrimaryKey(Integer id);

    @SelectProvider(type = SelectAllSqlProvider.class, method = "sql")
    List<Entity> selectAll(String orderBy);

    @SelectProvider(type = SelectByCriteriaSqlProvider.class, method = "sql")
    List<Entity> selectByCriteria(Entity criteria);

    /**
     * 根据条件查询单个数据
     *
     * @param criteria
     * @return
     */
    @SelectProvider(type = SelectByCriteriaSqlProvider.class, method = "sql")
    Entity selectOneByCriteria(Entity criteria);

    @SelectProvider(type = CountSqlProvider.class, method = "sql")
    Long count();

    @SelectProvider(type = CountByCriteriaSqlProvider.class, method = "sql")
    Long countByCriteria(Entity criteria);


    class InsertSqlProvider extends SqlProviderSupport {
        public String sql(ProviderContext context) {
            TableInfo table = tableInfo(context);

            return new SQL()
                    .INSERT_INTO(table.getTableName())
                    .INTO_COLUMNS(table.getColumns())
                    .INTO_VALUES(Stream.of(table.getFields()).map(this::bindParameter).toArray(String[]::new))
                    .toString();

        }
    }

    class BatchInsertSqlProvider extends SqlProviderSupport {
        public String sql(Object entities, ProviderContext context) {
            TableInfo table = tableInfo(context);

            int size = ((List)((Map)entities).get("list")).size();
            String value = "(" + String.join(",", Stream.of(table.getFields()).map(this::bindParameter).toArray(String[]::new)) + ")";
            String[] values = new String[size];
            Arrays.fill(values, value);

            SQL sql = new SQL()
                    .INSERT_INTO(table.getTableName())
                    .INTO_COLUMNS(table.getColumns());
            StringBuilder sqlBuilder =  new StringBuilder(sql.toString());
            sqlBuilder.append(" VALUES ");
            sqlBuilder.append(String.join(",", values));
            return sqlBuilder.toString();
        }
    }

    class UpdateSqlProvider extends SqlProviderSupport {
        public String sql(ProviderContext context) {
            TableInfo table = tableInfo(context);

            return new SQL()
                    .UPDATE(table.getTableName())
                    .SET(Stream.of(table.getFields())
                            .filter(field -> !table.getPrimaryKeyColumn().equals(columnName(field)))
                            .map(field -> columnName(field) + " = " + bindParameter(field)).toArray(String[]::new))
                    .WHERE(table.getPrimaryKeyColumn() + " = #{id}")
                    .toString();
        }
    }

    class UpdateSelectiveSqlProvider extends SqlProviderSupport {
        public String sql(Object entity, ProviderContext context) {
            TableInfo table = tableInfo(context);

            return new SQL()
                    .UPDATE(table.getTableName())
                    .SET(Stream.of(table.getFields())
                            .filter(field -> ReflectionUtils.getFieldValue(field, entity) != null && !table.getPrimaryKeyColumn().equals(columnName(field)))
                            .map(field -> columnName(field) + " = " + bindParameter(field)).toArray(String[]::new))
                    .WHERE(table.getPrimaryKeyColumn() + " = #{id}")
                    .toString();
        }
    }

    class DeleteSqlProvider extends SqlProviderSupport {
        public String sql(ProviderContext context) {
            TableInfo table = tableInfo(context);

            return new SQL()
                    .DELETE_FROM(table.getTableName())
                    .WHERE(table.getPrimaryKeyColumn() + " = #{id}")
                    .toString();
        }
    }

    class DeleteByCriteriaSqlProvider extends SqlProviderSupport {
        public String sql(Object criteria, ProviderContext context) {
            TableInfo table = tableInfo(context);

            return new SQL()
                    .DELETE_FROM(table.getTableName())
                    .WHERE(Stream.of(table.getFields())
                            .filter(field -> ReflectionUtils.getFieldValue(field, criteria) != null)
                            .map(field -> columnName(field) + " = " + bindParameter(field))
                            .toArray(String[]::new))
                    .toString();
        }
    }

    class SelectOneSqlProvider extends SqlProviderSupport {
        public String sql(ProviderContext context) {
            TableInfo table = tableInfo(context);

            return new SQL()
                    .SELECT(table.getSelectColumns())
                    .FROM(table.getTableName())
                    .WHERE(table.getPrimaryKeyColumn() + " = #{id}")
                    .toString();
        }
    }

    class SelectAllSqlProvider extends SqlProviderSupport {
        public String sql(String orderBy, ProviderContext context) {
            TableInfo table = tableInfo(context);
            SQL sql = new SQL()
                    .SELECT(table.getSelectColumns())
                    .FROM(table.getTableName());
            if (StringUtils.isEmpty(orderBy)) {
                orderBy = table.getPrimaryKeyColumn() + " DESC";
            }
            return sql.ORDER_BY(orderBy).toString();
        }
    }

    class SelectByCriteriaSqlProvider extends SqlProviderSupport {
        public String sql(Object criteria, ProviderContext context) {
            TableInfo table = tableInfo(context);
            return new SQL()
                    .SELECT(table.getSelectColumns())
                    .FROM(table.getTableName())
                    .WHERE(Stream.of(table.getFields())
                            .filter(field -> ReflectionUtils.getFieldValue(field, criteria) != null)
                            .map(field -> columnName(field) + " = " + bindParameter(field))
                            .toArray(String[]::new)).ORDER_BY(table.getPrimaryKeyColumn() + " DESC").toString();
        }
    }

    class CountByCriteriaSqlProvider extends SqlProviderSupport {
        public String sql(Object criteria, ProviderContext context) {
            TableInfo table = tableInfo(context);
            return new SQL()
                    .SELECT("COUNT(*)")
                    .FROM(table.getTableName())
                    .WHERE(Stream.of(table.getFields())
                            .filter(field -> ReflectionUtils.getFieldValue(field, criteria) != null)
                            .map(field -> columnName(field) + " = " + bindParameter(field)).toArray(String[]::new))
                    .toString();
        }
    }

    class CountSqlProvider extends SqlProviderSupport {
        public String sql(Object criteria, ProviderContext context) {
            TableInfo table = tableInfo(context);
            return new SQL()
                    .SELECT("COUNT(*)")
                    .FROM(table.getTableName())
                    .toString();
        }
    }


    abstract class SqlProviderSupport {

        /**
         * 表前缀
         */
        private static final String TABLE_PREFIX = "tb_";

        /**
         * 主键名
         */
        private static final String DEFAULT_PRIMARY_KEY = "id";

        /**
         * key:interface class   value:tableInfo
         */
        private static Map<Class, TableInfo> tableCache = new ConcurrentHashMap<>(256);


        /**
         * 获取表信息结构
         * @param context
         * @return
         */
        protected TableInfo tableInfo(ProviderContext context) {
            TableInfo info = tableCache.get(context.getMapperType());
            if (info != null) {
                return info;
            }

            Class<?> entityClass = entityType(context);
            //获取不含有@NoColumn注解的fields
            Field[] fields = excludeNoColumnField(ReflectionUtils.getFields(entityClass));
            info = TableInfo.entityClass(entityClass)
                    .fields(fields)
                    .tableName(tableName(entityClass))
                    .primaryKeyColumn(primaryKeyColumn(fields))
                    .columns(columns(fields))
                    .selectColumns(selectColumns(fields))
                    .build();

            tableCache.put(context.getMapperType(), info);
            return info;
        }

        /**
         * 获取BaseMapper接口中的泛型类型
         * @param context
         * @return
         */
        protected Class<?> entityType(ProviderContext context) {
            return Stream.of(context.getMapperType().getGenericInterfaces())
                    .filter(ParameterizedType.class::isInstance)
                    .map(ParameterizedType.class::cast)
                    .filter(type -> type.getRawType() == BaseMapper.class)
                    .findFirst()
                    .map(type -> type.getActualTypeArguments()[0])
                    .filter(Class.class::isInstance).map(Class.class::cast)
                    .orElseThrow(() -> new IllegalStateException("未找到BaseMapper的泛型类 " + context.getMapperType().getName() + "."));
        }


        protected String tableName(Class<?> entityType) {
            return TABLE_PREFIX + StringUtils.camel2Underscore(entityType.getSimpleName());
        }

        /**
         * 过滤含有@NoColumn注解的field
         * @param totalField  entityClass所有的字段
         * @return   不包含@NoColumn注解的fields
         */
        protected Field[] excludeNoColumnField(Field[] totalField) {
            return Stream.of(totalField)
                    //过滤含有@NoColumn注解的field
                    .filter(field -> !AnnotationUtils.isAnnotationPresent(field, NoColumn.class))
                    .toArray(Field[]::new);
        }
        /**
         * 获取查询对应的字段 (不包含pojo中含有@NoColumn主键的属性)
         *
         * @param fields p
         * @return
         */
        protected String[] selectColumns(Field[] fields) {
            return Stream.of(fields).map(this::selectColumnName).toArray(String[]::new);
        }

        /**
         * 获取所有pojo所有属性对应的数据库字段 (不包含pojo中含有@NoColumn主键的属性)
         *
         * @param fields entityClass所有fields
         * @return
         */
        protected String[] columns(Field[] fields) {
            return Stream.of(fields).map(this::columnName).toArray(String[]::new);
        }

        /**
         * 如果fields中含有@Primary的字段，则返回该字段名为主键，否则默认'id'为主键名
         * @param fields entityClass所有fields
         * @return  主键column(驼峰转为下划线)
         */
        protected String primaryKeyColumn(Field[] fields) {
            return Stream.of(fields).filter(field -> field.isAnnotationPresent(Primary.class))
                    .findFirst()    //返回第一个primaryKey的field
                    .map(this::columnName)
                    .orElse(DEFAULT_PRIMARY_KEY);
        }

        /**
         * 获取单个属性对应的数据库字段（带有下划线字段将其转换为"字段 AS pojo属性名"形式）
         *
         * @param field
         * @return
         */
        protected String selectColumnName(Field field) {
            String camel = StringUtils.camel2Underscore(field.getName());
            return camel.contains("_") ? camel + " AS " + field.getName() : camel;
        }

        /**
         * 获取单个属性对应的数据库字段
         *
         * @param field  entityClass中的field
         * @return
         */
        protected String columnName(Field field) {
            return StringUtils.camel2Underscore(field.getName());
        }

        protected String bindParameter(Field field) {
            return "#{" + field.getName() + "}";
        }
    }
}
