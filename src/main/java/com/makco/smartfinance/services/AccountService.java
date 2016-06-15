package com.makco.smartfinance.services;

import com.makco.smartfinance.persistence.entity.Account;
import com.makco.smartfinance.user_interface.validation.ErrorEnum;

import java.util.EnumSet;
import java.util.List;

/**
 * Created by mcalancea on 2016-06-02.
 */
public interface AccountService {
    Account getAccountById(Long id) throws Exception;
    <T extends Account> List<T> accountByType(Class<T> type) throws Exception;
    void saveOrUpdateAccount(Account account) throws Exception;
    void removeAccount(Long id) throws Exception;
    List<Account> getAccountByName(String accountName) throws Exception;

    EnumSet<ErrorEnum> validate(Account account) throws Exception;
}
