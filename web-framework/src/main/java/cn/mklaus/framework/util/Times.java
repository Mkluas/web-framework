package cn.mklaus.framework.util;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.util.TimeZone;

import static java.time.format.DateTimeFormatter.ISO_LOCAL_DATE;
import static java.time.format.DateTimeFormatter.ISO_LOCAL_TIME;

/**
 * @author Mklaus
 * @date 2018-01-05 上午11:58
 */
public class Times {

    public static int now() {
        return (int) (System.currentTimeMillis() / 1000);
    }

    public static String format(int seconds) {
        LocalDateTime dateTime = LocalDateTime.ofEpochSecond(seconds, 0, ZoneOffset.ofHours(8));
        return dateTime.format(CUSTOM_DATE_TIME_FORMATTER);
    }

    public static TimeZone getShangHaiTimeZone() {
        return TimeZone.getTimeZone("Asia/Shanghai");
    }

    public static int startTimeOfTodayOnBeijinTime() {
        Long s = LocalDateTime.of(LocalDate.now(), LocalTime.of(0,0)).toEpochSecond(ZoneOffset.of("+8"));
        return s.intValue();
    }

    private static final DateTimeFormatter CUSTOM_DATE_TIME_FORMATTER;

    static {
        CUSTOM_DATE_TIME_FORMATTER = new DateTimeFormatterBuilder()
                .parseCaseInsensitive()
                .append(ISO_LOCAL_DATE)
                .appendLiteral(' ')
                .append(ISO_LOCAL_TIME)
                .toFormatter();
    }

}
