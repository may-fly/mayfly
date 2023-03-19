package mayfly.core.base.mapper;


import mayfly.core.base.mapper.annotation.NoColumn;
import mayfly.core.base.mapper.annotation.PrimaryKey;
import mayfly.core.base.mapper.annotation.Table;
import mayfly.core.util.CollectionUtils;
import mayfly.core.util.PlaceholderResolver;
import mayfly.core.util.ReflectionUtils;
import mayfly.core.util.StringUtils;
import org.apache.ibatis.annotations.DeleteProvider;
import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.UpdateProvider;
import org.apache.ibatis.builder.annotation.ProviderContext;
import org.apache.ibatis.jdbc.SQL;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Stream;

/**
 * 通用Mapper，实现基本功能
 *
 * @author meilin.huang
 * @param <E>  实体类型
 */
public interface BaseMapper<E> {

    /**
     * 插入新对象,并返回主键id值(id通过实体获取)
     *
     * @param entity 实体对象
     * @return  影响条数
     */
    @InsertProvider(type = InsertSqlProvider.class, method = "sql")
    @Options(useGeneratedKeys = true, keyColumn = TableInfo.DEFAULT_PRIMARY_KEY)
    int insert(E entity);

    /**
     * 插入新对象（只设置非空字段）,并返回主键id值(id通过实体获取)
     *
     * @param entity 实体对象
     * @return  影响条数
     */
    @InsertProvider(type = InsertSelectiveSqlProvider.class, method = "sql")
    @Options(useGeneratedKeys = true, keyColumn = TableInfo.DEFAULT_PRIMARY_KEY)
    int insertSelective(E entity);

    /**
     * 批量插入实体
     *
     * @param entities  实体列表
     * @return          影响条数
     */
    @InsertProvider(type = BatchInsertSqlProvider.class, method = "sql")
    int batchInsert(@Param("entities") List<E> entities);

    /**
     * 根据主键id更新实体，若实体field为null，则对应数据库的字段也更新为null
     *
     * @param entity  实体对象
     * @return         影响条数
     */
    @UpdateProvider(type = UpdateSqlProvider.class, method = "sql")
    int updateByPrimaryKey(E entity);

    /**
     * 根据主键id更新实体，若实体field为null，则对应数据库的字段不更新
     *
     * @param entity  实体对象
     * @return        影响条数
     */
    @UpdateProvider(type = UpdateSelectiveSqlProvider.class, method = "sql")
    int updateByPrimaryKeySelective(E entity);

    /**
     * 根据主键id删除
     *
     * @param id  id
     * @return  影响条数
     */
    @DeleteProvider(type = DeleteSqlProvider.class, method = "sql")
    int deleteByPrimaryKey(Long id);

    /**
     * 伪删除，即将is_deleted字段更新为1
     *
     * @param id id
     * @return  影响条数
     */
    @UpdateProvider(type = FakeDeleteSqlProvider.class, method = "sql")
    int fakeDeleteByPrimaryKey(Long id);

    /**
     * 根据实体条件删除
     *
     * @param criteria  实体
     * @return  影响条数
     */
    @DeleteProvider(type = DeleteByCriteriaSqlProvider.class, method = "sql")
    int deleteByCriteria(E criteria);

    /**
     * 根据id查询实体
     *
     * @param id  id
     * @return    实体
     */
    @SelectProvider(type = SelectOneSqlProvider.class, method = "sql")
    E selectByPrimaryKey(Long id);

    /**
     * 查询所有实体
     *
     * @param orderBy  排序
     * @return   实体list
     */
    @SelectProvider(type = SelectAllSqlProvider.class, method = "sql")
    List<E> selectAll(String orderBy);

    /**
     * 根据id列表查询实体列表
     * @param ids  id列表
     * @return  list
     */
    @SelectProvider(type = SelectByPrimaryKeyInSqlProvider.class, method = "sql")
    List<E> selectByPrimaryKeyIn(@Param("ids") List<Long> ids);

    /**
     * 根据实体条件查询符合条件的实体list
     * @param criteria  条件实体
     * @return          list
     */
    @SelectProvider(type = SelectByCriteriaSqlProvider.class, method = "sql")
    List<E> selectByCriteria(E criteria);

