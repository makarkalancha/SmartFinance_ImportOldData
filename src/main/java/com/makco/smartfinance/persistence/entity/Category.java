package com.makco.smartfinance.persistence.entity;

import java.sql.Timestamp;
import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

/**
 * Created by mcalancea on 2016-04-25.
 */
@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "CATEGORY_GROUP_TYPE", discriminatorType = DiscriminatorType.STRING, length=1)
@Table(
    name="CATEGORY",
    uniqueConstraints =
    @UniqueConstraint(
            name="IDX_UNQ_CTGR_CGIDNM",
            columnNames = {"CATEGORY_GROUP_ID","NAME"}
    )
)
public abstract class Category {
    @Id
    @org.hibernate.annotations.GenericGenerator(
            name = "CATEGORY_SEQUENCE_GENERATOR",
            strategy = "enhanced-sequence",
            parameters = {
                    @org.hibernate.annotations.Parameter(
                            name = "sequence_name",
                            value = "SEQ_CATEGORY"
                    ),
                    @org.hibernate.annotations.Parameter(
                            name = "initial_value",
                            value = "1"
                    )
            }
    )
    @GeneratedValue(generator = "CATEGORY_SEQUENCE_GENERATOR")
    @Column(name="ID")
    protected Long id;

    @org.hibernate.annotations.CreationTimestamp
    @Column(name="T_CREATEDON",insertable = false, updatable = false)
    protected Timestamp createdOn;

    @org.hibernate.annotations.UpdateTimestamp
    @Column(name="T_UPDATEDON",insertable = false, updatable = false)
    protected Timestamp updatedOn;

    public Category(){

    }

    public Long getId() {
        return id;
    }

    public Timestamp getCreatedOn() {
        return createdOn;
    }

    public Timestamp getUpdatedOn() {
        return updatedOn;
    }
}
