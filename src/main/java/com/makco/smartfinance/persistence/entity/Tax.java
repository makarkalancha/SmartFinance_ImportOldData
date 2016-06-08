package com.makco.smartfinance.persistence.entity;

import com.google.common.base.Objects;
import com.makco.smartfinance.constants.DataBaseConstants;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.sql.Timestamp;
import java.time.LocalDateTime;

/**
 * Created by mcalancea on 2016-06-08.
 */

@Entity
@Table(
        name = "TAX",
        uniqueConstraints =
        @UniqueConstraint(
                name = "IDX_UNQ_TX_NM",
                columnNames = {"NAME"}
        )
)
public class Tax implements Serializable {

    @Id
    @org.hibernate.annotations.GenericGenerator(
            name = "TAX_SEQUENCE_GENERATOR",
            strategy = "enhanced-sequence",
            parameters = {
                    @org.hibernate.annotations.Parameter(
                            name = "sequence_name",
                            value = "SEQ_TAX"
                    ),
                    @org.hibernate.annotations.Parameter(
                            name = "initial_value",
                            value = "1"
                    )
            }
    )
    @GeneratedValue(generator = "TAX_SEQUENCE_GENERATOR")
    @Column(name = "ID")
    private Long id;

    @Column(name = "NAME", unique = true)
    @NotNull
    @Size(
            min = 2,
            max = DataBaseConstants.TAX_NAME_MAX_LGTH,
            message = "Name is required, maximum " + DataBaseConstants.TAX_NAME_MAX_LGTH + " characters."
    )
    private String name;

    @Column(name = "DESCRIPTION")
    @Size(
            min = 0,
            max = DataBaseConstants.TAX_DESCRIPTION_MAX_LGTH,
            message = "Description length is " + DataBaseConstants.TAX_DESCRIPTION_MAX_LGTH + " characters."
    )
    private String description;

    @Column(name = "RATE")
    private BigDecimal rate;

    @Column(name = "FORMULA")
    @Size(
            min = 0,
            max = DataBaseConstants.TAX_FORMULA_MAX_LGTH,
            message = "Formula length is " + DataBaseConstants.TAX_FORMULA_MAX_LGTH + " characters."
    )
    private String formula;

    @Column(name = "STARTDATE")
    private Date startDate;

    @Column(name = "ENDDATE")
    private Date endDate;

    @org.hibernate.annotations.CreationTimestamp
    @Column(name = "T_CREATEDON", insertable = false, updatable = false)
    private Timestamp createdOn;

    @org.hibernate.annotations.UpdateTimestamp
    @Column(name = "T_UPDATEDON", insertable = false, updatable = false)
    private Timestamp updatedOn;

    public Tax() {

    }

    public Tax(String name, String description, BigDecimal rate, String formula, Date startDate, Date endDate) {
        this.name = name;
        this.description = description;
        this.rate = rate;
        this.formula = formula;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getId() {
        return id;
    }

    //no setId method

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public String getFormula() {
        return formula;
    }

    public void setFormula(String formula) {
        this.formula = formula;
    }

    public BigDecimal getRate() {
        return rate;
    }

    public void setRate(BigDecimal rate) {
        this.rate = rate;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    @Override
    public boolean equals(Object other) {
        if (other instanceof Tax) {
            Tax that = (Tax) other;
            return Objects.equal(getName(), that.getName());
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getName());
    }

    @Override
    public String toString() {
        return "Tax{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", rate=" + rate +
                ", formula='" + formula + '\'' +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                ", createdOn=" + createdOn +
                ", updatedOn=" + updatedOn +
                '}';
    }

    public LocalDateTime getCreatedOn() {
        return createdOn.toLocalDateTime();
    }

    public LocalDateTime getUpdatedOn() {
        return updatedOn.toLocalDateTime();
    }
}
