package com.makco.smartfinance.persistence.entity;

import com.makco.smartfinance.constants.DataBaseConstants;
import org.hibernate.annotations.DiscriminatorOptions;

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
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.sql.Timestamp;
import java.time.LocalDateTime;

/**
 * Created by Makar Kalancha on 2016-04-25.
 * v1 (see tests)
 */
@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorOptions(force = true)
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

    @Column(name = "NAME", unique = true)
    @NotNull
    @Size(
            min = 2,
            max = DataBaseConstants.CAT_NAME_MAX_LGTH,
            message = "Name is required, maximum " + DataBaseConstants.CAT_NAME_MAX_LGTH + " characters."
    )
    protected String name;

    @Column(name = "DESCRIPTION")
    @Size(
            min = 0,
            max = DataBaseConstants.CAT_DESCRIPTION_MAX_LGTH,
            message = "Description length is " + DataBaseConstants.CAT_DESCRIPTION_MAX_LGTH + " characters."
    )
    protected String description;

    @org.hibernate.annotations.CreationTimestamp
    @Column(name="T_CREATEDON",insertable = false, updatable = false)
    protected Timestamp createdOn;

    @org.hibernate.annotations.UpdateTimestamp
    @Column(name="T_UPDATEDON",insertable = false, updatable = false)
    protected Timestamp updatedOn;

    public Category(){

    }

//    public Category(CategoryGroup categoryGroup, String name, String description){
//        this.categoryGroup = categoryGroup;
//    }

    public Long getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public abstract CategoryGroup getCategoryGroup();

    public abstract void setCategoryGroup(CategoryGroup categoryGroup);

    public abstract DataBaseConstants.CATEGORY_GROUP_TYPE getCategoryGroupType();

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
