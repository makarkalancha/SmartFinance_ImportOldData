package com.makco.smartfinance.services;

import com.makco.smartfinance.persistence.entity.CategoryGroup;
import com.makco.smartfinance.user_interface.validation.ErrorEnum;

import java.util.EnumSet;
import java.util.List;

/**
 * Created by Makar Kalancha on 2016-05-13.
 */
public interface CategoryGroupService {
    CategoryGroup getCategoryGroupById(Long id, boolean initializeCategories) throws Exception;
    List<CategoryGroup> categoryGroupListWithoutCategories() throws Exception;
    List<CategoryGroup> categoryGroupListWithCategories() throws Exception;
    void saveOrUpdateCategoryGroup(CategoryGroup categoryGroup) throws Exception;
    void removeCategoryGroup(Long id) throws Exception;
    List<CategoryGroup> getCategoryGroupByName(String categoryGroupName) throws Exception;

    EnumSet<ErrorEnum> validate(CategoryGroup categoryGroup) throws Exception;
}
