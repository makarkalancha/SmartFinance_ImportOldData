package com.makco.smartfinance.user_interface.models;

import com.makco.smartfinance.persistence.entity.Currency;
import com.makco.smartfinance.services.CurrencyService;
import com.makco.smartfinance.services.CurrencyServiceImpl;
import com.makco.smartfinance.user_interface.validation.ErrorEnum;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.EnumSet;

/**
 * Created by mcalancea on 2016-04-12.
 */
public class CurrencyModel {
    private final static Logger LOG = LogManager.getLogger(CurrencyModel.class);
    private CurrencyService currencyService = new CurrencyServiceImpl();
    private ObservableList<Currency> currencies = FXCollections.observableArrayList();
    private Currency pendingCurrency;

    public CurrencyModel(){

    }

    public void refreshCurrency() throws Exception{
        try{
            if (!currencies.isEmpty()) {
                currencies.clear();
            }
            currencies = FXCollections.observableArrayList(currencyService.currencyList());
            LOG.debug("currencies.size: " + currencies.size());
        }catch (Exception e) {
            throw e;
        }
    }

    public ObservableList<Currency> getCurrencies() throws Exception {
        return currencies;
    }

    public EnumSet<ErrorEnum> savePendingCurrency(String code, String name, String description) throws Exception {
        EnumSet<ErrorEnum> errors = EnumSet.noneOf(ErrorEnum.class);
        code = code.toUpperCase();
        try {
            Currency tmpCurrency;
            if (pendingCurrency != null) {
                pendingCurrency.setCode(code);
                pendingCurrency.setName(name);
                pendingCurrency.setDescription(description);
                tmpCurrency = pendingCurrency;
            } else {
                tmpCurrency = new Currency(code, name, description);
            }

            errors = currencyService.validate(tmpCurrency);
            if (errors.isEmpty()) {
                currencyService.saveOrUpdateCurrency(tmpCurrency);
                pendingCurrency = null;
            }
        } catch (Exception e) {
            throw e;
        } finally {
            refreshCurrency();
        }
        return errors;
    }

    public void deletePendingCurrency() throws Exception {
        try {
            if (pendingCurrency != null && pendingCurrency.getId() != null) {
                currencyService.removeCurrency(pendingCurrency.getId());
                pendingCurrency = null;
            }
        } catch (Exception e) {
            throw e;
        } finally {
            refreshCurrency();
        }
    }

    public Currency getPendingCurrency() throws Exception {
        return pendingCurrency;
    }

    public void setPendingCurrencyProperty(Currency currency) throws Exception {
        pendingCurrency = currency;
    }
}