    /**
     * 根据条件查询单个数据
     *
     * @param criteria  实体条件
     * @return          实体对象
     */
    @SelectProvider(type = SelectByCriteriaSqlProvider.class, method = "sql")
    E selectOneByCriteria(E criteria);

    /**
     * 返回实体总数
     *
     * @return  总数
     */
    @SelectProvider(type = CountSqlProvider.class, method = "sql")
    long count();

    /**
     * 根据条件查询符合条件的实体总数
     *
     * @param criteria  实体条件
     * @return    数量
     */
    @SelectProvider(type = CountByCriteriaSqlProvider.class, method = "sql")
    long countByCriteria(E criteria);


    /**
     * 插入provider
     */
    class InsertSqlProvider extends BaseSqlProviderSupport {
        /**
         * sql
         * @param context context
         * @return  sql
         */
        public String sql(ProviderContext context) {
            TableInfo table = tableInfo(context);

            return new SQL()
                    .INSERT_INTO(table.tableName)
                    .INTO_COLUMNS(table.columns)
                    .INTO_VALUES(Stream.of(table.fields).map(TableInfo::bindParameter).toArray(String[]::new))
                    .toString();

        }
    }

    /**
     * 插入非空字段
     */
    class InsertSelectiveSqlProvider extends BaseSqlProviderSupport {
        /**
         * sql
         * @param entity  entity
         * @param context context
         * @return  sql
         */
        public String sql(Object entity, ProviderContext context) {
            TableInfo table = tableInfo(context);

            Field[] notNullFields = Stream.of(table.fields)
                    .filter(field -> ReflectionUtils.getFieldValue(field, entity) != null && !table.primaryKeyColumn.equals(TableInfo.columnName(field)))
                    .toArray(Field[]::new);

            return new SQL()
                    .INSERT_INTO(table.tableName)
                    .INTO_COLUMNS(TableInfo.columns(notNullFields))
                    .INTO_VALUES(Stream.of(notNullFields).map(TableInfo::bindParameter).toArray(String[]::new))
                    .toString();

        }
    }

    /**
     * 批量插入provider
     */
    class BatchInsertSqlProvider extends BaseSqlProviderSupport {
        /**
         * sql
         * @param param  mybatis @Param注解绑定的param map
         * @param context context
         * @return  sql
         */
        public String sql(Map<String, Object> param, ProviderContext context) {
            TableInfo table = tableInfo(context);
            @SuppressWarnings("unchecked")
            int size = ((List<Object>)param.get("entities")).size();
            // 构造 ( #{entities[1-->数组索引].fieldName}, #{entities[1].fieldName2})
            String value = "(" + String.join(",", Stream.of(table.fields)
                    .map(field -> "#{entities[${index}]." + field.getName() + "}").toArray(String[]::new)) + ")";
            String[] values = new String[size];
            Map<String, Object> fillIndex = new HashMap<>(2);
            for (int i = 0; i < size; i++) {
                fillIndex.put("index", i);
                values[i] = PlaceholderResolver.getDefaultResolver().resolveByMap(value, fillIndex);
            }

            SQL sql = new SQL()
                    .INSERT_INTO(table.tableName)
                    .INTO_COLUMNS(table.columns);
            return sql.toString() + " VALUES " + String.join(",", values);
        }
    }

    /**
     * 更新provider
     */
    class UpdateSqlProvider extends BaseSqlProviderSupport {
        /**
         * sql
         * @param context context
         * @return  sql
         */
        public String sql(ProviderContext context) {
            TableInfo table = tableInfo(context);

            return new SQL()
                    .UPDATE(table.tableName)
                    .SET(Stream.of(table.fields)
                            .filter(field -> !table.primaryKeyColumn.equals(TableInfo.columnName(field)))
                            .map(TableInfo::assignParameter).toArray(String[]::new))
                    .WHERE(table.getPrimaryKeyWhere())
                    .toString();
        }
    }

