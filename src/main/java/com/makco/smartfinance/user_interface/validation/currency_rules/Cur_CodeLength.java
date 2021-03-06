package com.makco.smartfinance.user_interface.validation.currency_rules;

import com.makco.smartfinance.constants.DataBaseConstants;
import com.makco.smartfinance.persistence.entity.Currency;
import com.makco.smartfinance.user_interface.validation.ErrorEnum;
import com.makco.smartfinance.user_interface.validation.Rule;

import java.util.EnumSet;

/**
 * Created by Makar Kalancha on 2016-04-12.
 */
public class Cur_CodeLength implements Rule<Currency> {

    @Override
    public EnumSet<ErrorEnum> validate(Currency currency) throws Exception{
        EnumSet<ErrorEnum> errors = EnumSet.noneOf(ErrorEnum.class);
        try {
            if (currency.getCode().length() != DataBaseConstants.CUR_CODE_MAX_LGTH) {
                errors.add(ErrorEnum.Cur_CODE_LGTH);
            }
        }catch (Exception e){
            throw e;
        }
        return errors;
    }
}
