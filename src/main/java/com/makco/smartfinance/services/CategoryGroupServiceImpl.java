package com.makco.smartfinance.services;

import com.makco.smartfinance.persistence.dao.CategoryGroupDAO;
import com.makco.smartfinance.persistence.dao.CategoryGroupDAOImpl;
import com.makco.smartfinance.persistence.dao.CurrencyDAO;
import com.makco.smartfinance.persistence.dao.CurrencyDAOImpl;
import com.makco.smartfinance.persistence.entity.CategoryGroup;
import com.makco.smartfinance.persistence.entity.CategoryGroupCredit;
import com.makco.smartfinance.persistence.entity.CategoryGroupDebit;
import com.makco.smartfinance.persistence.entity.Currency;
import com.makco.smartfinance.user_interface.validation.ErrorEnum;
import com.makco.smartfinance.user_interface.validation.RuleSet;
import com.makco.smartfinance.user_interface.validation.rule_sets.CurrencyRuleSet;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

/**
 * Created by mcalancea on 2016-05-13.
 */
public class CategoryGroupServiceImpl implements CategoryGroupService {
    private final static Logger LOG = LogManager.getLogger(CategoryGroupServiceImpl.class);
    private CategoryGroupDAO categoryGroupDAO = new CategoryGroupDAOImpl();
    @Override
    public List<CategoryGroup> categoryGroupList(boolean initializeCategories) throws Exception {
        List<CategoryGroup> categoryGroupList = new ArrayList<>();
        try {
            categoryGroupList = categoryGroupDAO.categoryGroupList(initializeCategories);
        } catch (Exception e) {
            throw e;
        }
        return categoryGroupList;
    }

    @Override
    public <T extends CategoryGroup> List<T> categoryGroupByType(Class<T> type, boolean initializeCategories) throws Exception {
        List<T> categoryGroupList = new ArrayList<>();
        try {
            categoryGroupList = categoryGroupDAO.categoryGroupByType(type, initializeCategories);
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
}
