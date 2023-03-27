package com.jiamian.translation.util;

import com.jiamian.translation.common.enums.DateTimeFormatterEnum;
import org.apache.commons.lang3.StringUtils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;

public class DateUtil {

    public static final String LONGDATEFORMAT = "yyyyMMddHHmmss";
    public static final String LONGDATEFORMAT2 = "yyMMddHHmmss";

    /**
     * 获取服务器当前时间
     */
    public static Date getCurrentDate() {
        return new Date();
    }

    /**
     * 获得考试所用时间
     */
    public static String getLastTime(int seconds) {
        String last_time;
        // 转化格式
        int minute = seconds / 60;
        int second = seconds - minute * 60;
        if (second > 9) {
            last_time = minute + "分" + second + "秒";
        } else {
            last_time = minute + "分0" + second + "秒";
        }
        return last_time;
    }

    /**
     * 获得当前时间
     */
    public static String getDate() {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date();
        String time = dateFormat.format(date);
        return time;
    }

    /**
     * 获得半小时后的时间戳
     */
    public static Long getTimestampHalfHourLater() {
        return System.currentTimeMillis() + 30 * 60 * 1000;
    }

    /**
     * 获得当前时间加2个小时以后的时间,用于token的过期时间
     */
    public static Date get2HoursLater() {
        Date date = new Date();
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(Calendar.MINUTE, 120);
        return c.getTime();
    }

    /**
     * 获得当前时间,用于保存文件
     */
    public static String getDateStr() {
        return getDateStr(null, LONGDATEFORMAT2);
    }

    public static String getDateStr(String pattern) {
        return getDateStr(null, pattern);
    }

    public static String getDateStr(Date date, String pattern) {
        if (date == null) {
            date = new Date();
        }
        if (pattern == null) {
            pattern = LONGDATEFORMAT2;
        }
        DateFormat dateFormat = new SimpleDateFormat(pattern);
        String time = dateFormat.format(date);
        return time;
    }

    /**
     * 微博时间格式化 07.04
     */
    public static String parseWbDate(String month, String day) {
        if (month.length() == 1) {
            month = "0" + month;
        }
        if (day.length() == 1) {
            day = "0" + day;
        }
        return month + "月" + day + "日";
    }

    /**
     * 获取当月最开始的一刻
     *
     * @param date
     * @return
     */
    public static Date getMonthFirstDay(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        return calendar.getTime();
    }

    /**
     * 获取当前时间的月份最后一刻
     *
     * @param date
     * @return
     */
    public static Date getMonthLastDay(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        return calendar.getTime();
    }

