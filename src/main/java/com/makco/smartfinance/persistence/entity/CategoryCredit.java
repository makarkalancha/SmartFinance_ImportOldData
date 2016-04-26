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
@DiscriminatorValue("C")
public class CategoryCredit extends Category{
    //TODO because it's LAZY, check if you need to add ProgressIndicator when object is loaded
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CATEGORY_GROUP_ID",nullable = false)
    private CategoryGroupCredit categoryGroupCredit;

    public CategoryCredit(){

    }

    public CategoryCredit(String description, String name) {
        this.description = description;
        this.name = name;
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (other == null) {
            return false;
        }

        if (!(other instanceof CategoryCredit)) {
            return false;
        }

        CategoryCredit that = (CategoryCredit) other;

        return getId().equals(that.getId());
    }

    @Override
    public int hashCode() {
        return id.hashCode();
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
}
