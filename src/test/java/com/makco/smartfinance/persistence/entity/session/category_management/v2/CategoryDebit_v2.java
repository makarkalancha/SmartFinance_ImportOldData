package com.makco.smartfinance.persistence.entity.session.category_management.v2;

import com.google.common.base.Objects;
import com.makco.smartfinance.constants.DataBaseConstants;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

/**
 * Created by mcalancea on 2016-04-25.
 */
@Entity
//@DiscriminatorValue(DataBaseConstants.CATEGORY_GROUP_TYPE_DEBIT)
@DiscriminatorValue(DataBaseConstants.CATEGORY_GROUP_TYPE.Values.DEBIT)
//because CategoryDebit is used in CategoryGroupDebit SortedSet<CategoryDebit> and this collection puts only Comparable
public class CategoryDebit_v2 extends Category_v2 implements Comparable<CategoryDebit_v2>{
    private final static Logger LOG = LogManager.getLogger(CategoryDebit_v2.class);
//    /**
//     * @ManyToOne is eager loaded by default (p318-289)
//     */
//    @ManyToOne(fetch = FetchType.EAGER)
//    @JoinColumn(name = "CATEGORY_GROUP_ID", /*referencedColumnName = "ID",*/ nullable = false)
//    private CategoryGroup_v2 categoryGroup;
////    private CategoryGroupDebit categoryGroup;

    public CategoryDebit_v2(){

    }

    public CategoryDebit_v2(CategoryGroup_v2 categoryGroup, String name, String description) {
        this.categoryGroup = (CategoryGroupDebit_v2) categoryGroup;
        this.name = name;
        this.description = description;
    }

    @Override
    public CategoryGroup_v2 getCategoryGroup() {
        return categoryGroup;
    }

    @Override
    public void setCategoryGroup(CategoryGroup_v2 categoryGroup) {
        this.categoryGroup = (CategoryGroupDebit_v2) categoryGroup;
//        this.categoryGroup = categoryGroup;
    }

    @Override
    public String getCategoryGroupType() {
        return DataBaseConstants.CATEGORY_GROUP_TYPE.Values.DEBIT;
    }

    //when entity is transient id == null, so it's impossible to put it in Map or Set
    //redundant check getCategoryGroupType(), because there's already check for instance of CategoryDebit
    @Override
    public boolean equals(Object other) {
        if (other instanceof CategoryDebit_v2) {
            CategoryDebit_v2 that = (CategoryDebit_v2) other;
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
        return "CategoryDebit{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", getCategoryGroupType='" + getCategoryGroupType() + '\'' +
                ", CategoryGroupDebit='" + categoryGroup + '\'' +
                ", createdOn='" + createdOn + '\'' +
                ", updatedOn='" + updatedOn + '\'' +
                '}';
    }


    //because CategoryDebit is used in CategoryGroupDebit SortedSet<CategoryDebit> and this collection puts only Comparable
    @Override
    public int compareTo(CategoryDebit_v2 that) {
        LOG.debug("this:" + this);
        LOG.debug("this.getName():" + this.getName());
        LOG.debug("that:" + that);
        LOG.debug("that.getName():" + that.getName());
        return this.getName().compareTo(that.getName());
    }
}
