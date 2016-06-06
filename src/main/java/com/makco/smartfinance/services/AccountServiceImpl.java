package com.makco.smartfinance.services;

import com.makco.smartfinance.persistence.dao.AccountDAO;
import com.makco.smartfinance.persistence.dao.AccountDAOImpl;
import com.makco.smartfinance.persistence.entity.Account;
import com.makco.smartfinance.user_interface.validation.ErrorEnum;
import com.makco.smartfinance.user_interface.validation.RuleSet;
import com.makco.smartfinance.user_interface.validation.rule_sets.AccountRuleSet;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

/**
 * Created by mcalancea on 2016-06-02.
 */
public class AccountServiceImpl implements AccountService {
    private final static Logger LOG = LogManager.getLogger(AccountServiceImpl.class);
    private AccountDAO accountDAO = new AccountDAOImpl();
    @Override
    public <T extends Account> List<T> accountByType(Class<T> type) throws Exception {
        List<T> accountList = new ArrayList<>();
        try {
            accountList = accountDAO.accountByType(type);
        } catch (Exception e) {
            throw e;
        }
        return accountList;
    }

    @Override
    public Account getAccountById(Long id) throws Exception {
        Account account = null;
        try {
            account = accountDAO.getAccountById(id);
        } catch (Exception e) {
            throw e;
        }
        return account;
    }

    @Override
    public void saveOrUpdateAccount(Account account) throws Exception {
        try {
            accountDAO.saveOrUpdateAccount(account);
        } catch (Exception e) {
            throw e;
        }
    }

    @Override
    public void removeAccount(Long id) throws Exception {
        try{
            accountDAO.removeAccount(id);
        } catch (Exception e) {
            throw e;
        }
    }

    @Override
    public List<Account> getAccountByName(String accountName) throws Exception {
        List<Account> categories = null;
        try {
            categories = accountDAO.getAccountByName(accountName);
        } catch (Exception e) {
            throw e;
        }
        return categories;
    }

    @Override
    public EnumSet<ErrorEnum> validate(Account account) throws Exception {
        EnumSet<ErrorEnum> errors = EnumSet.noneOf(ErrorEnum.class);
        try {
            RuleSet ruleSet = new AccountRuleSet();
            errors = ruleSet.validate(account);
        } catch (Exception e) {
            throw e;
        }
        return errors;
    }
}
