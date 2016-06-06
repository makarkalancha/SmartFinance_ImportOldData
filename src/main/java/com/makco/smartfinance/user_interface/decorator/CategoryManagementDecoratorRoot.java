package com.makco.smartfinance.user_interface.decorator;

import com.makco.smartfinance.persistence.entity.Category;

/**
 * User: Makar Kalancha
 * Date: 05/06/2016
 * Time: 17:53
 */
public class CategoryManagementDecoratorRoot implements CategoryManagmentDecorator {
    private String name;

    public CategoryManagementDecoratorRoot(String name) {
        this.name = name;
    }

    @Override
    public Long getId() {
        return null;
    }

    @Override
    public String getDescription() {
        return null;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getCategoryGroupType() {
        return null;
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
}
