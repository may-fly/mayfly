package mayfly.common.utils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;

/**
 * @author hml
 * @version 1.0
 * @description: 日期工具类
 * @date 2018-11-07 10:56 AM
 */
public final class DateUtils {

    private static DateTimeFormatter defaultFormatter = DateTimeFormatter.ofPattern("yyyy:MM:dd HH:mm:ss");

    public static String defaultFormat(LocalDateTime dateTime) {
        return defaultFormatter.format(dateTime);
    }

    /**
     * 格式化日期时间
     * @param temporal  可以是LocalDateTime, LocalDate以及LocalTime等常用日期时间类
     * @param pattern   yyyy:年 MM:月 dd:日 HH:小时 mm:分钟 ss:秒
     * @return
     */
    public static String formatDate(TemporalAccessor temporal, String pattern) {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern(pattern);
        return dtf.format(temporal);
    }


//    public static void main(String[] args) {
//        System.out.print(formatDate(LocalTime.now(), "HHmm"));
//    }
}
