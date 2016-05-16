package com.makco.smartfinance.persistence.dao;

import com.makco.smartfinance.persistence.entity.CategoryGroup;

import java.util.List;

/**
 * Created by mcalancea on 2016-05-12.
 */
public interface CategoryGroupDAO {
    List<CategoryGroup> categoryGroupList(boolean initializeCategories) throws Exception;
    <T extends CategoryGroup> List<T> categoryGroupByType(Class<T> type, boolean initializeCategories) throws Exception;
    void saveOrUpdateCategoryGroup(CategoryGroup categoryGroup) throws Exception;
    void removeCategoryGroup(Long id) throws Exception;

    List<CategoryGroup> getCategoryGroupByName(String categoryGroupName, boolean initializeCategories) throws Exception;
}
