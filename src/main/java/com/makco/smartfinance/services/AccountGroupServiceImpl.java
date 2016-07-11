package com.makco.smartfinance.services;

import com.makco.smartfinance.persistence.dao.AccountGroupDAO;
import com.makco.smartfinance.persistence.dao.AccountGroupDAOImpl;
import com.makco.smartfinance.persistence.entity.AccountGroup;
import com.makco.smartfinance.user_interface.validation.ErrorEnum;
import com.makco.smartfinance.user_interface.validation.RuleSet;
import com.makco.smartfinance.user_interface.validation.rule_sets.AccountGroupRuleSet;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

/**
 * Created by Makar Kalancha on 2016-06-06.
 */
public class AccountGroupServiceImpl implements AccountGroupService {
    private final static Logger LOG = LogManager.getLogger(AccountGroupServiceImpl.class);
    private AccountGroupDAO accountGroupDAO = new AccountGroupDAOImpl();

    @Override
    public List<AccountGroup> accountGroupListWithoutCategories() throws Exception {
        List<AccountGroup> accountGroupList = new ArrayList<>();
        try {
            accountGroupList = accountGroupDAO.accountGroupListWithoutCategories();
        } catch (Exception e) {
            throw e;
        }
        return accountGroupList;
    }

    @Override
    public List<AccountGroup> accountGroupListWithCategories() throws Exception {
        List<AccountGroup> accountGroupList = new ArrayList<>();
        try {
            accountGroupList = accountGroupDAO.accountGroupListWithCategories();
        } catch (Exception e) {
            throw e;
        }
        return accountGroupList;
    }

    @Override
    public void removeAccountGroup(Long id) throws Exception {
        try{
            accountGroupDAO.removeAccountGroup(id);
        } catch (Exception e) {
            throw e;
        }
    }

    @Override
    public void saveOrUpdateAccountGroup(AccountGroup accountGroup) throws Exception {
        try{
            accountGroupDAO.saveOrUpdateAccountGroup(accountGroup);
        } catch (Exception e) {
            throw e;
        }
    }

    @Override
    public List<AccountGroup> getAccountGroupByName(String accountGroupName) throws Exception {
        List<AccountGroup> accountGroupList = new ArrayList<>();
        try{
            accountGroupList = accountGroupDAO.getAccountGroupByName(accountGroupName);
        } catch (Exception e) {
            throw e;
        }
        return accountGroupList;
    }

    @Override
    public EnumSet<ErrorEnum> validate(AccountGroup accountGroup) throws Exception {
        EnumSet<ErrorEnum> errors = EnumSet.noneOf(ErrorEnum.class);
        try {
            RuleSet ruleSet = new AccountGroupRuleSet();
            errors = ruleSet.validate(accountGroup);
        } catch (Exception e) {
            throw e;
        }
        return errors;
    }

    @Override
    public AccountGroup getAccountGroupById(Long id, boolean initializeCategories) throws Exception {
        AccountGroup accountGroup = null;
        try{
            accountGroup = accountGroupDAO.getAccountGroupById(id, initializeCategories);
        } catch (Exception e) {
            throw e;
        }
        return accountGroup;
    }
}
