package com.makco.smartfinance.user_interface.decorator;

import com.google.common.base.Objects;
import com.makco.smartfinance.constants.DataBaseConstants;
import com.makco.smartfinance.persistence.entity.Category;
import com.makco.smartfinance.persistence.entity.CategoryGroup;

import java.util.Calendar;

/**
 * Created by mcalancea on 2016-06-03.
 */
public class CategoryManagementDecoratorCategoryGroup implements CategoryManagmentDecorator {
    private String name;
    private String description;
    private DataBaseConstants.CATEGORY_GROUP_TYPE type;

    public CategoryManagementDecoratorCategoryGroup(CategoryGroup categoryGroup){
        this.name = categoryGroup.getName();
        this.description = categoryGroup.getDescription();
        this.type = categoryGroup.getCategoryGroupType();
    }

    @Override
    public Long getId() {
        return null;
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getCategoryGroupType() {
        return type.getDiscriminator();
    }

    @Override
    public Calendar getCreatedOn() {
        return null;
    }

    @Override
    public Calendar getUpdatedOn() {
        return null;
    }


    @Override
    public Category getCategory() {
        return null;
    }

    @Override
    public boolean equals(Object other) {
        if (other instanceof CategoryManagementDecoratorCategoryGroup) {
            CategoryManagementDecoratorCategoryGroup that = (CategoryManagementDecoratorCategoryGroup) other;
            return Objects.equal(getId(), that.getId())
                    && Objects.equal(getName(), that.getName())
                    && Objects.equal(getDescription(), that.getDescription())
                    && Objects.equal(getCategoryGroupType(), that.getCategoryGroupType())
                    && Objects.equal(getCreatedOn(), that.getCreatedOn())
                    && Objects.equal(getUpdatedOn(), that.getUpdatedOn())
                    && Objects.equal(getCategory(), that.getCategory());
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(
                getId(),
                getName(),
                getDescription(),
                getCategoryGroupType(),
                getCreatedOn(),
                getUpdatedOn(),
                getCategory()
        );
    }
}
