package com.makco.smartfinance.services;

import com.makco.smartfinance.persistence.dao.CurrencyDAO;
import com.makco.smartfinance.persistence.dao.CurrencyDAOImpl;
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
 * Created by Makar Kalancha on 2016-04-12.
 */
public class CurrencyServiceImpl implements CurrencyService {
    private final static Logger LOG = LogManager.getLogger(CurrencyServiceImpl.class);
    private CurrencyDAO currencyDAO = new CurrencyDAOImpl();

    @Override
    public List<Currency> currencyList() throws Exception {
        List<Currency> currencyList = new ArrayList<>();
        try {
            currencyList = currencyDAO.currencyList();
        } catch (Exception e) {
            throw e;
        }
        return currencyList;
    }

    @Override
    public Currency getCurrencyById(Long id) throws Exception {
        Currency currency = null;
        try {
            currency = currencyDAO.getCurrencyById(id);
        } catch (Exception e) {
            throw e;
        }
        return currency;
    }

    @Override
    public List<Currency> getCurrencyByCode(String code) throws Exception {
        List<Currency> currencyList = new ArrayList<>();
        try {
            currencyList = currencyDAO.getCurrencyByCode(code);
        } catch (Exception e) {
            throw e;
        }
        return currencyList;
    }

    @Override
    public void removeCurrency(Long id) throws Exception {
        try{
            currencyDAO.removeCurrency(id);
        } catch (Exception e) {
            throw e;
        }
    }

    @Override
    public void saveOrUpdateCurrency(Currency currency) throws Exception {
        try{
            currencyDAO.saveOrUpdateCurrency(currency);
        } catch (Exception e) {
            throw e;
        }
    }

    @Override
    public EnumSet<ErrorEnum> validate(Currency currency) throws Exception {
        EnumSet<ErrorEnum> errors = EnumSet.noneOf(ErrorEnum.class);
        try {
            RuleSet ruleSet = new CurrencyRuleSet();
            errors = ruleSet.validate(currency);
        } catch (Exception e) {
            throw e;
        }
        return errors;
    }
}
