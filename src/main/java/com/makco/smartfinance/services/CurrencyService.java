package com.makco.smartfinance.services;

import com.makco.smartfinance.persistence.entity.Currency;
import com.makco.smartfinance.user_interface.validation.ErrorEnum;
import java.util.EnumSet;
import java.util.List;

/**
 * Created by mcalancea on 2016-04-12.
 */
//http://programmers.stackexchange.com/questions/220909/service-layer-vs-dao-why-both
//http://programmers.stackexchange.com/questions/162399/how-essential-is-it-to-make-a-service-layer
public interface CurrencyService {
    List<Currency> currencyList() throws Exception;
    Currency getCurrencyById(Long id) throws Exception;
    List<Currency> getCurrencyByCode(String code) throws Exception;

    void removeCurrency(Long id) throws Exception;
    void saveOrUpdateCurrency(Currency currency) throws Exception;
    EnumSet<ErrorEnum> validate(Currency currency) throws Exception;
}
