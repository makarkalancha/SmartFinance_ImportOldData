package com.makco.smartfinance.services;

import com.makco.smartfinance.persistence.dao.CategoryDAO;
import com.makco.smartfinance.persistence.dao.CategoryDAOImpl;
import com.makco.smartfinance.persistence.dao.CategoryGroupDAO;
import com.makco.smartfinance.persistence.dao.CategoryGroupDAOImpl;
import com.makco.smartfinance.persistence.entity.Category;
import com.makco.smartfinance.persistence.entity.CategoryGroup;
import com.makco.smartfinance.user_interface.validation.ErrorEnum;
import com.makco.smartfinance.user_interface.validation.RuleSet;
import com.makco.smartfinance.user_interface.validation.rule_sets.CategoryGroupRuleSet;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

/**
 * Created by mcalancea on 2016-06-02.
 */
public class CategoryServiceImpl implements CategoryService {
    private final static Logger LOG = LogManager.getLogger(CategoryServiceImpl.class);
    private CategoryDAO categoryDAO = new CategoryDAOImpl();
    @Override
    public <T extends Category> List<T> categoryByType(Class<T> type) throws Exception {
        List<T> categoryList = new ArrayList<>();
        try {
            categoryList = categoryDAO.categoryByType(type);
        } catch (Exception e) {
            throw e;
        }
        return categoryList;
    }

    @Override
    public Category getCategoryById(Long id) throws Exception {
        Category category = null;
        try {
            category = categoryDAO.getCategoryById(id);
        } catch (Exception e) {
            throw e;
        }
        return category;
    }

    @Override
    public List<Category> categoryList() throws Exception {
        List<Category> categories = null;
        try {
            categories = categoryDAO.categoryList();
        } catch (Exception e) {
            throw e;
        }
        return categories;
    }

    @Override
    public void saveOrUpdateCategory(Category category) throws Exception {
        try {
            categoryDAO.saveOrUpdateCategory(category);
        } catch (Exception e) {
            throw e;
        }
    }

    @Override
    public void removeCategory(Long id) throws Exception {
        try{
            categoryDAO.removeCategory(id);
        } catch (Exception e) {
            throw e;
        }
    }

    @Override
    public List<Category> getCategoryByName(String categoryName) throws Exception {
        List<Category> categories = null;
        try {
            categories = categoryDAO.getCategoryByName(categoryName);
        } catch (Exception e) {
            throw e;
        }
        return categories;
    }

    @Override
    public EnumSet<ErrorEnum> validate(Category category) throws Exception {
        EnumSet<ErrorEnum> errors = EnumSet.noneOf(ErrorEnum.class);
        try {
            RuleSet ruleSet = new CategoryGroupRuleSet();
            errors = ruleSet.validate(category);
        } catch (Exception e) {
            throw e;
        }
        return errors;
    }
}
