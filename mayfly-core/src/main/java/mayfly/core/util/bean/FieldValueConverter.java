package mayfly.core.util.bean;

/**
 * Bean字段值转换 </br>
 * 泛型 T:bean中原始值类型 V:转换后的值类型，
 *
 * @author meilin.huang
 * @version 1.0
 * @date 2019-03-06 6:42 PM
 */
public interface FieldValueConverter<T, V> {

    /**
     * 字段值转换, 如将枚举值Integer转换为String类型的name <br/>
     * V:转换后的值， T:bean中原始值
     *
     * @param fieldValue 真实字段值
     */
    V convert(T fieldValue);
}
