package com.makco.smartfinance.persistence.entity.entity_manager.test_entities;

import com.makco.smartfinance.constants.DataBaseConstants;
import com.makco.smartfinance.persistence.entity.Category;
import com.makco.smartfinance.persistence.entity.CategoryGroup;

import javax.persistence.CascadeType;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import java.util.Collection;
import java.util.SortedSet;
import java.util.TreeSet;

/**
 * Created by Makar Kalancha on 2016-04-29.
 */
@Entity
@DiscriminatorValue("E")
public class EagerCategoryGroupDebit extends CategoryGroup<EagerCategoryDebit> {
//    @OneToMany(mappedBy = "categoryGroup", cascade = CascadeType.PERSIST, fetch = FetchType.EAGER)
    /**
     * test_44_3_2_seleteAllCategoryGroupsWithCategories_withNativeQuery_emptyCGAndCatTables
     * fails when remove EagerCategoryGroupDebit with categories FK constraint violation
     * so it's not only that session is using CascadeType.ALL
     */
    @OneToMany(mappedBy = "categoryGroup", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @OrderBy("name")
    private SortedSet<EagerCategoryDebit> debitCategories = new TreeSet<>();

    public EagerCategoryGroupDebit() {
    }

    public EagerCategoryGroupDebit(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public SortedSet<? extends Category> getDebitCategories() {
        return new TreeSet<>(debitCategories);
    }

//    @Override
//    public void addCategories(List<EagerCategoryDebit> categories) {
//        this.debitCategories.addAll(categories);
//    }
//
//    @Override
//    public void addCategory(EagerCategoryDebit category) {
//        this.debitCategories.add(category);
//    }
//
//    @Override
//    public void removeCategory(EagerCategoryDebit category) {
//        this.debitCategories.remove(category);
//    }
//
//    @Override
//    public void removeCategories(List<EagerCategoryDebit> categories) {
//        this.debitCategories.remove(categories);
//    }


    @Override
    public Collection<EagerCategoryDebit> getCategories() {
        return this.debitCategories;
    }

    @Override
    public void setCategories(Collection<EagerCategoryDebit> categories) {
        this.debitCategories = new TreeSet<>(categories);
    }

    @Override
    public DataBaseConstants.CATEGORY_GROUP_TYPE getCategoryGroupType() {
//        return "E";
        return null;
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (other == null) {
            return false;
        }

        if (!(other instanceof EagerCategoryGroupDebit)) {
            return false;
        }

        EagerCategoryGroupDebit that = (EagerCategoryGroupDebit) other;

        return getId().equals(that.getId());
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    @Override
    public String toString() {
        return "CategoryGroupEagerDebit{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", getCategoryGroupType='" + getCategoryGroupType() + '\'' +
                ", createdOn='" + createdOn + '\'' +
                ", updatedOn='" + updatedOn + '\'' +
                '}';
    }

    public String toStringFull() {
        return "CategoryGroupDebit{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", createdOn='" + createdOn + '\'' +
                ", updatedOn='" + updatedOn + '\'' +
                '}';
    }
}
