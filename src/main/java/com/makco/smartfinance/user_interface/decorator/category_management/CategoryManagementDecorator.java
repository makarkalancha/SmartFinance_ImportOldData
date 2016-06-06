package com.makco.smartfinance.user_interface.decorator.category_management;

import com.makco.smartfinance.persistence.entity.Category;

/**
 * Created by mcalancea on 2016-06-03.
 */
public interface CategoryManagementDecorator {
    Long getId();
    String getDescription();
    String getName();
    String getCategoryGroupType();
    String getCreatedOn();
    String getUpdatedOn();
    Category getCategory();
}
