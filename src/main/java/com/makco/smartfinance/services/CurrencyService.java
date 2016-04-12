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
    List<Currency> currencyList();
    Currency getCurrencyById(Long id);
    List<Currency> getCurrencyByCode(String code);

    void addCurrency(Currency currency);
    void removeCurrency(Long id);
    void saveOrUpdateCurrency(Currency currency);
    void updateCurrency(Currency currency);
    EnumSet<ErrorEnum> validate(Currency currency);
}
