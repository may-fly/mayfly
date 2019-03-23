package mayfly.common.utils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.Temporal;

/**
 * @author hml
 * @version 1.0
 * @description: 日期工具类
 * @date 2018-11-07 10:56 AM
 */
public final class DateUtils {

    public static final String DEFAULT_DATETIME_PATTERN = "yyyy:MM:dd HH:mm:ss";

    public static final String DEFAULT_TIME_PATTERN = "HH:mm:ss";

    private static DateTimeFormatter defaultFormatter = DateTimeFormatter.ofPattern(DEFAULT_DATETIME_PATTERN);

    /**
     * 使用默认格式解析日期时间，格式为:yyyy:MM:dd HH:mm:ss
     * @param dateTime
     * @return
     */
    public static String defaultFormat(LocalDateTime dateTime) {
        return defaultFormatter.format(dateTime);
    }

    /**
     * 格式化日期时间
     * @param temporal  可以是LocalDateTime, LocalDate以及LocalTime等常用日期时间类
     * @param pattern   yyyy:年 MM:月 dd:日 HH:小时 mm:分钟 ss:秒
     * @return
     */
    public static String formatDate(Temporal temporal, String pattern) {
        return DateTimeFormatter.ofPattern(pattern).format(temporal);
    }
}