    /**
     * 只能新非空字段 provider
     */
    class UpdateSelectiveSqlProvider extends BaseSqlProviderSupport {
        /**
         * sql
         * @param entity  entity
         * @param context context
         * @return  sql
         */
        public String sql(Object entity, ProviderContext context) {
            TableInfo table = tableInfo(context);

            return new SQL()
                    .UPDATE(table.tableName)
                    .SET(Stream.of(table.fields)
                            .filter(field -> ReflectionUtils.getFieldValue(field, entity) != null && !table.primaryKeyColumn.equals(TableInfo.columnName(field)))
                            .map(TableInfo::assignParameter).toArray(String[]::new))
                    .WHERE(table.getPrimaryKeyWhere())
                    .toString();
        }
    }

    /**
     * 删除provider
     */
    class DeleteSqlProvider extends BaseSqlProviderSupport {
        public String sql(ProviderContext context) {
            TableInfo table = tableInfo(context);

            return new SQL()
                    .DELETE_FROM(table.tableName)
                    .WHERE(table.primaryKeyColumn + " = #{id}")
                    .toString();
        }
    }

    /**
     * 伪删除
     */
    class FakeDeleteSqlProvider extends BaseSqlProviderSupport {
        public String sql(ProviderContext context) {
            TableInfo table = tableInfo(context);

            return new SQL()
                    .UPDATE(table.tableName)
                    .SET("is_deleted = 1")
                    .WHERE(table.primaryKeyColumn + " = #{id}")
                    .toString();
        }
    }

    /**
     * 根据条件删除
     */
    class DeleteByCriteriaSqlProvider extends BaseSqlProviderSupport {
        /**
         * sql
         * @param criteria  entity condition
         * @param context context
         * @return  sql
         */
        public String sql(Object criteria, ProviderContext context) {
            TableInfo table = tableInfo(context);

            return new SQL()
                    .DELETE_FROM(table.tableName)
                    .WHERE(Stream.of(table.fields)
                            .filter(field -> ReflectionUtils.getFieldValue(field, criteria) != null)
                            .map(TableInfo::assignParameter)
                            .toArray(String[]::new))
                    .toString();
        }
    }

    /**
     * 单条数据查询
     */
    class SelectOneSqlProvider extends BaseSqlProviderSupport {
        /**
         * sql
         * @param context context
         * @return  sql
         */
        public String sql(ProviderContext context) {
            TableInfo table = tableInfo(context);

            return new SQL()
                    .SELECT(table.selectColumns)
                    .FROM(table.tableName)
                    .WHERE(table.getPrimaryKeyWhere())
                    .toString();
        }
    }

    /**
     * 查询所有记录
     */
    class SelectAllSqlProvider extends BaseSqlProviderSupport {
        /**
         * sql
         * @param orderBy  排序字段
         * @param context context
         * @return  sql
         */
        public String sql(String orderBy, ProviderContext context) {
            TableInfo table = tableInfo(context);
            SQL sql = new SQL()
                    .SELECT(table.selectColumns)
                    .FROM(table.tableName);
            if (StringUtils.isEmpty(orderBy)) {
                orderBy = table.primaryKeyColumn + " DESC";
            }
            return sql.ORDER_BY(orderBy).toString();
        }
    }

    /**
     * 根据id列表查询
     */
    class SelectByPrimaryKeyInSqlProvider extends BaseSqlProviderSupport {
        public String sql(Map<String, Object> params, ProviderContext context) {
            @SuppressWarnings("unchecked")
            List<Object> ids = (List<Object>)params.get("ids");
            TableInfo table = tableInfo(context);
            return new SQL()
                    .SELECT(table.selectColumns)
                    .FROM(table.tableName)
                    .WHERE(table.primaryKeyColumn
                            + " IN (" + String.join(",", ids.stream().map(String::valueOf).toArray(String[]::new)) +")")
                    .toString();
        }
    }

    /**
     * 根据条件查询
     */
    class SelectByCriteriaSqlProvider extends BaseSqlProviderSupport {
        /**
         * sql
         * @param criteria  entity 条件
         * @param context context
         * @return  sql
         */
        public String sql(Object criteria, ProviderContext context) {
            TableInfo table = tableInfo(context);
            return new SQL()
                    .SELECT(table.selectColumns)
                    .FROM(table.tableName)
                    .WHERE(Stream.of(table.fields)
                            .filter(field -> ReflectionUtils.getFieldValue(field, criteria) != null)
                            .map(TableInfo::assignParameter)
                            .toArray(String[]::new)).ORDER_BY(table.primaryKeyColumn + " DESC").toString();
        }
    }

