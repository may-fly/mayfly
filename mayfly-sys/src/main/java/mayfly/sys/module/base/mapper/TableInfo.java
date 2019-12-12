package mayfly.sys.module.base.mapper;

import java.lang.reflect.Field;

/**
 * table info
 *
 * @author hml
 * @date 2018/1/25 下午4:16
 */
public class TableInfo {
    /**
     * 表对应的实体类型
     */
    private Class<?> entityClass;

    /**
     * 实体类型不含@NoColunm注解的field
     */
    private Field[] fields;

    /**
     * 表名
     */
    private String tableName;

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

    public static Builder entityClass(Class<?> entityClass) {
        return new Builder(entityClass);
    }

    public static class Builder {
        private TableInfo tableInfo;

        public Builder(Class<?> entityClass) {
            tableInfo = new TableInfo();
            tableInfo.setEntityClass(entityClass);
        }

        public Builder fields(Field[] fields) {
            tableInfo.setFields(fields);
            return this;
        }

        public Builder tableName(String tableName) {
            tableInfo.setTableName(tableName);
            return this;
        }

        public Builder primaryKeyColumn(String primaryKeyColumn) {
            tableInfo.setPrimaryKeyColumn(primaryKeyColumn);
            return this;
        }

        public Builder columns(String[] columns) {
            tableInfo.setColumns(columns);
            return this;
        }

        public Builder selectColumns(String[] columns) {
            tableInfo.setSelectColumns(columns);
            return this;
        }

        public TableInfo build() {
            return tableInfo;
        }
    }

    /**
     * getter setter
     **/

    public Class<?> getEntityClass() {
        return entityClass;
    }

    public void setEntityClass(Class<?> entityClass) {
        this.entityClass = entityClass;
    }

    public Field[] getFields() {
        return fields;
    }

    public void setFields(Field[] fields) {
        this.fields = fields;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getPrimaryKeyColumn() {
        return primaryKeyColumn;
    }

    public void setPrimaryKeyColumn(String primaryKeyColumn) {
        this.primaryKeyColumn = primaryKeyColumn;
    }

    public String[] getColumns() {
        return columns;
    }

    public void setColumns(String[] columns) {
        this.columns = columns;
    }

    public String[] getSelectColumns() {
        return selectColumns;
    }

    public void setSelectColumns(String[] selectColumns) {
        this.selectColumns = selectColumns;
    }
}
