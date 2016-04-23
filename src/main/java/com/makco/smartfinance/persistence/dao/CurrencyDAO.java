package com.makco.smartfinance.persistence.dao;

import com.makco.smartfinance.persistence.entity.Currency;
import java.util.List;

/**
 * Created by mcalancea on 2016-04-12.
 */
//https://howtoprogramwithjava.com/hibernate-creating-data-access-objects-daos/
public interface CurrencyDAO {
    List<Currency> currencyList() throws Exception;
    Currency getCurrencyById(Long id) throws Exception;
    List<Currency> getCurrencyByCode(String code) throws Exception;

    void removeCurrency(Long id) throws Exception;
    void saveOrUpdateCurrency(Currency currency) throws Exception;
}
