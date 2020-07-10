package mayfly.core.validation.annotation.time;

import java.time.temporal.ChronoUnit;
import java.time.temporal.Temporal;

/**
 * @author meilin.huang
 * @date 2020-07-09 2:06 下午
 */
public enum RangeUnit {
    /**
     * 年
     */
    YEAR,

    MONTH,

    DAYS,

    WEEKS,

    HOURS,

    MINUTES,

    SECOND;

    public static Temporal plus(Temporal date, RangeUnit unit, long value) {
        switch (unit) {
            case DAYS:
                return date.plus(value, ChronoUnit.DAYS);
            case MONTH:
                return date.plus(value, ChronoUnit.MONTHS);
            case YEAR:
                return date.plus(value, ChronoUnit.YEARS);
            case WEEKS:
                return date.plus(value, ChronoUnit.WEEKS);
            case HOURS:
                return date.plus(value, ChronoUnit.HOURS);
            case MINUTES:
                return date.plus(value, ChronoUnit.MINUTES);
            case SECOND:
                return date.plus(value, ChronoUnit.SECONDS);
            default:
                return date;
        }
    }

}
