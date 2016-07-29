package com.makco.smartfinance.persistence.entity;

import com.google.common.base.Objects;
import com.makco.smartfinance.constants.DataBaseConstants;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

/**
 * Created by Makar Kalancha on 2016-04-25.
 * v1 (see tests)
 * because CategoryDebit is used in CategoryGroupCredit SortedSet<CategoryCredit> and this collection puts only Comparable
 */
@Entity
@DiscriminatorValue(DataBaseConstants.CATEGORY_GROUP_TYPE.Values.CREDIT)
public class CategoryCredit extends Category implements Comparable<CategoryCredit>{
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "CATEGORY_GROUP_ID", referencedColumnName = "ID", nullable = false)
    private CategoryGroup categoryGroup;

    public CategoryCredit(){

    }

    public CategoryCredit(CategoryGroup categoryGroup, String name, String description) {
        this.categoryGroup = (CategoryGroupCredit) categoryGroup;
        this.description = description;
        this.name = name;
    }

    @Override
    public CategoryGroup getCategoryGroup() {
        return categoryGroup;
    }

    @Override
    public void setCategoryGroup(CategoryGroup categoryGroup) {
        this.categoryGroup = categoryGroup;
    }

    @Override
    public DataBaseConstants.CATEGORY_GROUP_TYPE getCategoryGroupType() {
        return DataBaseConstants.CATEGORY_GROUP_TYPE.CREDIT;
    }

    /**
     * when entity is transient id == null, so it's impossible to put it in Map or Set
     * redundant check getCategoryGroupType(), because there's already check for instance of CategoryDebit
     */
    @Override
    public boolean equals(Object other) {
        if (other instanceof CategoryCredit) {
            CategoryCredit that = (CategoryCredit) other;
            return Objects.equal(getCategoryGroupType(), that.getCategoryGroupType())
                    && Objects.equal(getName(), that.getName());
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getCategoryGroupType(), getName());
    }

    @Override
    public String toString() {
        return "CategoryCredit{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", getCategoryGroupType='" + getCategoryGroupType() + '\'' +
                ", categoryGroup='" + categoryGroup + '\'' +
                ", createdOn='" + createdOn + '\'' +
                ", updatedOn='" + updatedOn + '\'' +
                '}';
    }

    @Override
    public int compareTo(CategoryCredit that) {
        return this.getName().compareTo(that.getName());
    }
}
