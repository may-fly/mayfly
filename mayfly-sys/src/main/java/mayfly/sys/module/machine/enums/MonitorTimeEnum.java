package mayfly.sys.module.machine.enums;

import mayfly.core.util.DateUtils;
import mayfly.core.util.enums.NameValueEnum;

import java.time.LocalDate;

/**
 * @author meilin.huang
 * @date 2020-08-31 5:05 下午
 */
public enum MonitorTimeEnum implements NameValueEnum<Integer> {
    /**
     * 今天
     */
    TODAY(0, "今天"),

    SEVEN_DAYS(7, "7天内"),

    THIRTY_DAYS(15, "15天内");

    private final Integer value;

    private final String desc;

    MonitorTimeEnum(Integer value, String desc) {
        this.value = value;
        this.desc = desc;
    }

    @Override
    public String getName() {
        return this.desc;
    }

    @Override
    public Integer getValue() {
        return this.value;
    }

    /**
     * 获取开始和结束时间
     *
     * @return 0 开始时间；1 结束时间
     */
    public String[] getBeginAndEndTime() {
        LocalDate now = LocalDate.now();
        String nowStr = DateUtils.defaultFormat(now);
        if (this.value == 0) {
            return new String[]{nowStr, nowStr};
        }
        return new String[]{DateUtils.defaultFormat(now.minusDays(this.value)), nowStr};
    }


}
