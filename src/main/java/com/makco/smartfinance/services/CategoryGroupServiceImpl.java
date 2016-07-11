package com.makco.smartfinance.services;

import com.makco.smartfinance.persistence.dao.CategoryGroupDAO;
import com.makco.smartfinance.persistence.dao.CategoryGroupDAOImpl;
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
 * Created by Makar Kalancha on 2016-05-13.
 */
public class CategoryGroupServiceImpl implements CategoryGroupService {
    private final static Logger LOG = LogManager.getLogger(CategoryGroupServiceImpl.class);
    private CategoryGroupDAO categoryGroupDAO = new CategoryGroupDAOImpl();

    @Override
    public List<CategoryGroup> categoryGroupListWithoutCategories() throws Exception {
        List<CategoryGroup> categoryGroupList = new ArrayList<>();
        try {
            categoryGroupList = categoryGroupDAO.categoryGroupListWithoutCategories();
        } catch (Exception e) {
            throw e;
        }
        return categoryGroupList;
    }

    @Override
    public List<CategoryGroup> categoryGroupListWithCategories() throws Exception {
        List<CategoryGroup> categoryGroupList = new ArrayList<>();
        try {
            categoryGroupList = categoryGroupDAO.categoryGroupListWithCategories();
        } catch (Exception e) {
            throw e;
        }
        return categoryGroupList;
    }

    @Override
    public void removeCategoryGroup(Long id) throws Exception {
        try{
            categoryGroupDAO.removeCategoryGroup(id);
        } catch (Exception e) {
            throw e;
        }
    }

    @Override
    public void saveOrUpdateCategoryGroup(CategoryGroup categoryGroup) throws Exception {
        try{
            categoryGroupDAO.saveOrUpdateCategoryGroup(categoryGroup);
        } catch (Exception e) {
            throw e;
        }
    }

    @Override
    public List<CategoryGroup> getCategoryGroupByName(String categoryGroupName) throws Exception {
        List<CategoryGroup> categoryGroupList = new ArrayList<>();
        try{
            categoryGroupList = categoryGroupDAO.getCategoryGroupByName(categoryGroupName);
        } catch (Exception e) {
            throw e;
        }
        return categoryGroupList;
    }

    @Override
    public EnumSet<ErrorEnum> validate(CategoryGroup categoryGroup) throws Exception {
        EnumSet<ErrorEnum> errors = EnumSet.noneOf(ErrorEnum.class);
        try {
            RuleSet ruleSet = new CategoryGroupRuleSet();
            errors = ruleSet.validate(categoryGroup);
        } catch (Exception e) {
            throw e;
        }
        return errors;
    }

    @Override
    public CategoryGroup getCategoryGroupById(Long id, boolean initializeCategories) throws Exception {
        CategoryGroup categoryGroup = null;
        try{
            categoryGroup = categoryGroupDAO.getCategoryGroupById(id, initializeCategories);
        } catch (Exception e) {
            throw e;
        }
        return categoryGroup;
    }
}
