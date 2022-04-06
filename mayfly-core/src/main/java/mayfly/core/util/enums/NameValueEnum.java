package mayfly.core.util.enums;

/**
 * 扩展了{@link ValueEnum}接口. 带有枚举值以及枚举名称的枚举接口(可使用{@link EnumUtils}中的方法。<br/>
 * 如 {@link EnumUtils#getNameByValue(NameValueEnum[], Object) getNameByValue})等
 *
 * @author meilin.huang
 * @version 1.0
 */
public interface NameValueEnum<T> extends ValueEnum<T> {
    /**
     * 获取枚举名称
     *
     * @return 枚举名
     */
    String getName();
}
