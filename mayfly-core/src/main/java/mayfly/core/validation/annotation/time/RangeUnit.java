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

    public static long getBetween(RangeUnit unit, Temporal date, Temporal date2) {
        switch (unit) {
            case DAYS:
                return ChronoUnit.DAYS.between(date, date2);
            case MONTH:
                return ChronoUnit.MONTHS.between(date, date2);
            case YEAR:
                return ChronoUnit.YEARS.between(date, date2);
            case WEEKS:
                return ChronoUnit.WEEKS.between(date, date2);
            case HOURS:
                return ChronoUnit.HOURS.between(date, date2);
            case MINUTES:
                return ChronoUnit.MINUTES.between(date, date2);
            case SECOND:
                return ChronoUnit.SECONDS.between(date, date2);
            default:
                return 0;
        }
    }

}
