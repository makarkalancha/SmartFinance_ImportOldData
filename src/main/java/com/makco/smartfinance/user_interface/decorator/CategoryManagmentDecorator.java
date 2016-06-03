package com.makco.smartfinance.user_interface.decorator;

import com.makco.smartfinance.persistence.entity.Category;
import com.makco.smartfinance.persistence.entity.CategoryGroup;
import java.util.Calendar;

/**
 * Created by mcalancea on 2016-06-03.
 */
public interface CategoryManagmentDecorator {
    Long getId();
    String getDescription();
    String getName();
    String getCategoryGroupType();
    Calendar getCreatedOn();
    Calendar getUpdatedOn();
    Category getCategory();
}
