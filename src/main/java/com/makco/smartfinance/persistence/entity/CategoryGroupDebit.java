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
@DiscriminatorValue(DataBaseConstants.CATEGORY_GROUP_TYPE.Values.DEBIT)
public class CategoryGroupDebit extends CategoryGroup<CategoryDebit>{
    @OneToMany(mappedBy = "categoryGroup", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @javax.persistence.OrderBy("name")
    private SortedSet<CategoryDebit> categories = new TreeSet<>();

    public CategoryGroupDebit() {
    }

    public CategoryGroupDebit(String name, String description) {
        this.name = name;
        this.description = description;
    }

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
        return DataBaseConstants.CATEGORY_GROUP_TYPE.Values.DEBIT;
    }

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
                ", getCategoryGroupType='" + getCategoryGroupType() + '\'' +
                ", createdOn='" + createdOn + '\'' +
                ", updatedOn='" + updatedOn + '\'' +

                '}';
    }
}
