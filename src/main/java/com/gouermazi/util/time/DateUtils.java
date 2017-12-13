package com.gouermazi.util.time;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * created by tab chan on 2017/12/01
 */
public class DateUtils {
    /**
     * @param future 大日子
     * @param old    小日子
     * @return 两个日期相差的天数
     */
    public static long daysForm2Dates(Date future, Date old) {
        return TimeUnit.MILLISECONDS.toDays(future.getTime() - old.getTime());
    }

    /**
     * <p>
     * (2017,9,2, 13,43,12) 和 (2017,7,2, 13,43,12) 差2月,return 2<br>
     * (2017,9,2, 13,43,12) 和 (2017,8,2, 13,43,13) 不足一月return 0<br>
     * <p/>
     *
     * @param future 大日子
     * @param old    小日子
     * @return 两个日期相差的月数
     */
    public static long monthsForm2Dates(Date future, Date old) {
        LocalDateTime future1 = LocalDateTime.ofInstant(future.toInstant(), ZoneId.systemDefault());
        LocalDateTime old1 = LocalDateTime.ofInstant(old.toInstant(), ZoneId.systemDefault());
        return ChronoUnit.MONTHS.between(old1, future1);
    }

    /**
     * <p>
     * (2017,9,2, 13,43,12) 和 (2015,9,2, 13,43,12) 差一年,return 2<br>
     * (2017,9,2, 13,43,12) 和 (2016,9,2, 13,43,13) 不足一年return 0<br>
     * <p/>
     *
     * @param future 大日子
     * @param old    小日子
     * @return 两个日期相差的年数
     */
    public static long yearsForm2Dates(Date future, Date old) {
        LocalDateTime future1 = LocalDateTime.ofInstant(future.toInstant(), ZoneId.systemDefault());
        LocalDateTime old1 = LocalDateTime.ofInstant(old.toInstant(), ZoneId.systemDefault());
        return ChronoUnit.YEARS.between(old1, future1);
    }
}
