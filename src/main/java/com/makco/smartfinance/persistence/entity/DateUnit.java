package com.makco.smartfinance.persistence.entity;

import com.makco.smartfinance.persistence.entity.converter.LocalDateAttributeConverter;

import javax.persistence.Column;
import javax.persistence.Convert;
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
    private final static LocalDate EPOCH = LocalDate.ofEpochDay(0);

    @Id
    @Column(name="UNITDATE", updatable = false)
    private Long unitDate;

    @Column(name="UNITDATEOFMONTH", updatable = false)
    private Integer unitDateOfMonth;

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
        this.unitDate = ChronoUnit.DAYS.between(EPOCH, localDate);;
        this.unitDateOfMonth = localDate.getDayOfMonth();
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

    public Long getUnitDate() {
        return unitDate;
    }

    public Integer getUnitDateOfMonth() {
        return unitDateOfMonth;
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
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        DateUnit that = (DateUnit) o;

        return unitDate.equals(that.unitDate);
    }

    @Override
    public int hashCode() {
        return unitDate.hashCode();
    }

    @Override
    public String toString() {
        return "DateUnit{" +
                "unitDate=" + unitDate +
                ", unitDateOfMonth=" + unitDateOfMonth +
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
