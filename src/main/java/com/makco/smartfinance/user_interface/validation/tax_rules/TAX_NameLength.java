package com.makco.smartfinance.user_interface.validation.tax_rules;

import com.makco.smartfinance.constants.DataBaseConstants;
import com.makco.smartfinance.persistence.entity.Tax;
import com.makco.smartfinance.user_interface.validation.ErrorEnum;
import com.makco.smartfinance.user_interface.validation.Rule;

import java.util.EnumSet;

/**
 * Created by Makar Kalancha on 2016-04-22.
 */
public class TAX_NameLength implements Rule<Tax> {

    @Override
    public EnumSet<ErrorEnum> validate(Tax tax) throws Exception{
        EnumSet<ErrorEnum> errors = EnumSet.noneOf(ErrorEnum.class);
        try {
            if (tax.getName().length() > DataBaseConstants.TAX_NAME_MAX_LGTH) {
                errors.add(ErrorEnum.TAX_NAME_LGTH);
            }
        }catch (Exception e){
            throw e;
        }
        return errors;
    }
}
