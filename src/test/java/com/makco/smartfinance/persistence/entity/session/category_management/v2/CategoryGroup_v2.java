package com.makco.smartfinance.persistence.entity.session.category_management.v2;

import com.makco.smartfinance.constants.DataBaseConstants;
import org.hibernate.annotations.DiscriminatorOptions;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.SortedSet;
import java.util.TreeSet;

/**
 * Created by Makar Kalancha on 2016-04-25.
 */
//@MappedSuperclass
@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
////http://stackoverflow.com/questions/12199874/about-the-use-of-forcediscriminator-discriminatoroptionsforce-true
@DiscriminatorOptions(force = true)
@DiscriminatorColumn(name = "TYPE", discriminatorType= DiscriminatorType.STRING, length=1)
@Table(
    name="CATEGORY_GROUP",
    uniqueConstraints =
    @UniqueConstraint(
            name="IDX_UNQ_CTGRGRP_TPNM",
            columnNames = {"TYPE","NAME"}
    )
)
public abstract class CategoryGroup_v2/*<T extends Category_v2>*/{
    @Id
    @org.hibernate.annotations.GenericGenerator(
            name = "CATEGORY_GROUP_SEQUENCE_GENERATOR",
            strategy = "enhanced-sequence",
            parameters = {
                    @org.hibernate.annotations.Parameter(
                            name = "sequence_name",
                            value = "SEQ_CATEGORY_GROUP"
                    ),
                    @org.hibernate.annotations.Parameter(
                            name = "initial_value",
                            value = "1"
                    )
            }
    )
    @GeneratedValue(generator = "CATEGORY_GROUP_SEQUENCE_GENERATOR")
    @Column(name="ID")
    protected Long id;

    @Column(name = "NAME", unique = true)
    @NotNull
    @Size(
            min = 2,
            max = DataBaseConstants.CG_NAME_MAX_LGTH,
            message = "Name is required, maximum " + DataBaseConstants.CG_NAME_MAX_LGTH + " characters."
    )
    protected String name;

    @Column(name = "DESCRIPTION")
    @Size(
            min = 0,
            max = DataBaseConstants.CG_DESCRIPTION_MAX_LGTH,
            message = "Description length is " + DataBaseConstants.CG_DESCRIPTION_MAX_LGTH + " characters."
    )
    protected String description;

    @org.hibernate.annotations.CreationTimestamp
    @Column(name="T_CREATEDON",insertable = false, updatable = false)
    protected Timestamp createdOn;

    @org.hibernate.annotations.UpdateTimestamp
    @Column(name="T_UPDATEDON",insertable = false, updatable = false)
    protected Timestamp updatedOn;

    /**
     * Property com.makco.smartfinance.persistence.entity.CategoryGroup.categories has an unbound type and no explicit target entity.
     * Resolve this Generic usage issue or set an explicit target attribute (eg @OneToMany(target=) or use an explicit @Type
     */
    @OneToMany(mappedBy = "categoryGroup", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @javax.persistence.OrderBy("name")
    protected SortedSet<Category_v2> categories = new TreeSet<>();

    public CategoryGroup_v2(){

    }

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

//    public abstract void addCategory(T category);
//
//    public abstract void addCategories(List<T> categories);
//
//    public abstract void removeCategory(T category);
//
//    public abstract void removeCategories(List<T> categories);

    public abstract String getCategoryGroupType();

    public Collection<Category_v2> getCategories() {
        return this.categories;
    }

    public void setCategories(Collection<Category_v2> categories) {
        this.categories = new TreeSet<>(categories);
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

    public abstract String toStringFull();
}
