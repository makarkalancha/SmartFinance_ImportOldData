package com.makco.smartfinance.persistence.dao;

import com.makco.smartfinance.persistence.entity.CategoryGroup;

import java.util.List;

/**
 * Created by mcalancea on 2016-05-12.
 */
public interface CategoryGroupDAO {
    CategoryGroup getCategoryGroupById(Long id, boolean initializeCategories) throws Exception;
    List<CategoryGroup> categoryGroupListWithoutCategories() throws Exception;
    List<CategoryGroup> categoryGroupListWithCategories()throws Exception;
    void saveOrUpdateCategoryGroup(CategoryGroup categoryGroup) throws Exception;
    void removeCategoryGroup(Long id) throws Exception;

    List<CategoryGroup> getCategoryGroupByName(String categoryGroupName) throws Exception;
}
