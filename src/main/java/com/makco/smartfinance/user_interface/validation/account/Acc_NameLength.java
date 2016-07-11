package com.makco.smartfinance.user_interface.validation.account;

import com.makco.smartfinance.constants.DataBaseConstants;
import com.makco.smartfinance.persistence.entity.Account;
import com.makco.smartfinance.user_interface.validation.ErrorEnum;
import com.makco.smartfinance.user_interface.validation.Rule;

import java.util.EnumSet;

/**
 * Created by Makar Kalancha on 2016-06-06.
 */
public class Acc_NameLength implements Rule<Account> {

    @Override
    public EnumSet<ErrorEnum> validate(Account account) throws Exception{
        EnumSet<ErrorEnum> errors = EnumSet.noneOf(ErrorEnum.class);
        try {
            if (account != null && account.getName().length() > DataBaseConstants.ACC_NAME_MAX_LGTH) {
                errors.add(ErrorEnum.Acc_NAME_LGTH);
            }
        }catch (Exception e){
            throw e;
        }
        return errors;
    }
}
