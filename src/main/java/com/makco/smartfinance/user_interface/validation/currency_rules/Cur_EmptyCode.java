package com.makco.smartfinance.user_interface.validation.currency_rules;

import com.makco.smartfinance.persistence.entity.Currency;
import com.makco.smartfinance.user_interface.validation.ErrorEnum;
import com.makco.smartfinance.user_interface.validation.Rule;
import org.apache.commons.lang3.StringUtils;

import java.util.EnumSet;

/**
 * Created by Makar Kalancha on 2016-04-12.
 */
public class Cur_EmptyCode implements Rule<Currency> {

    @Override
    public EnumSet<ErrorEnum> validate(Currency currency) throws Exception{
        EnumSet<ErrorEnum> errors = EnumSet.noneOf(ErrorEnum.class);
        try {
            if (StringUtils.isBlank(currency.getCode())) {
                errors.add(ErrorEnum.Cur_CODE_NULL);
            }
        }catch (Exception e){
            throw e;
        }
        return errors;
    }
}
