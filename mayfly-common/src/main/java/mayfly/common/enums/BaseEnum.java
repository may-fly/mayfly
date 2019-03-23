package mayfly.common.enums;

/**
 * V:枚举值类型
 * @author meilin.huang
 * @version 1.0
 * @date 2019-03-22 11:04 AM
 */
public interface BaseEnum {
    /**
     * 获取枚举值
     * @returnint
     */
    Integer getValue();

    /**
     * 获取枚举名称
     * @return
     */
    String getName();
}
