package com.makco.smartfinance.user_interface.decorator;

import com.google.common.base.Objects;
import com.makco.smartfinance.constants.DataBaseConstants;
import com.makco.smartfinance.persistence.entity.Category;
import com.makco.smartfinance.persistence.entity.CategoryGroup;

import java.util.Calendar;

/**
 * Created by mcalancea on 2016-06-03.
 */
public class CategoryManagementDecoratorCategory implements CategoryManagmentDecorator {
    private Long id;
    private DataBaseConstants.CATEGORY_GROUP_TYPE type;
    private String name;
    private String description;
    private Calendar createdOn;
    private Calendar updatedOn;
    private Category category;

    public CategoryManagementDecoratorCategory(Category category){
        this.id = category.getId();
        this.type = category.getCategoryGroupType();
        this.name = category.getName();
        this.description = category.getDescription();

        Calendar calendarCreated = Calendar.getInstance();
        calendarCreated.setTime(category.getCreatedOn());
        this.createdOn = calendarCreated;

        Calendar calendarUpdated = Calendar.getInstance();
        calendarUpdated.setTime(category.getUpdatedOn());
        this.updatedOn = calendarUpdated;

        this.category = category;
    }

    @Override
    public Long getId() {
        return id;
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
        return createdOn;
    }

    @Override
    public Calendar getUpdatedOn() {
        return updatedOn;
    }

    @Override
    public Category getCategory() {
        return category;
    }

    @Override
    public boolean equals(Object other) {
        if (other instanceof CategoryManagementDecoratorCategory) {
            CategoryManagementDecoratorCategory that = (CategoryManagementDecoratorCategory) other;
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
