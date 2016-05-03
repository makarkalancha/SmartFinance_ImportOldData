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
//@DiscriminatorValue("D")
@DiscriminatorValue(CategoryGroup.CATEGORY_GROUP_TYPE_DEBIT)
public class CategoryGroupDebit extends CategoryGroup<CategoryDebit>{
    /**
     *http://stackoverflow.com/questions/30838526/how-to-have-a-sorted-set-of-objects-based-on-a-specific-field
     *https://vladmihalcea.com/2015/03/05/a-beginners-guide-to-jpa-and-hibernate-cascade-types/
     *https://howtoprogramwithjava.com/hibernate-onetomany-bidirectional-relationship/
     * @OneToMany(mappedBy = "categoryGroupDebit", cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)//entityManager
     * - association collections @OneToMany and @ManyToMany are lazy-loaded by default  (p318-289)
    */
    @OneToMany(mappedBy = "categoryGroup", cascade = CascadeType.ALL, fetch = FetchType.LAZY)//session
    @javax.persistence.OrderBy("name")
    private SortedSet<CategoryDebit> categories = new TreeSet<>();
//    private SortedSet<? extends Category> categories = new TreeSet<CategoryDebit>();
//    private List<? extends Category> categories = new ArrayList<CategoryDebit>();
//    private SortedMap<Long, CategoryDebit> categories = new TreeMap<>();

    public CategoryGroupDebit() {
    }

    public CategoryGroupDebit(String name, String description) {
        this.name = name;
        this.description = description;
    }

//    public SortedSet<CategoryDebit> getDebitCategories() {
//        return new TreeSet<>(categories);
//    }
//
//    public SortedSet<? extends Category> getDebitCategories() {
//        return new TreeSet<>(categories);
//    }
//
//    @Override
//    public void addCategories(List<CategoryDebit> categories) {
//        this.categories.addAll(categories);
//    }
//
//    @Override
//    public void addCategory(CategoryDebit category) {
//        this.categories.add(category);
//    }
//
//    @Override
//    public void removeCategory(CategoryDebit category) {
//        this.categories.remove(category);
//    }
//
//    @Override
//    public void removeCategories(List<CategoryDebit> categories) {
//        this.categories.remove(categories);
//    }

    @Override
    public Collection<CategoryDebit> getCategories() {
        return this.categories;
    }

    @Override
    public void setCategories(Collection<CategoryDebit> categories) {
        this.categories = new TreeSet<>(categories);
    }

    @Override
    public String getCategoryGroupType() {
        return CATEGORY_GROUP_TYPE_DEBIT;
    }

    //    @Override
////    public void addCategory(Category category) {
//////        this.categories.add((CategoryDebit) category);
////        this.categories.add(category);
////    }
//    public <T extends Category> void addCategory(T category) {
//        this.categories.add(category);
//    }

//    @Override
////    public void addCategories(List<Category> categories) {
////        this.categories.addAll((CategoryDebit) categories);
//    public void addCategories(List<? extends Category> categories) {
//
//    }


//    public void addDebitCategory(CategoryDebit debitCategory) {
//        this.categories.add(debitCategory);
//    }

//    public void addDebitCategories(List<CategoryDebit> categories) {
//        this.categories.addAll(categories);
//    }

    @Override
    public boolean equals(Object other) {
        if (other instanceof CategoryGroupDebit) {
            CategoryGroupDebit that = (CategoryGroupDebit) other;
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
        return "CategoryGroupDebit{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", createdOn='" + createdOn + '\'' +
                ", updatedOn='" + updatedOn + '\'' +

                '}';
    }

    @Override
    public String toStringFull() {
        return "CategoryGroupDebit{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", createdOn='" + createdOn + '\'' +
                ", updatedOn='" + updatedOn + '\'' +
                ", categories='" + categories + '\'' +
                '}';
    }
}
