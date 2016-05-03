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
    /**
     * @ManyToOne is eager loaded by default (p318-289)
     */
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "CATEGORY_GROUP_ID",nullable = false)
    private CategoryGroup categoryGroup;
//    private CategoryGroupDebit categoryGroup;

    public CategoryDebit(){

    }

    public CategoryDebit(CategoryGroup categoryGroup, String name, String description) {
        this.categoryGroup = (CategoryGroupDebit) categoryGroup;
        this.name = name;
        this.description = description;
    }

    @Override
    public CategoryGroup getCategoryGroup() {
        return categoryGroup;
    }

    @Override
    public void setCategoryGroup(CategoryGroup categoryGroup) {
        this.categoryGroup = (CategoryGroupDebit) categoryGroup;
//        this.categoryGroup = categoryGroup;
    }

    @Override
    public String getCategoryGroupType() {
        return "D";
    }

    //when entity is transient id == null, so it's impossible to put it in Map or Set
    //redundant check getCategoryGroupType(), because there's already check for instance of CategoryDebit
    @Override
    public boolean equals(Object other) {
        if (other instanceof CategoryDebit) {
            CategoryDebit that = (CategoryDebit) other;
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
                ", createdOn='" + createdOn + '\'' +
                ", updatedOn='" + updatedOn + '\'' +
                ", CategoryGroupDebit='" + categoryGroup + '\'' +
                '}';
    }


    //because CategoryDebit is used in CategoryGroupDebit SortedSet<CategoryDebit> and this collection puts only Comparable
    @Override
    public int compareTo(CategoryDebit that) {
        return this.getName().compareTo(that.getName());
    }
}
