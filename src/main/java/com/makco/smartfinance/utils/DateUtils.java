package com.makco.smartfinance.utils;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.Date;

/**
 * Created by mcalancea on 2016-06-09.
 */
public class DateUtils {
    public static Date convertLocalDateToUtilDate(LocalDate localDate) throws Exception {
        return Date.from(localDate.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
    }

    public static LocalDateTime convertUtilDateToLocalDateTime(Date date) throws Exception {
        Instant instant = Instant.ofEpochMilli(date.getTime());
        return LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
    }

    public static LocalDate convertUtilDateToLocalDate(Date date) throws Exception {
        return convertUtilDateToLocalDateTime(date).toLocalDate();
    }

    public static LocalTime convertUtilDateToLocalTime(Date date) throws Exception {
        return convertUtilDateToLocalDateTime(date).toLocalTime();
    }
}
