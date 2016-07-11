package com.makco.smartfinance.user_interface.validation.account;

import com.makco.smartfinance.persistence.entity.Account;
import com.makco.smartfinance.user_interface.validation.ErrorEnum;
import com.makco.smartfinance.user_interface.validation.Rule;

import java.util.EnumSet;

/**
 * Created by Makar Kalancha on 2016-04-08.
 */
public class Acc_EmptyAccountGroup implements Rule<Account> {

    @Override
    public EnumSet<ErrorEnum> validate(Account account) throws Exception{
        EnumSet<ErrorEnum> errors = EnumSet.noneOf(ErrorEnum.class);
        try {
            if (account == null || account.getAccountGroup() == null) {
                errors.add(ErrorEnum.Acc_AG_EMPTY);
            }
        }catch (Exception e){
            throw e;
        }
        return errors;
    }
}
