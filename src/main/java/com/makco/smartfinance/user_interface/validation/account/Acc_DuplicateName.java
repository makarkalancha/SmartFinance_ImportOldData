package com.makco.smartfinance.user_interface.validation.account;

import com.makco.smartfinance.persistence.entity.Account;
import com.makco.smartfinance.services.AccountService;
import com.makco.smartfinance.services.AccountServiceImpl;
import com.makco.smartfinance.user_interface.validation.ErrorEnum;
import com.makco.smartfinance.user_interface.validation.Rule;

import java.util.EnumSet;
import java.util.List;

/**
 * Created by mcalancea on 2016-06-06.
 */
public class Acc_DuplicateName implements Rule<Account> {
    private AccountService accountService = new AccountServiceImpl();
    @Override
    public EnumSet<ErrorEnum> validate(Account account) throws Exception{
        EnumSet<ErrorEnum> errors = EnumSet.noneOf(ErrorEnum.class);
        try {
            if (account != null) {
                List<Account> accountByName = accountService.getAccountByName(account.getName());
                accountByName.removeIf(c -> c.getId().equals(account.getId()));
                accountByName.removeIf(c -> !c.getAccountGroupType().equals(account.getAccountGroupType()));
                if (!accountByName.isEmpty()) {
                    errors.add(ErrorEnum.Acc_NAME_DUPLICATE);
                }
            }
        }catch (Exception e){
            throw e;
        }
        return errors;
    }
}
