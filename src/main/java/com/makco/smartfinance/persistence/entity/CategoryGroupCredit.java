package com.makco.smartfinance.persistence.entity;

import com.google.common.base.Objects;

import java.util.Collection;
import java.util.SortedSet;
import java.util.TreeSet;
import javax.persistence.CascadeType;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;

/**
 * Created by mcalancea on 2016-04-25.
 */
@Entity
@DiscriminatorValue(CategoryGroup.CATEGORY_GROUP_TYPE_CREDIT)
public class CategoryGroupCredit extends CategoryGroup<CategoryCredit>{
    //http://stackoverflow.com/questions/30838526/how-to-have-a-sorted-set-of-objects-based-on-a-specific-field
    @OneToMany(mappedBy = "categoryGroup", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @javax.persistence.OrderBy("name")
    private SortedSet<CategoryCredit> categories = new TreeSet<>();
//    private SortedMap<Long, CategoryCredit> categories = new TreeMap<>();

    public CategoryGroupCredit() {
    }

    public CategoryGroupCredit(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public SortedSet<CategoryCredit> getDebitCategories() {
        return new TreeSet<>(categories);
    }

//    @Override
//    public void addCategories(List<CategoryCredit> categories) {
//        this.categories.addAll(categories);
//    }
//
//    @Override
//    public void addCategory(CategoryCredit category) {
//        this.categories.add(category);
//    }
//
//    @Override
//    public void removeCategory(CategoryCredit category) {
//        this.categories.remove(category);
//    }
//
//    @Override
//    public void removeCategories(List<CategoryCredit> categories) {
//        this.categories.removeAll(categories);
//    }

    @Override
    public Collection<CategoryCredit> getCategories() {
        return this.categories;
    }

    @Override
    public void setCategories(Collection<CategoryCredit> categories) {
        this.categories = new TreeSet<>(categories);
    }

    @Override
    public String getCategoryGroupType() {
        return CATEGORY_GROUP_TYPE_CREDIT;
    }

    @Override
    public boolean equals(Object other) {
        if (other instanceof CategoryGroupCredit) {
            CategoryGroupCredit that = (CategoryGroupCredit) other;
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
        return "CategoryGroupCredit{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", createdOn='" + createdOn + '\'' +
                ", updatedOn='" + updatedOn + '\'' +
                '}';
    }

    @Override
    public String toStringFull() {
        return "CategoryGroupCredit{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", createdOn='" + createdOn + '\'' +
                ", updatedOn='" + updatedOn + '\'' +
                ", categories='" + categories + '\'' +
                '}';
    }
}
