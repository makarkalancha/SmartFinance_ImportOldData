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
@DiscriminatorValue("D")
public class CategoryGroupDebit extends CategoryGroup<CategoryDebit>{
    //http://stackoverflow.com/questions/30838526/how-to-have-a-sorted-set-of-objects-based-on-a-specific-field
    @OneToMany(mappedBy = "categoryGroupDebit", cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    @javax.persistence.OrderBy("name")
    private SortedSet<CategoryDebit> debitCategories = new TreeSet<>();
//    private SortedSet<? extends Category> debitCategories = new TreeSet<CategoryDebit>(); 
//    private List<? extends Category> debitCategories = new ArrayList<CategoryDebit>();
//    private SortedMap<Long, CategoryDebit> debitCategories = new TreeMap<>();

    public CategoryGroupDebit() {
    }

    public CategoryGroupDebit(String name, String description) {
        this.name = name;
        this.description = description;
    }

//    public SortedSet<CategoryDebit> getDebitCategories() {
//        return new TreeSet<>(debitCategories);
//    }

    public SortedSet<? extends Category> getDebitCategories() {
        return new TreeSet<>(debitCategories);
    }

    @Override
    public void addCategories(List<CategoryDebit> categories) {
        this.debitCategories.addAll(categories);
    }

    @Override
    public void addCategory(CategoryDebit category) {
        this.debitCategories.add(category);
    }

    //    @Override
////    public void addCategory(Category category) {
//////        this.debitCategories.add((CategoryDebit) category);
////        this.debitCategories.add(category);
////    }
//    public <T extends Category> void addCategory(T category) {
//        this.debitCategories.add(category);
//    }

//    @Override
////    public void addCategories(List<Category> categories) {
////        this.debitCategories.addAll((CategoryDebit) categories);
//    public void addCategories(List<? extends Category> categories) {
//
//    }


//    public void addDebitCategory(CategoryDebit debitCategory) {
//        this.debitCategories.add(debitCategory);
//    }

//    public void addDebitCategories(List<CategoryDebit> debitCategories) {
//        this.debitCategories.addAll(debitCategories);
//    }

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (other == null) {
            return false;
        }

        if (!(other instanceof CategoryGroupDebit)) {
            return false;
        }

        CategoryGroupDebit that = (CategoryGroupDebit) other;

        return getId().equals(that.getId());
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    @Override
    public String toString() {
        return "CategoryGroupDebit{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", createdOn='" + createdOn + '\'' +
                ", updatedOn='" + updatedOn + '\'' +
                ", debitCategories='" + debitCategories + '\'' +
                '}';
    }

    @Override
    public String toStringSimple() {
        return "CategoryGroupDebit{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", createdOn='" + createdOn + '\'' +
                ", updatedOn='" + updatedOn + '\'' +
                '}';
    }
}