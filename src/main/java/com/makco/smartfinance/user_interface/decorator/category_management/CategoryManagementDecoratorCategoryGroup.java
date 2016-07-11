package com.makco.smartfinance.user_interface.decorator.category_management;

import com.google.common.base.Objects;
import com.makco.smartfinance.constants.DataBaseConstants;
import com.makco.smartfinance.persistence.entity.Category;
import com.makco.smartfinance.persistence.entity.CategoryGroup;

/**
 * Created by Makar Kalancha on 2016-06-03.
 */
public class CategoryManagementDecoratorCategoryGroup implements CategoryManagementDecorator {
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
    public String getCreatedOn() {
        return null;
    }

    @Override
    public String getUpdatedOn() {
        return null;
    }


    @Override
    public Category getCategory() {
        return null;
    }

    @Override
    public boolean equals(Object other) {
        if (other instanceof CategoryManagementDecorator) {
            CategoryManagementDecorator that = (CategoryManagementDecorator) other;
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
