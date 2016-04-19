package com.makco.smartfinance.persistence.entity;

import java.io.Serializable;
import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * Created by mcalancea on 2016-04-12.
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
        min = 2,
        max = 3,
        message = "Currency code"
    )
    private String code;

    @Column(name="NAME")
    @Size(
            min = 0,
            max = 64,
            message = "Full name of the currency, length is 64 characters."
    )
    private String name;

    @Column(name="DESCRIPTION")
    @Size(
            min = 0,
            max = 128,
            message = "Description length is 128 characters."
    )
    private String description;

    @Temporal(TemporalType.TIMESTAMP)
    @org.hibernate.annotations.CreationTimestamp
    @Column(name="T_CREATEDON",insertable = false, updatable = false)
    protected LocalDateTime createdOn;

    @Temporal(TemporalType.TIMESTAMP)
    @org.hibernate.annotations.UpdateTimestamp
    @Column(name="T_UPDATEDON",insertable = false, updatable = false)
    protected LocalDateTime updatedOn;

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
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Currency that = (Currency) o;

        return id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
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
        return createdOn;
    }

    public LocalDateTime getUpdatedOn() {
        return updatedOn;
    }

}
