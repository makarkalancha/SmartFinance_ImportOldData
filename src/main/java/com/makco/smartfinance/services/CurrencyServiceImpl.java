package com.makco.smartfinance.services;

import com.makco.smartfinance.persistence.dao.CurrencyDAO;
import com.makco.smartfinance.persistence.dao.CurrencyDAOImpl;
import com.makco.smartfinance.persistence.entity.Currency;
import com.makco.smartfinance.user_interface.constants.DialogMessages;
import com.makco.smartfinance.user_interface.validation.ErrorEnum;
import com.makco.smartfinance.user_interface.validation.RuleSet;
import com.makco.smartfinance.user_interface.validation.rule_sets.CurrencyRuleSet;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Created by mcalancea on 2016-04-12.
 */
public class CurrencyServiceImpl implements CurrencyService {
    private final static Logger LOG = LogManager.getLogger(CurrencyServiceImpl.class);
    private CurrencyDAO currencyDAO = new CurrencyDAOImpl();

    @Override
    public void addCurrency(Currency currency) {
        try {
            currencyDAO.addCurrency(currency);
        } catch (Exception e) {
            DialogMessages.showExceptionAlert(e);
        }
    }

    @Override
    public List<Currency> currencyList() {
        List<Currency> currencyList = new ArrayList<>();
        try {
            currencyList = currencyDAO.currencyList();
        } catch (Exception e) {
            DialogMessages.showExceptionAlert(e);
        }
        return currencyList;
    }

    @Override
    public Currency getCurrencyById(Long id) {
        Currency currency = null;
        try {
            currency = currencyDAO.getCurrencyById(id);
        } catch (Exception e) {
            DialogMessages.showExceptionAlert(e);
        }
        return currency;
    }

    @Override
    public List<Currency> getCurrencyByCode(String code) {
        List<Currency> currencyList = new ArrayList<>();
        try {
            currencyList = currencyDAO.getCurrencyByCode(code);
        } catch (Exception e) {
            DialogMessages.showExceptionAlert(e);
        }
        return currencyList;
    }

    @Override
    public void removeCurrency(Long id) {
        try{
            currencyDAO.removeCurrency(id);
        } catch (Exception e) {
            DialogMessages.showExceptionAlert(e);
        }
    }

    @Override
    public void saveOrUpdateCurrency(Currency currency) {
        try{
            currencyDAO.saveOrUpdateCurrency(currency);
        } catch (Exception e) {
            DialogMessages.showExceptionAlert(e);
        }
    }

    @Override
    public void updateCurrency(Currency currency) {
        try{
            currencyDAO.updateCurrency(currency);
        } catch (Exception e) {
            DialogMessages.showExceptionAlert(e);
        }
    }

    @Override
    public EnumSet<ErrorEnum> validate(Currency currency) {
        EnumSet<ErrorEnum> errors = EnumSet.noneOf(ErrorEnum.class);
        try {
            RuleSet ruleSet = new CurrencyRuleSet();
            errors = ruleSet.validate(currency);
        } catch (Exception e) {
            DialogMessages.showExceptionAlert(e);
        }
        return errors;
    }
}
