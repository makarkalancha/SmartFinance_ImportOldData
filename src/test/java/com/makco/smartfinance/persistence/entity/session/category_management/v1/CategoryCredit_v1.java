package com.makco.smartfinance.persistence.entity.session.category_management.v1;

import com.google.common.base.Objects;
import com.makco.smartfinance.constants.DataBaseConstants;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

/**
 * Created by Makar Kalancha on 2016-04-25.
 * because CategoryDebit is used in CategoryGroupCredit SortedSet<CategoryCredit> and this collection puts only Comparable
 */
@Entity
//@DiscriminatorValue(DataBaseConstants.CREDIT)
@DiscriminatorValue(DataBaseConstants.CATEGORY_GROUP_TYPE.Values.CREDIT)
public class CategoryCredit_v1 extends Category_v1 implements Comparable<CategoryCredit_v1>{
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "CATEGORY_GROUP_ID", referencedColumnName = "ID", nullable = false)
    private CategoryGroup_v1 categoryGroup;
//    private CategoryGroupCredit categoryGroup;

    public CategoryCredit_v1(){

    }

    public CategoryCredit_v1(CategoryGroup_v1 categoryGroup, String name, String description) {
        this.categoryGroup = (CategoryGroupCredit_v1) categoryGroup;
        this.description = description;
        this.name = name;
    }

    @Override
    public CategoryGroup_v1 getCategoryGroup() {
        return categoryGroup;
    }

    @Override
    public void setCategoryGroup(CategoryGroup_v1 categoryGroup) {
        this.categoryGroup = categoryGroup;
    }

    @Override
    public String getCategoryGroupType() {
        return DataBaseConstants.CATEGORY_GROUP_TYPE.Values.CREDIT;
    }

    /**
     * when entity is transient id == null, so it's impossible to put it in Map or Set
     * redundant check getCategoryGroupType(), because there's already check for instance of CategoryDebit
     */
    @Override
    public boolean equals(Object other) {
        if (other instanceof CategoryCredit_v1) {
            CategoryCredit_v1 that = (CategoryCredit_v1) other;
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

    //because CategoryDebit is used in CategoryGroupDebit SortedSet<CategoryDebit> and this collection puts only Comparable
    @Override
    public int compareTo(CategoryCredit_v1 that) {
        return this.getName().compareTo(that.getName());
    }
}