    /**
     * 根据条件统计
     */
    class CountByCriteriaSqlProvider extends BaseSqlProviderSupport {
        /**
         * sql
         * @param criteria  entity 条件
         * @param context context
         * @return  sql
         */
        public String sql(Object criteria, ProviderContext context) {
            TableInfo table = tableInfo(context);
            return new SQL()
                    .SELECT("COUNT(*)")
                    .FROM(table.tableName)
                    .WHERE(Stream.of(table.fields)
                            .filter(field -> ReflectionUtils.getFieldValue(field, criteria) != null)
                            .map(TableInfo::assignParameter).toArray(String[]::new))
                    .toString();
        }
    }

    /**
     * 统计所有数据
     */
    class CountSqlProvider extends BaseSqlProviderSupport {
        /**
         * sql
         * @param criteria  entity 条件
         * @param context context
         * @return  sql
         */
        public String sql(Object criteria, ProviderContext context) {
            TableInfo table = tableInfo(context);
            return new SQL()
                    .SELECT("COUNT(*)")
                    .FROM(table.tableName)
                    .toString();
        }
    }


    /**
     * 基类
     */
    abstract class BaseSqlProviderSupport {
        /**
         * key -> mapper class   value -> tableInfo
         */
        private static Map<Class<?>, TableInfo> tableCache = new ConcurrentHashMap<>(128);

        /**
         * 获取表信息结构
         *
         * @param context  provider context
         * @return  表基本信息
         */
        protected TableInfo tableInfo(ProviderContext context) {
            // 如果不存在则创建
            return tableCache.computeIfAbsent(context.getMapperType(), TableInfo::of);
        }
    }



    /**
     * table info
     *
     * @author meilin.huang
     * @date 2020-02-16 3:50 下午
     */
    class TableInfo {
        /**
         * 表前缀
         */
        private static final String TABLE_PREFIX = "t_";

        /**
         * 主键名
         */
        private static final String DEFAULT_PRIMARY_KEY = "id";

        /**
         * 表名
         */
        private String tableName;

        /**
         * 实体类型不含@NoColunm注解的field
         */
        private Field[] fields;

        /**
         * 主键列名
         */
        private String primaryKeyColumn;

        /**
         * 所有列名
         */
        private String[] columns;

        /**
         * 所有select sql的列名，有带下划线的将其转为aa_bb AS aaBb
         */
        private String[] selectColumns;

        private TableInfo() {}

        /**
         * 获取主键的where条件，如 id = #{id}
         *
         * @return  主键where条件
         */
        public String getPrimaryKeyWhere() {
            String pk = this.primaryKeyColumn;
            return pk + " = #{" + pk + "}";
        }

        /**
         * 获取TableInfo的简单工厂
         *
         * @param mapperType mapper类型
         * @return            {@link TableInfo}
         */
        public static TableInfo of(Class<?> mapperType) {
            Class<?> entityClass = entityType(mapperType);
            // 获取不含有@NoColumn注解的fields
            Field[] fields = excludeNoColumnField(entityClass);
            TableInfo tableInfo = new TableInfo();
            tableInfo.fields = fields;
            tableInfo.tableName = tableName(entityClass);
            tableInfo.primaryKeyColumn =  primaryKeyColumn(fields);
            tableInfo.columns = columns(fields);
            tableInfo.selectColumns = selectColumns(fields);
            return tableInfo;
        }

        /**
         * 获取BaseMapper接口中的泛型类型
         *
         * @param mapperType  mapper类型
         * @return       实体类型
         */
        public static Class<?> entityType(Class<?> mapperType) {
            return Stream.of(mapperType.getGenericInterfaces())
                    .filter(ParameterizedType.class::isInstance)
                    .map(ParameterizedType.class::cast)
                    .filter(type -> type.getRawType() == BaseMapper.class)
                    .findFirst()
                    .map(type -> type.getActualTypeArguments()[0])
                    .filter(Class.class::isInstance).map(Class.class::cast)
                    .orElseThrow(() -> new IllegalStateException("未找到BaseMapper的泛型类 " + mapperType.getName() + "."));
        }


