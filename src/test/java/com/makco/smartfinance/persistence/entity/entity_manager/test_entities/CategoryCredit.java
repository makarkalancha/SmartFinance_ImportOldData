package com.makco.smartfinance.persistence.entity.entity_manager.test_entities;

import com.google.common.base.Objects;
import com.makco.smartfinance.persistence.entity.Category;
import com.makco.smartfinance.persistence.entity.CategoryGroup;
import com.makco.smartfinance.persistence.entity.entity_manager.test_entities.CategoryGroupCredit;

import javax.persistence.*;

/**
 * Created by mcalancea on 2016-04-29.
 */
@Entity
@DiscriminatorValue("C")
public class CategoryCredit extends Category implements Comparable<CategoryCredit>{
    //TODO because it's LAZY, check if you need to add ProgressIndicator when object is loaded
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CATEGORY_GROUP_ID",nullable = false)
    private CategoryGroup categoryGroupCredit;

    public CategoryCredit(){

    }

    public CategoryCredit(CategoryGroup categoryGroup, String name, String description) {
        this.categoryGroupCredit = (CategoryGroupCredit) categoryGroup;
        this.description = description;
        this.name = name;
    }

    @Override
    public CategoryGroup getCategoryGroup() {
        return categoryGroupCredit;
    }

    @Override
    public void setCategoryGroup(CategoryGroup categoryGroup) {
        this.categoryGroupCredit = categoryGroup;
    }

    @Override
    public String getCategoryGroupType() {
        return "С";
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
        return this.name.compareTo(that.name);
    }
}
