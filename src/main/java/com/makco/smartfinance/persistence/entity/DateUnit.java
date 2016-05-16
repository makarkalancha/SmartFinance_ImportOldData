package com.makco.smartfinance.persistence.entity;

import com.google.common.base.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.sql.Timestamp;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

/**
 * Created by mcalancea on 2016-04-19.
 */
@Entity
@Table(name="DATEUNIT")
public class DateUnit {
    //epochDay - the Epoch Day to convert, based on the epoch 1970-01-01
    private final static LocalDate EPOCH = LocalDate.ofEpochDay(0);

    @Id
    @Column(name="UNITDAY", updatable = false)
    private Long unitDay;

    @Column(name="UNITDAYOFMONTH", updatable = false)
    private Integer unitDayOfMonth;

    @Column(name="UNITDAYOFYEAR", updatable = false)
    private Integer unitDayOfYear;

    @Column(name="UNITMONTH", updatable = false)
    private Long unitMonth;

    @Column(name="UNITMONTHOFYEAR", updatable = false)
    private Integer unitMonthOfYear;

    @Column(name="UNITYEAR", updatable = false)
    private Integer unitYear;

    @Column(name="UNITDAYOFWEEK", updatable = false)
    private Integer unitDayOfWeek;

    @Column(name="WEEKDAY", updatable = false)
    private boolean weekday;

    @Column(name="UNITTIMESTAMP", updatable = false)
    private LocalDate unitTimestamp;

    @org.hibernate.annotations.CreationTimestamp
    @Column(name="T_CREATEDON",insertable = false, updatable = false)
    private Timestamp createdOn;

    public DateUnit(){

    }

    public DateUnit(LocalDate localDate) {
        this.unitDay = ChronoUnit.DAYS.between(EPOCH, localDate);;
        this.unitDayOfMonth = localDate.getDayOfMonth();
        this.unitDayOfYear = localDate.getDayOfYear();
        this.unitMonth = ChronoUnit.MONTHS.between(EPOCH, localDate);
        this.unitMonthOfYear = localDate.getMonthValue();
        this.unitYear = localDate.getYear();
        this.unitDayOfWeek = localDate.getDayOfWeek().getValue();
        this.weekday = (localDate.getDayOfWeek().getValue() == DayOfWeek.SUNDAY.getValue() ||
                localDate.getDayOfWeek().getValue() == DayOfWeek.SATURDAY.getValue()) ?
                false :
                true;
        this.unitTimestamp = localDate;
    }

    public Long getUnitDay() {
        return unitDay;
    }

    public Integer getUnitDayOfMonth() {
        return unitDayOfMonth;
    }

    public Integer getUnitDayOfYear() {
        return unitDayOfYear;
    }

    public Integer getUnitDayOfWeek() {
        return unitDayOfWeek;
    }

    public Long getUnitMonth() {
        return unitMonth;
    }

    public Integer getUnitMonthOfYear() {
        return unitMonthOfYear;
    }

    public LocalDate getUnitTimestamp() {
        return unitTimestamp;
    }

    public Integer getUnitYear() {
        return unitYear;
    }

    public boolean isWeekday() {
        return weekday;
    }

    public LocalDateTime getCreatedOn() {
        return createdOn.toLocalDateTime();
    }

    @Override
    public boolean equals(Object other) {
        if (other instanceof DateUnit) {
            DateUnit that = (DateUnit) other;
            return Objects.equal(getUnitDay(), that.getUnitDay());
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getUnitDay());
    }

    @Override
    public String toString() {
        return "DateUnit{" +
                "unitDay=" + unitDay +
                ", unitDayOfMonth=" + unitDayOfMonth +
                ", unitDayOfYear=" + unitDayOfYear +
                ", unitMonth=" + unitMonth +
                ", unitMonthOfYear=" + unitMonthOfYear +
                ", unitYear=" + unitYear +
                ", unitDayOfWeek=" + unitDayOfWeek +
                ", weekday=" + weekday +
                ", unitTimestamp=" + unitTimestamp +
                ", createdOn=" + createdOn +
                '}';
    }
}
