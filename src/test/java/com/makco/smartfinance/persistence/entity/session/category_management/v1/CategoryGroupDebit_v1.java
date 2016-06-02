package com.makco.smartfinance.persistence.entity.session.category_management.v1;

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
//@DiscriminatorValue("D")
//@DiscriminatorValue(DataBaseConstants.CATEGORY_GROUP_TYPE_DEBIT)
@DiscriminatorValue(DataBaseConstants.CATEGORY_GROUP_TYPE.Values.DEBIT)
public class CategoryGroupDebit_v1 extends CategoryGroup_v1<CategoryDebit_v1> {
    /**
     *http://stackoverflow.com/questions/30838526/how-to-have-a-sorted-set-of-objects-based-on-a-specific-field
     *https://vladmihalcea.com/2015/03/05/a-beginners-guide-to-jpa-and-hibernate-cascade-types/
     *https://howtoprogramwithjava.com/hibernate-onetomany-bidirectional-relationship/
     * @OneToMany(mappedBy = "categoryGroupDebit", cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)//entityManager
     * - association collections @OneToMany and @ManyToMany are lazy-loaded by default  (p318-289)
    */

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
//http://stackoverflow.com/questions/4334197/discriminator-wrongclassexception-jpa-with-hibernate-backend
    @OneToMany(mappedBy = "categoryGroup", cascade = CascadeType.ALL, fetch = FetchType.LAZY)//session
//    @OneToMany(mappedBy = "categoryGroup", cascade = CascadeType.ALL, fetch = FetchType.EAGER)//do NOT USE, see above
    @javax.persistence.OrderBy("name")
    private SortedSet<CategoryDebit_v1> categories = new TreeSet<>();
//    private SortedSet<? extends Category> categories = new TreeSet<CategoryDebit>();
//    private List<? extends Category> categories = new ArrayList<CategoryDebit>();
//    private SortedMap<Long, CategoryDebit> categories = new TreeMap<>();

    public CategoryGroupDebit_v1() {
    }

    public CategoryGroupDebit_v1(String name, String description) {
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
    public Collection<CategoryDebit_v1> getCategories() {
        return this.categories;
    }

    @Override
    public void setCategories(Collection<CategoryDebit_v1> categories) {
        this.categories = new TreeSet<>(categories);
    }

    @Override
    public DataBaseConstants.CATEGORY_GROUP_TYPE getCategoryGroupType() {
        return DataBaseConstants.CATEGORY_GROUP_TYPE.DEBIT;
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
        if (other instanceof CategoryGroupDebit_v1) {
            CategoryGroupDebit_v1 that = (CategoryGroupDebit_v1) other;
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
                ", getCategoryGroupType='" + getCategoryGroupType() + '\'' +
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