    public static Date getMonthAgo(Date date, int month) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.MONTH, -month);
        return calendar.getTime();
    }

    public static Date getDayFirstTime(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        return calendar.getTime();
    }

    public static Date getDayLastTime(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        return calendar.getTime();
    }


    /**
     * LocalDateTime转换为Date
     *
     * @param localDateTime
     */
    public static Date localDateTime2Date(LocalDateTime localDateTime) {
        ZoneId zoneId = ZoneId.systemDefault();
        ZonedDateTime zdt = localDateTime.atZone(zoneId);
        return Date.from(zdt.toInstant());
    }

    public static LocalDateTime getLocalDateTimeFromTimeStamp(long timestamp){
        Instant instant = Instant.ofEpochMilli(timestamp);
        return LocalDateTime.ofInstant(instant, ZoneId.of("+8"));
    }
    /**
     * yyyyMMddHHmmss
     */
    static DateTimeFormatter YYYYMMDDHHMMSS = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");

    static DateTimeFormatter YYYYMMDDHHMMSSS = DateTimeFormatter.ofPattern("yyyyMMddHHmmssS");

    static DateTimeFormatter YYYYMMDDHHMMSSSN = DateTimeFormatter.ofPattern("yyyyMMddHHmmssSN");

    static DateTimeFormatter YYYYMMDD = DateTimeFormatter.ofPattern("yyyyMMdd");

    static DateTimeFormatter YYYYMM = DateTimeFormatter.ofPattern("yyyyMM");

    static DateTimeFormatter YYYYMMDDHHMMSS2 = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public static String format(LocalDate localDate) {
        return format(localDate, 3);
    }

    public static String formatYYYYMM(LocalDate localDate) {
        try {
            if (Objects.nonNull(localDate)) {
                return YYYYMM.format(localDate);
            }
        } catch (Exception e) {
        }
        return null;
    }

    public static String formatYYYYMMDD() {
        try {
            return YYYYMMDD.format(LocalDate.now());
        } catch (Exception e) {
        }
        return null;
    }

    public static String formatYYYYMMDD(LocalDateTime localDateTime) {
        try {
            return YYYYMMDD.format(localDateTime);
        } catch (Exception e) {
        }
        return null;
    }

    /**
     * yyyy-MM-dd HH:mm:ss
     *
     * @param localDate
     * @return
     * @Author buganhuang
     * @CreateDate 2018年5月5日
     */
    public static String formatYYYYMMDDHHMMSS(LocalDateTime localDate) {
        try {
            if (Objects.nonNull(localDate)) {
                return YYYYMMDDHHMMSS2.format(localDate);
            }
        } catch (Exception e) {
        }
        return null;
    }

    public static String formatYYYYMM(LocalDateTime localDate) {
        try {
            if (Objects.nonNull(localDate)) {
                return YYYYMM.format(localDate);
            }
        } catch (Exception e) {
        }
        return null;
    }

    public static String formatYYYYMMDD(LocalDate localDate) {
        try {
            if (Objects.nonNull(localDate)) {
                return YYYYMMDD.format(localDate);
            }
        } catch (Exception e) {
        }
        return null;
    }

    /**
     * 将LocalDate转化为字符串格式
     *
     * @param localDate
     * @param type
     * @return
     */
    public static String format(LocalDate localDate, Integer type) {
        try {
            if (Objects.nonNull(localDate)) {
                DateTimeFormatterEnum format = DateTimeFormatterEnum.getByType(type);
                return format.getFormatter().format(localDate);
            }
        } catch (Exception e) {
        }
        return null;
    }

    public static LocalDate toLocalDate(String date) {
        return toLocalDate(date, 5);
    }

    /**
     * 将String转化为LocalDateTime
     *
     * @param date
     * @param type
     * @return
     */
    public static LocalDate toLocalDate(String date, Integer type) {
        try {
            if (StringUtils.isNotBlank(date)) {
                DateTimeFormatterEnum format = DateTimeFormatterEnum.getByType(type);
                return LocalDate.parse(date, format.getFormatter());
            }
        } catch (Exception e) {
        }
        return null;
    }

    /**
     * 返回YYYYMMDDHHMMSS
     *
     * @param localDateTime
     * @return
     * @Author buganhuang
     * @CreateDate 2018年4月27日
     */
    public static String getDateStr(LocalDateTime localDateTime) {
        if (null == localDateTime) {
            return null;
        }
        return YYYYMMDDHHMMSS2.format(localDateTime);

    }

    /**
     * 返回YYYYMMDDHHMMSS
     *
     * @param localDateTime
     * @return
     * @Author buganhuang
     * @CreateDate 2018年4月27日
     */
    public static Long getDateLong(LocalDateTime localDateTime) {
        if (null == localDateTime) {
            return null;
        }
        return Long.valueOf(YYYYMMDDHHMMSS.format(localDateTime));

    }

    /**
     * 返回YYYYMMDDHHMMSS
     *
     * @param localDateTime
     * @return
     * @Author buganhuang
     * @CreateDate 2018年4月27日
     */
    public static Long getDateLong() {
        return Long.valueOf(YYYYMMDDHHMMSS.format(LocalDateTime.now()));
    }

    /**
     * 返回YYYYMMDDHHMMSS
     *
     * @param localDateTime
     * @return
     * @Author buganhuang
     * @CreateDate 2018年4月27日
     */
    public static LocalDateTime fromTimestampOfSecond(Long time) {
        LocalDateTime createTime = LocalDateTime.ofEpochSecond(time, 0, ZoneOffset.of("+8"));
        return createTime;
    }


    /**
     * 返回YYYYMMDDHHMMSS
     *
     * @param localDateTime
     * @return
     * @Author buganhuang
     * @CreateDate 2018年4月27日
     */
    public static Long getTimestamp(LocalDateTime localDateTime) {
        if (null == localDateTime) {
            return null;
        }
        return localDateTime.toInstant(ZoneOffset.of("+8")).getEpochSecond();

    }

    /**
     * 返回YYYYMMDDHHMMSS
     *
     * @param localDateTime
     * @return
     * @Author buganhuang
     * @CreateDate 2018年4月27日
     */
    public static Long getTimestamp() {
        return LocalDateTime.now().toInstant(ZoneOffset.of("+8")).getEpochSecond();

    }
}
