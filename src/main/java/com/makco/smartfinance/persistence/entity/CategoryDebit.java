package com.makco.smartfinance.persistence.entity;

import com.google.common.base.Objects;
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
//because CategoryDebit is used in CategoryGroupDebit SortedSet<CategoryDebit> and this collection puts only Comparable
public class CategoryDebit extends Category implements Comparable<CategoryDebit>{

    //TODO because it's LAZY, check if you need to add ProgressIndicator when object is loaded
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CATEGORY_GROUP_ID",nullable = false)
//    private CategoryGroup categoryGroupDebit;
    private CategoryGroupDebit categoryGroupDebit;

    public CategoryDebit(){

    }

    public CategoryDebit(CategoryGroup categoryGroup, String description, String name) {
        this.categoryGroupDebit = (CategoryGroupDebit) categoryGroup;
        this.name = name;
        this.description = description;
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

    //when entity is transient id == null, so it's impossible to put it in Map or Set
    @Override
    public boolean equals(Object other) {
//        if (this == other) {
//            return true;
//        }
//        if (other == null) {
//            return false;
//        }
//
//        if (!(other instanceof CategoryDebit)) {
//            return false;
//        }
//
//        CategoryDebit that = (CategoryDebit) other;
//
//        return getId().equals(that.getId());
        if (other instanceof CategoryDebit) {
            CategoryDebit that = (CategoryDebit) other;
            return Objects.equal(id, that.getId())
                    && Objects.equal(name, that.getName())
                    && Objects.equal(description, that.getDescription());
        }
        return false;
    }


    //when entity is transient id == null, so it's impossible to put it in Map or Set
    @Override
    public int hashCode() {
        return Objects.hashCode(id, name, description);
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


    //because CategoryDebit is used in CategoryGroupDebit SortedSet<CategoryDebit> and this collection puts only Comparable
    @Override
    public int compareTo(CategoryDebit that) {
        return this.name.compareTo(that.name);
    }
}
