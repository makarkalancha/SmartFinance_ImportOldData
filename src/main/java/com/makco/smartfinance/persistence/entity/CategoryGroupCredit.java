package com.makco.smartfinance.persistence.entity;

import com.google.common.base.Objects;
import com.makco.smartfinance.constants.DataBaseConstants;

import javax.persistence.CascadeType;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import java.util.Collection;
import java.util.SortedSet;
import java.util.TreeSet;

/**
 * Created by mcalancea on 2016-04-25.
 */
@Entity
//@DiscriminatorValue(DataBaseConstants.CATEGORY_GROUP_TYPE_CREDIT)
@DiscriminatorValue(DataBaseConstants.CATEGORY_GROUP_TYPE.Values.CREDIT)
public class CategoryGroupCredit extends CategoryGroup<CategoryCredit>{
    //http://stackoverflow.com/questions/30838526/how-to-have-a-sorted-set-of-objects-based-on-a-specific-field
    //http://stackoverflow.com/questions/4334197/discriminator-wrongclassexception-jpa-with-hibernate-backend
    //http://anshuiitk.blogspot.ca/2011/04/hibernate-wrongclassexception.html

    /**
     * EAGER doesn't work when you try to select all categories (and categoreis have manyToOne relationship
     * which by default is EAGER), category is load with category_group which loads EAGERly all its categories
     * so strange things happen: like hibernate loads categories of on specific category_group
     * [select categorygr0_.ID as ID2_1_0_, categorygr0_.T_CREATEDON as T_CREATE3_1_0_, categorygr0_.DESCRIPTION as DESCRIPT4_1_0_, categorygr0_.NAME as NAME5_1_0_, categorygr0_.T_UPDATEDON as T_UPDATE6_1_0_, categorygr0_.TYPE as TYPE1_1_0_, categories1_.CATEGORY_GROUP_ID as CATEGORY7_0_1_, categories1_.ID as ID2_0_1_, categories1_.ID as ID2_0_2_, categories1_.T_CREATEDON as T_CREATE3_0_2_, categories1_.DESCRIPTION as DESCRIPT4_0_2_, categories1_.NAME as NAME5_0_2_, categories1_.T_UPDATEDON as T_UPDATE6_0_2_, categories1_.CATEGORY_GROUP_ID as CATEGORY7_0_2_ from TEST.CATEGORY_GROUP categorygr0_ left outer join TEST.CATEGORY categories1_ on categorygr0_.ID=categories1_.CATEGORY_GROUP_ID and categories1_.CATEGORY_GROUP_TYPE='D' where categorygr0_.ID=? and categorygr0_.TYPE in ('C', 'D', 'E') order by categories1_.NAME]
     * and then hibernate tries to put it in SortedSet using Comparable.compateTo (based on getName method);
     * hibernate throws NullPointerException at
     * 	at com.makco.smartfinance.persistence.entity.CategoryDebit.compareTo(CategoryDebit.java:97)
     * 	at com.makco.smartfinance.persistence.entity.CategoryDebit.compareTo(CategoryDebit.java:17)
     * reason: hibernate returns object like this
     * CategoryDebit{id='7', name='null', description='null', getCategoryGroupType='D', CategoryGroupDebit='null', createdOn='null', updatedOn='null'}
     */
    @OneToMany(mappedBy = "categoryGroup", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
//    @OneToMany(mappedBy = "categoryGroup", cascade = CascadeType.ALL, fetch = FetchType.EAGER)//do NOT USE, see above
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
        return DataBaseConstants.CATEGORY_GROUP_TYPE.Values.CREDIT;
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
                ", getCategoryGroupType='" + getCategoryGroupType() + '\'' +
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
