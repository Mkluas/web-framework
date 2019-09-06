package cn.mklaus.framework.util;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.util.Date;
import java.util.TimeZone;

import static java.time.format.DateTimeFormatter.ISO_LOCAL_DATE;
import static java.time.format.DateTimeFormatter.ISO_LOCAL_TIME;

/**
 * @author Mklaus
 * Created on 2018-01-05 上午11:58
 */
public class Times {

    public static int now() {
        return (int) (System.currentTimeMillis() / 1000);
    }

    public static TimeZone getShangHaiTimeZone() {
        return TimeZone.getTimeZone("Asia/Shanghai");
    }

    public static int startTimeOfTodayOnBeijinTime() {
        return toSeconds(LocalDate.now());
    }

    /**
     * Convert
     */

    public static int toSeconds(LocalDateTime localDateTime) {
        return (int) localDateTime.toEpochSecond(ZoneOffset.of("+8"));
    }

    public static int toSeconds(LocalDate localDate) {
        return (int) localDate.atStartOfDay().toEpochSecond(ZoneOffset.of("+8"));
    }

    public static LocalDateTime toLocalDateTime(int seconds) {
        return LocalDateTime.ofEpochSecond(seconds, 0, ZoneOffset.of("+8"));
    }

    public static LocalDate toLocalDate(int seconds) {
        return toLocalDateTime(seconds).toLocalDate();
    }

    public static LocalDateTime toLocalDateTime(Date date) {
        return date.toInstant().atZone(getShangHaiTimeZone().toZoneId()).toLocalDateTime();
    }

    public static LocalDate toLocalDate(Date date) {
        return date.toInstant().atZone(getShangHaiTimeZone().toZoneId()).toLocalDate();
    }

    public static LocalDateTime toLocalDateTime(long seconds) {
        return LocalDateTime.ofEpochSecond(seconds, 0, ZoneOffset.of("+8"));
    }

    public static Date toDate(LocalDate localDate) {
        return toDate(localDate.atStartOfDay());
    }

    public static Date toDate(LocalDateTime localDateTime) {
        Instant instant = localDateTime.atZone(getShangHaiTimeZone().toZoneId()).toInstant();
        return Date.from(instant);
    }

    /**
     * Format
     */

    public static String format(int seconds) {
        LocalDateTime dateTime = LocalDateTime.ofEpochSecond(seconds, 0, ZoneOffset.ofHours(8));
        return dateTime.format(CUSTOM_DATE_TIME_FORMATTER);
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
