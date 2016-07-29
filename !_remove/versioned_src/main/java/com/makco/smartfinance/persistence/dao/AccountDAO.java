package com.makco.smartfinance.persistence.dao;

import com.makco.smartfinance.persistence.entity.Account;

import java.util.List;

/**
 * Created by Makar Kalancha on 2016-06-06.
 */
public interface AccountDAO {
    Account getAccountById(Long id) throws Exception;
    <T extends Account> List<T> accountByType(Class<T> type) throws Exception;
    void saveOrUpdateAccount(Account account) throws Exception;
    void removeAccount(Long id) throws Exception;

    List<Account> getAccountByName(String accountName) throws Exception;
}
