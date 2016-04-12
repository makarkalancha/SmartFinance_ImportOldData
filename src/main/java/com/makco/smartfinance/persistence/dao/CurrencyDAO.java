package com.makco.smartfinance.persistence.dao;

import com.makco.smartfinance.persistence.entity.Currency;
import java.util.List;

/**
 * Created by mcalancea on 2016-04-12.
 */
//https://howtoprogramwithjava.com/hibernate-creating-data-access-objects-daos/
public interface CurrencyDAO {
    List<Currency> currencyList();
    Currency getCurrencyById(Long id);
    List<Currency> getCurrencyByCode(String code);

    void addCurrency(Currency currency);
    void removeCurrency(Long id);
    void saveOrUpdateCurrency(Currency currency);
    void updateCurrency(Currency currency);
}
