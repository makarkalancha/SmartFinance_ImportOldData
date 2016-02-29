package com.smartfinance.h2db;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;

/**
 * Created by mcalancea on 2016-02-26.
 */
public class DateUnit {
    private final static LocalDate EPOCH = LocalDate.ofEpochDay(0);
    private final java.util.Date date;
    private final LocalDate localDate;

    public DateUnit(java.util.Date date){
        this.date = date;
        this.localDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    }

    public java.sql.Date getUnitTimeStamp() {
        return new java.sql.Date(date.getTime());
    }

    public int getUnitYear() {
        return localDate.getYear();
    }

    public int getUnitMonthOfYear() {
        return localDate.getMonthValue();
    }

    public long getUnitMonth() {
        return ChronoUnit.MONTHS.between(EPOCH, localDate);
    }

    public long getUnitDate() {
        return ChronoUnit.DAYS.between(EPOCH, localDate);
    }

    public int getUnitDayOfWeek() {
        return localDate.getDayOfWeek().getValue();
    }

    public boolean getWeekDay() {
        return (getUnitDayOfWeek() == DayOfWeek.SUNDAY.getValue() || getUnitDayOfWeek() == DayOfWeek.SATURDAY.getValue()) ?
                false :
                true;
    }

}
