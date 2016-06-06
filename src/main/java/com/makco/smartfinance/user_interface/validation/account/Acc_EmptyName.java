package com.makco.smartfinance.user_interface.validation.account;

import com.makco.smartfinance.persistence.entity.Account;
import com.makco.smartfinance.user_interface.validation.ErrorEnum;
import com.makco.smartfinance.user_interface.validation.Rule;
import org.apache.commons.lang3.StringUtils;

import java.util.EnumSet;

/**
 * Created by mcalancea on 2016-06-06.
 */
public class Acc_EmptyName implements Rule<Account> {

    @Override
    public EnumSet<ErrorEnum> validate(Account account) throws Exception{
        EnumSet<ErrorEnum> errors = EnumSet.noneOf(ErrorEnum.class);
        try {
            if (account != null && StringUtils.isBlank(account.getName())) {
                errors.add(ErrorEnum.Acc_NULL_NAME);
            }
        }catch (Exception e){
            throw e;
        }
        return errors;
    }
}
