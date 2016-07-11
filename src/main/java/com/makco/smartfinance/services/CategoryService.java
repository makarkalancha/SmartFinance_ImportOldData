package com.makco.smartfinance.services;

import com.makco.smartfinance.persistence.entity.Category;
import com.makco.smartfinance.user_interface.validation.ErrorEnum;

import java.util.EnumSet;
import java.util.List;

/**
 * Created by Makar Kalancha on 2016-06-02.
 */
public interface CategoryService {
    Category getCategoryById(Long id) throws Exception;
    <T extends Category> List<T> categoryByType(Class<T> type) throws Exception;
    void saveOrUpdateCategory(Category category) throws Exception;
    void removeCategory(Long id) throws Exception;
    List<Category> getCategoryByName(String categoryName) throws Exception;

    EnumSet<ErrorEnum> validate(Category category) throws Exception;
}
