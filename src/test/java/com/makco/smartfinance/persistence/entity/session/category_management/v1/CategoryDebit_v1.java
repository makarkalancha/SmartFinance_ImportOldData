package com.makco.smartfinance.persistence.entity.session.category_management.v1;

import com.google.common.base.Objects;
import com.makco.smartfinance.constants.DataBaseConstants;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

/**
 * Created by Makar Kalancha on 2016-04-25.
 * because CategoryDebit is used in CategoryGroupDebit SortedSet<CategoryDebit> and this collection puts only Comparable
 */
@Entity
@DiscriminatorValue(DataBaseConstants.CATEGORY_GROUP_TYPE.Values.DEBIT)
public class CategoryDebit_v1 extends Category_v1 implements Comparable<CategoryDebit_v1>{
    private final static Logger LOG = LogManager.getLogger(CategoryDebit_v1.class);
    /**
     * @ManyToOne is eager loaded by default (p318-289)
     */
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "CATEGORY_GROUP_ID", referencedColumnName = "ID", nullable = false)
    private CategoryGroup_v1 categoryGroup;
//    private CategoryGroupDebit categoryGroup;

    public CategoryDebit_v1(){

    }

    public CategoryDebit_v1(CategoryGroup_v1 categoryGroup, String name, String description) {
        this.categoryGroup = (CategoryGroupDebit_v1) categoryGroup;
        this.name = name;
        this.description = description;
    }

    @Override
    public CategoryGroup_v1 getCategoryGroup() {
        return categoryGroup;
    }

    @Override
    public void setCategoryGroup(CategoryGroup_v1 categoryGroup) {
        this.categoryGroup = (CategoryGroupDebit_v1) categoryGroup;
//        this.categoryGroup = categoryGroup;
    }

    @Override
    public String getCategoryGroupType() {
        return DataBaseConstants.CATEGORY_GROUP_TYPE.Values.DEBIT;
    }

    /**
     * when entity is transient id == null, so it's impossible to put it in Map or Set
     * redundant check getCategoryGroupType(), because there's already check for instance of CategoryDebit
     */
    @Override
    public boolean equals(Object other) {
        if (other instanceof CategoryDebit_v1) {
            CategoryDebit_v1 that = (CategoryDebit_v1) other;
            return Objects.equal(getCategoryGroupType(), that.getCategoryGroupType())
                    && Objects.equal(getName(), that.getName());
        }
        return false;
    }


    //when entity is transient id == null, so it's impossible to put it in Map or Set
    @Override
    public int hashCode() {
        return Objects.hashCode(getCategoryGroupType(), getName());
    }

    @Override
    public String toString() {
        return "CategoryDebit{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", getCategoryGroupType='" + getCategoryGroupType() + '\'' +
                ", CategoryGroupDebit='" + categoryGroup + '\'' +
                ", createdOn='" + createdOn + '\'' +
                ", updatedOn='" + updatedOn + '\'' +
                '}';
    }


    //because CategoryDebit is used in CategoryGroupDebit SortedSet<CategoryDebit> and this collection puts only Comparable
    @Override
    public int compareTo(CategoryDebit_v1 that) {
        LOG.debug("this:" + this);
        LOG.debug("this.getName():" + this.getName());
        LOG.debug("that:" + that);
        LOG.debug("that.getName():" + that.getName());
        return this.getName().compareTo(that.getName());
    }
}
