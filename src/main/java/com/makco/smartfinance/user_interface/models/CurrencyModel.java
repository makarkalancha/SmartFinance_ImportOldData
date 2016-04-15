package com.makco.smartfinance.user_interface.models;

import com.makco.smartfinance.persistence.entity.Currency;
import com.makco.smartfinance.services.CurrencyService;
import com.makco.smartfinance.services.CurrencyServiceImpl;
import com.makco.smartfinance.user_interface.constants.DialogMessages;
import com.makco.smartfinance.user_interface.validation.ErrorEnum;
import java.util.EnumSet;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

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

    public void refreshCurrency(){
        try{
            if (!currencies.isEmpty()) {
                currencies.clear();
            }
            currencies = FXCollections.observableArrayList(currencyService.currencyList());
            LOG.debug("currencies.size: " + currencies.size());
        }catch (Exception e){
            DialogMessages.showExceptionAlert(e);
        }
    }

    public ObservableList<Currency> getCurrencies() {
        return currencies;
    }

    public EnumSet<ErrorEnum> savePendingCurrency(String code, String name, String description) {
        EnumSet<ErrorEnum> errors = EnumSet.noneOf(ErrorEnum.class);
        code = code.toUpperCase();
        try {
            Currency tmpCurrency;
            if (pendingCurrency != null) {
                pendingCurrency.setCode(code);
                pendingCurrency.setName(name);
                pendingCurrency.setDescription(description);
                tmpCurrency = pendingCurrency;
                pendingCurrency = null;
            } else {
                tmpCurrency = new Currency(code, name, description);
            }

            errors = currencyService.validate(tmpCurrency);
            if (errors.isEmpty()) {
                currencyService.saveOrUpdateCurrency(tmpCurrency);
            }
        } catch (Exception e) {
            DialogMessages.showExceptionAlert(e);
        } finally {
            refreshCurrency();
        }
        return errors;
    }

    public void deletePendingCurrency(){
        try{
            if (pendingCurrency != null && pendingCurrency.getId() != null) {
                currencyService.removeCurrency(pendingCurrency.getId());
                pendingCurrency = null;
            }
        } catch (Exception e){
            DialogMessages.showExceptionAlert(e);
        }finally {
            refreshCurrency();
        }
    }

    public Currency getPendingCurrency() {
        return pendingCurrency;
    }

    public void setPendingCurrencyProperty(Currency currency) {
        pendingCurrency = currency;
    }


}
