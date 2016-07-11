package com.makco.smartfinance.persistence.entity.session.category_management.v2;

import com.google.common.base.Objects;
import com.makco.smartfinance.constants.DataBaseConstants;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

/**
 * Created by Makar Kalancha on 2016-04-25.
 */
@Entity
//@DiscriminatorValue(DataBaseConstants.CREDIT)
@DiscriminatorValue(DataBaseConstants.CATEGORY_GROUP_TYPE.Values.CREDIT)
public class CategoryCredit_v2 extends Category_v2 implements Comparable<CategoryCredit_v2>{
//    @ManyToOne(fetch = FetchType.EAGER)
//    @JoinColumn(name = "CATEGORY_GROUP_ID", /*referencedColumnName = "ID",*/ nullable = false)
//    private CategoryGroup_v2 categoryGroup;
////    private CategoryGroupCredit categoryGroup;

    public CategoryCredit_v2(){

    }

    public CategoryCredit_v2(CategoryGroup_v2 categoryGroup, String name, String description) {
        this.categoryGroup = (CategoryGroupCredit_v2) categoryGroup;
        this.description = description;
        this.name = name;
    }

    @Override
    public CategoryGroup_v2 getCategoryGroup() {
        return categoryGroup;
    }

    @Override
    public void setCategoryGroup(CategoryGroup_v2 categoryGroup) {
        this.categoryGroup = categoryGroup;
    }

    @Override
    public String getCategoryGroupType() {
        return DataBaseConstants.CATEGORY_GROUP_TYPE.Values.CREDIT;
    }

    @Override
    public boolean equals(Object other) {
        if (other instanceof CategoryCredit_v2) {
            CategoryCredit_v2 that = (CategoryCredit_v2) other;
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
    public int compareTo(CategoryCredit_v2 that) {
        return this.getName().compareTo(that.getName());
    }
}
