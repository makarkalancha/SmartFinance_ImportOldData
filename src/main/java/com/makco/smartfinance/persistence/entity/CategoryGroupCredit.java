package com.makco.smartfinance.persistence.entity;

import java.util.List;
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
@DiscriminatorValue("C")
public class CategoryGroupCredit extends CategoryGroup<CategoryCredit>{
    //http://stackoverflow.com/questions/30838526/how-to-have-a-sorted-set-of-objects-based-on-a-specific-field
    @OneToMany(mappedBy = "categoryGroupCredit", cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    @javax.persistence.OrderBy("name")
    private SortedSet<CategoryCredit> creditCategories = new TreeSet<>();
//    private SortedMap<Long, CategoryCredit> creditCategories = new TreeMap<>();

    public CategoryGroupCredit() {
    }

    public CategoryGroupCredit(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public SortedSet<CategoryCredit> getDebitCategories() {
        return new TreeSet<>(creditCategories);
    }

    @Override
    public void addCategories(List<CategoryCredit> categories) {
        this.creditCategories.addAll(categories);
    }

    @Override
    public void addCategory(CategoryCredit category) {
        this.creditCategories.add(category);
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (other == null) {
            return false;
        }

        if (!(other instanceof CategoryGroupCredit)) {
            return false;
        }

        CategoryGroupCredit that = (CategoryGroupCredit) other;

        return getId().equals(that.getId());
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    @Override
    public String toString() {
        return "CategoryGroupCredit{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", createdOn='" + createdOn + '\'' +
                ", updatedOn='" + updatedOn + '\'' +
                ", creditCategories='" + creditCategories + '\'' +
                '}';
    }

    @Override
    public String toStringSimple() {
        return "CategoryGroupCredit{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", createdOn='" + createdOn + '\'' +
                ", updatedOn='" + updatedOn + '\'' +
                '}';
    }
}
