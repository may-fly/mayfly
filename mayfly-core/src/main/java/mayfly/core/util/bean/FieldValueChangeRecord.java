package mayfly.core.util.bean;

/**
 * @author meilin.huang
 * @version 1.0
 * @date 2019-12-19 4:18 下午
 */
public class FieldValueChangeRecord {

    /**
     * 字段名
     */
    private final String fieldName;

    /**
     * 旧值
     */
    private final Object oldValue;

    /**
     * 新值
     */
    private final Object newValue;

    public FieldValueChangeRecord(String fieldName, Object oldValue, Object newValue) {
        this.fieldName = fieldName;
        this.oldValue = oldValue;
        this.newValue = newValue;
    }

    public String getFieldName() {
        return fieldName;
    }

    public Object getOldValue() {
        return oldValue;
    }

    public Object getNewValue() {
        return newValue;
    }
}
