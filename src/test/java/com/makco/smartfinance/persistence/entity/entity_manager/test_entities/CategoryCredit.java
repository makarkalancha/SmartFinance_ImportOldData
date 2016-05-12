package com.makco.smartfinance.persistence.entity.entity_manager.test_entities;

import com.google.common.base.Objects;
import com.makco.smartfinance.constants.DataBaseConstants;
import com.makco.smartfinance.persistence.entity.Category;
import com.makco.smartfinance.persistence.entity.CategoryGroup;

import javax.persistence.*;

/**
 * Created by mcalancea on 2016-04-29.
 */
@Entity
@DiscriminatorValue(DataBaseConstants.CATEGORY_GROUP_TYPE.Values.CREDIT)
public class CategoryCredit extends Category implements Comparable<CategoryCredit>{
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CATEGORY_GROUP_ID",nullable = false)
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
    public String getCategoryGroupType() {
        return DataBaseConstants.CATEGORY_GROUP_TYPE.Values.CREDIT;
    }

    @Override
    public boolean equals(Object other) {
        if (other instanceof CategoryDebit) {
            CategoryDebit that = (CategoryDebit) other;
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
                ", createdOn='" + createdOn + '\'' +
                ", updatedOn='" + updatedOn + '\'' +
                '}';
    }

    //because CategoryDebit is used in CategoryGroupDebit SortedSet<CategoryDebit> and this collection puts only Comparable
    @Override
    public int compareTo(CategoryCredit that) {
        return this.getName().compareTo(that.getName());
    }
}
