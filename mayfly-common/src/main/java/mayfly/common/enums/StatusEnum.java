package mayfly.common.enums;

import java.util.stream.Stream;

/**
 * @author meilin.huang
 * @version 1.0
 * @date 2018-12-07 1:43 PM
 */
public enum StatusEnum {
    /**
     * 禁用状态
     */
    DISABLE((byte)0),

    /**
     * 启用状态
     */
    ENABLE((byte)1);

    /**
     * 状态值
     */
    private byte value;
    StatusEnum(byte value) {
        this.value = value;
    }

    public byte value() {
        return value;
    }


    /**
     * 校验该状态值是否存在
     * @param value
     * @return
     */
    public static boolean checkStatusValue(byte value) {
        for (StatusEnum statusEnum : StatusEnum.values()) {
            if (value == statusEnum.value) {
                return true;
            }
        }
        return false;
    }

    /**
     * 将所有状态值转换为String类型，以便于在@ValueIn注解中使用
     * @return
     */
    public static String[] toStringArr() {
        return Stream.of(StatusEnum.values()).map(status -> String.valueOf(status.value)).toArray(String[]::new);
    }

}
