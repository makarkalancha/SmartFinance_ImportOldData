package com.makco.smartfinance.persistence.entity.entity_manager.test_entities;

import com.google.common.base.Objects;
import com.makco.smartfinance.persistence.entity.Category;
import com.makco.smartfinance.persistence.entity.CategoryGroup;

import javax.persistence.*;

/**
 * Created by mcalancea on 2016-04-29.
 */
@Entity
@DiscriminatorValue("E")
//because CategoryDebit is used in CategoryGroupDebit SortedSet<CategoryDebit> and this collection puts only Comparable
public class EagerCategoryDebit extends Category implements Comparable<EagerCategoryDebit>{
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "CATEGORY_GROUP_ID",nullable = false)
    private CategoryGroup categoryGroup;
//    private EagerCategoryGroupDebit categoryGroup;

    public EagerCategoryDebit(){

    }

    public EagerCategoryDebit(CategoryGroup categoryGroup, String name, String description) {
        this.categoryGroup = (EagerCategoryGroupDebit) categoryGroup;
        this.name = name;
        this.description = description;
    }

    @Override
    public CategoryGroup getCategoryGroup() {
        return categoryGroup;
    }

    @Override
    public void setCategoryGroup(CategoryGroup categoryGroup) {
        this.categoryGroup = (EagerCategoryGroupDebit) categoryGroup;
//        this.categoryGroup = categoryGroup;
    }

    @Override
    public String getCategoryGroupType() {
        return "E";
    }
    //when entity is transient id == null, so it's impossible to put it in Map or Set
    @Override
    public boolean equals(Object other) {
        if (other instanceof EagerCategoryDebit) {
            EagerCategoryDebit that = (EagerCategoryDebit) other;
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
        return "CategoryEagerDebit{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", getCategoryGroupType='" + getCategoryGroupType() + '\'' +
                ", categoryGroup='" + categoryGroup + '\'' +
                ", createdOn='" + createdOn + '\'' +
                ", updatedOn='" + updatedOn + '\'' +
                '}';
    }


    //because CategoryDebit is used in CategoryGroupDebit SortedSet<CategoryDebit> and this collection puts only Comparable
    @Override
    public int compareTo(EagerCategoryDebit that) {
        return this.getName().compareTo(that.getName());
    }
}
