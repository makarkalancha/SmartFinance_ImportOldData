package com.makco.smartfinance.user_interface.validation.tax_rules;

import com.makco.smartfinance.constants.DataBaseConstants;
import com.makco.smartfinance.persistence.entity.Tax;
import com.makco.smartfinance.user_interface.validation.ErrorEnum;
import com.makco.smartfinance.user_interface.validation.Rule;

import java.util.EnumSet;

/**
 * Created by mcalancea on 2016-04-22.
 * Tax start date less or equal than [LTEQ] end date
 */
public class TAX_StartLTEQEndDate implements Rule<Tax> {

    @Override
    public EnumSet<ErrorEnum> validate(Tax tax) throws Exception{
        EnumSet<ErrorEnum> errors = EnumSet.noneOf(ErrorEnum.class);
        try {
            if (tax.getStartDate().compareTo(tax.getEndDate()) <= 0)  {
                errors.add(ErrorEnum.TAX_START_LT_EQ_END);
            }
        }catch (Exception e){
            throw e;
        }
        return errors;
    }
}