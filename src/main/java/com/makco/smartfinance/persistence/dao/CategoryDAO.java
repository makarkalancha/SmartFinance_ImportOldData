package com.makco.smartfinance.persistence.dao;

import com.makco.smartfinance.persistence.entity.Category;
import com.makco.smartfinance.persistence.entity.CategoryGroup;
import com.makco.smartfinance.persistence.entity.Organization;

import java.util.List;

/**
 * Created by mcalancea on 2016-05-12.
 */
public interface CategoryDAO {
    Category getCategoryById(Long id) throws Exception;
    <T extends Category> List<T> categoryByType(Class<T> type) throws Exception;
    void saveOrUpdateCategory(Category category) throws Exception;
    void removeCategory(Long id) throws Exception;

    List<Category> getCategoryByName(String categoryName) throws Exception;
}
