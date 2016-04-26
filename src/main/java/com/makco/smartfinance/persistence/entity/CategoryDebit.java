package com.makco.smartfinance.persistence.entity;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

/**
 * Created by mcalancea on 2016-04-25.
 */
@Entity
@DiscriminatorValue("D")
public class CategoryDebit extends Category{

    //TODO because it's LAZY, check if you need to add ProgressIndicator when object is loaded
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CATEGORY_GROUP_ID",nullable = false)
//    private CategoryGroup categoryGroupDebit;
    private CategoryGroupDebit categoryGroupDebit;

    public CategoryDebit(){

    }

    public CategoryDebit(String description, String name) {
        this.description = description;
        this.name = name;
    }

    @Override
    public CategoryGroup getCategoryGroup() {
        return categoryGroupDebit;
    }

    @Override
    public void setCategoryGroup(CategoryGroup categoryGroup) {
        this.categoryGroupDebit = (CategoryGroupDebit) categoryGroup;
//        this.categoryGroupDebit = categoryGroup;
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (other == null) {
            return false;
        }

        if (!(other instanceof CategoryDebit)) {
            return false;
        }

        CategoryDebit that = (CategoryDebit) other;

        return getId().equals(that.getId());
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    @Override
    public String toString() {
        return "CategoryDebit{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", createdOn='" + createdOn + '\'' +
                ", updatedOn='" + updatedOn + '\'' +
                ", CategoryGroupDebit='" + categoryGroupDebit.toStringSimple() + '\'' +
                '}';
    }
}
