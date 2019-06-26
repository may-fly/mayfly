package mayfly.common.enums;

/**
 * 带有枚举值以及枚举说明的枚举接口(可使用{@link mayfly.common.util.EnumUtils}中的方法)
 * @author meilin.huang
 * @version 1.0
 * @date 2019-03-22 11:04 AM
 */
public interface NameValueEnum extends ValueEnum {
    /**
     * 获取枚举名称
     * @return
     */
    String getName();
}
