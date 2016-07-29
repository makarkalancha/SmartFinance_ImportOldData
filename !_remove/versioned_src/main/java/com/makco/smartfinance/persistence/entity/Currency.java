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
import java.sql.Timestamp;
import java.time.LocalDateTime;

/**
 * Created by Makar Kalancha on 2016-04-12.
 */

@Entity
@Table(
        name="CURRENCY",
        uniqueConstraints =
        @UniqueConstraint(
                name="IDX_UNQ_CRRNC_CD",
                columnNames = {"CODE"}
        )
)
public class Currency implements Serializable{
    @Id
    @org.hibernate.annotations.GenericGenerator(
            name = "CURRENCY_SEQUENCE_GENERATOR",
            strategy = "enhanced-sequence",
            parameters = {
                    @org.hibernate.annotations.Parameter(
                            name = "sequence_name",
                            value = "SEQ_CURRENCY"
                    ),
                    @org.hibernate.annotations.Parameter(
                            name = "initial_value",
                            value = "1"
                    )
            }
    )
    @GeneratedValue(generator = "CURRENCY_SEQUENCE_GENERATOR")
    @Column(name="ID")
    private Long id;

    @Column(name="CODE")
    @NotNull
    @Size(
        min = DataBaseConstants.CUR_CODE_MAX_LGTH,
        max = DataBaseConstants.CUR_CODE_MAX_LGTH,
        message = "Currency code is required, maximum " + DataBaseConstants.CUR_CODE_MAX_LGTH + " characters."
    )
    private String code;

    @Column(name = "NAME")
    @Size(
            min = 0,
            max = DataBaseConstants.CUR_NAME_MAX_LGTH,
            message = "Full name of the currency, length is " + DataBaseConstants.CUR_NAME_MAX_LGTH + " characters."
    )
    private String name;

    @Column(name = "DESCRIPTION")
    @Size(
            min = 0,
            max = DataBaseConstants.CUR_DESCRIPTION_MAX_LGTH,
            message = "Description length is " + DataBaseConstants.CUR_DESCRIPTION_MAX_LGTH + " characters."
    )
    private String description;

    @org.hibernate.annotations.CreationTimestamp
    @Column(name="T_CREATEDON",insertable = false, updatable = false)
    protected Timestamp createdOn;

    @org.hibernate.annotations.UpdateTimestamp
    @Column(name="T_UPDATEDON",insertable = false, updatable = false)
    protected Timestamp updatedOn;

    public Currency(){

    }

    public Currency(String code, String name, String description){
        this.code = code;
        this.name = name;
        this.description = description;
    }

    public Long getId() {
        return id;
    }

    //no setId method

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public boolean equals(Object other) {
        if (other instanceof Currency) {
            Currency that = (Currency) other;
            return Objects.equal(getCode(), that.getCode());
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getCode());
    }

    @Override
    public String toString() {
        return "Currency{" +
                "id=" + id +
                ", code='" + code + '\'' +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", createdOn=" + createdOn +
                ", updatedOn=" + updatedOn +
                '}';
    }

    public LocalDateTime getCreatedOn() {
        if(createdOn == null){
            return null;
        }
        return createdOn.toLocalDateTime();
    }

    public LocalDateTime getUpdatedOn() {
        if(updatedOn == null){
            return null;
        }
        return updatedOn.toLocalDateTime();
    }

}
