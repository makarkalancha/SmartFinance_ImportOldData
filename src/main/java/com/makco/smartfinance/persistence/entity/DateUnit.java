package com.makco.smartfinance.persistence.entity;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Calendar;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.UniqueConstraint;

/**
 * Created by mcalancea on 2016-04-19.
 */
@Entity
@Table(
        name="DATEUNIT",
        uniqueConstraints = {
                @UniqueConstraint(name = "IDXUNITYEAR", columnNames = {"UNITYEAR"}),
                @UniqueConstraint(name = "IDXUNITMONTHOFYEAR", columnNames = {"UNITMONTHOFYEAR"}),
                @UniqueConstraint(name = "IDXUNITMONTH", columnNames = {"UNITMONTH"}),
                @UniqueConstraint(name = "IDXUNITDATEOFMONTH", columnNames = {"UNITDATEOFMONTH"}),
                @UniqueConstraint(name = "IDXUNITDAYOFWEEK", columnNames = {"UNITDAYOFWEEK"}),
                @UniqueConstraint(name = "IDXWEEKDAY", columnNames = {"WEEKDAY"}),
                @UniqueConstraint(name = "IDXUNITTIMESTAMP", columnNames = {"UNITTIMESTAMP"}),
        }
)
public class DateUnit {
    @Column(name="UNITDATE")
    private Long unitDate;

    @Column(name="UNITDATEOFMONTH")
    private Long unitDateOfMonth;

    @Column(name="UNITMONTH")
    private Long unitMonth;

    @Column(name="UNITMONTHOFYEAR")
    private Integer unitMonthOfYear;

    @Column(name="UNITYEAR")
    private Integer unitYear;

    @Column(name="UNITDAYOFWEEK")
    private Integer unitDayOfWeek;

    @Column(name="WEEKDAY")
    private boolean weekday;

    @Column(name="UNITTIMESTAMP")
    private Calendar unitTimestamp;

    @Temporal(TemporalType.TIMESTAMP)
    @org.hibernate.annotations.CreationTimestamp
    @Column(name="T_CREATEDON",insertable = false, updatable = false)
    private Calendar createdOn;

    public DateUnit(){

    }

    public DateUnit(Long unitDate, Long unitDateOfMonth, Long unitMonth, Integer unitMonthOfYear, Integer unitYear, Integer unitDayOfWeek,
            boolean weekday, Calendar unitTimestamp, Calendar createdOn) {
        this.unitDate = unitDate;
        this.unitDateOfMonth = unitDateOfMonth;
        this.unitMonth = unitMonth;
        this.unitMonthOfYear = unitMonthOfYear;
        this.unitYear = unitYear;
        this.unitDayOfWeek = unitDayOfWeek;
        this.weekday = weekday;
        this.unitTimestamp = unitTimestamp;
        this.createdOn = createdOn;
    }

    public Long getUnitDate() {
        return unitDate;
    }

    public Long getUnitDateOfMonth() {
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

    public Calendar getUnitTimestamp() {
        return unitTimestamp;
    }

    public Integer getUnitYear() {
        return unitYear;
    }

    public boolean isWeekday() {
        return weekday;
    }

    public LocalDateTime getCreatedOn() {
        return createdOn.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
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