        /**
         * 获取表名
         *
         * @param entityType  实体类型
         * @return      表名
         */
        public static String tableName(Class<?> entityType) {
            Table table = entityType.getAnnotation(Table.class);
            return table == null ? TABLE_PREFIX + StringUtils.camel2Underscore(entityType.getSimpleName()) : table.value();
        }

        /**
         * 过滤含有@NoColumn注解或者是静态的field
         *
         * @param entityClass 实体类型
         * @return 不包含@NoColumn注解的fields
         */
        public static Field[] excludeNoColumnField(Class<?> entityClass) {
            Field[] allFields = ReflectionUtils.getFields(entityClass);
            List<String> excludeColumns = getClassExcludeColumns(entityClass);
            return Stream.of(allFields)
                    //过滤掉类上指定的@NoCloumn注解的字段和字段上@NoColumn注解或者是静态的field
                    .filter(field -> !CollectionUtils.contains(excludeColumns, field.getName())
                            && (!field.isAnnotationPresent(NoColumn.class) && !Modifier.isStatic(field.getModifiers())))
                    .toArray(Field[]::new);
        }

        /**
         * 获取实体类上标注的不需要映射的字段名
         *
         * @param entityClass  实体类
         * @return             不需要映射的字段名
         */
        public static List<String> getClassExcludeColumns(Class<?> entityClass) {
            List<String> excludeColumns = null;
            NoColumn classNoColumns = entityClass.getAnnotation(NoColumn.class);
            if (classNoColumns != null) {
                excludeColumns = Arrays.asList(classNoColumns.fields());
            }
            return excludeColumns;
        }

        /**
         * 获取查询对应的字段 (不包含pojo中含有@NoColumn主键的属性)
         *
         * @param fields p
         * @return  所有需要查询的查询字段
         */
        public static String[] selectColumns(Field[] fields) {
            return Stream.of(fields).map(TableInfo::selectColumnName).toArray(String[]::new);
        }

        /**
         * 获取所有pojo所有属性对应的数据库字段 (不包含pojo中含有@NoColumn主键的属性)
         *
         * @param fields entityClass所有fields
         * @return        所有的column名称
         */
        public static String[] columns(Field[] fields) {
            return Stream.of(fields).map(TableInfo::columnName).toArray(String[]::new);
        }

        /**
         * 如果fields中含有@Primary的字段，则返回该字段名为主键，否则默认'id'为主键名
         *
         * @param fields entityClass所有fields
         * @return 主键column(驼峰转为下划线)
         */
        public static String primaryKeyColumn(Field[] fields) {
            return Stream.of(fields).filter(field -> field.isAnnotationPresent(PrimaryKey.class))
                    .findFirst()    //返回第一个primaryKey的field
                    .map(TableInfo::columnName)
                    .orElse(DEFAULT_PRIMARY_KEY);
        }

        /**
         * 获取单个属性对应的数据库字段（带有下划线字段将其转换为"字段 AS pojo属性名"形式）
         *
         * @param field  字段
         * @return      带有下划线字段将其转换为"字段 AS pojo属性名"形式
         */
        public static String selectColumnName(Field field) {
            String camel = columnName(field);
            return camel.contains("_") ? camel + " AS `" + field.getName() + "`" : camel;
        }

        /**
         * 获取单个属性对应的数据库字段
         *
         * @param field entityClass中的field
         * @return  字段对应的column
         */
        public static String columnName(Field field) {
            return "`" + StringUtils.camel2Underscore(field.getName()) + "`";
        }

        /**
         * 绑定参数
         *
         * @param field  字段
         * @return        参数格式
         */
        public static String bindParameter(Field field) {
            return "#{" + field.getName() + "}";
        }

        /**
         * 获取该字段的参数赋值语句，如 user_name = #{userName}
         * @param field  字段
         * @return       参数赋值语句
         */
        public static String assignParameter(Field field) {
            return columnName(field) + " = " + bindParameter(field);
        }
    }
}
