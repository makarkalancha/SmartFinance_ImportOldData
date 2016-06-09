package com.makco.smartfinance.user_interface.validation.tax_rules;

import com.makco.smartfinance.persistence.entity.Tax;
import com.makco.smartfinance.user_interface.validation.ErrorEnum;
import com.makco.smartfinance.user_interface.validation.Rule;
import org.apache.commons.lang3.StringUtils;

import java.util.EnumSet;

/**
 * Created by mcalancea on 2016-04-22.
 */
public class TAX_EmptyName implements Rule<Tax> {

    @Override
    public EnumSet<ErrorEnum> validate(Tax tax) throws Exception{
        EnumSet<ErrorEnum> errors = EnumSet.noneOf(ErrorEnum.class);
        try {
            if (StringUtils.isBlank(tax.getName())) {
                errors.add(ErrorEnum.TAX_NAME_NULL);
            }
        }catch (Exception e){
            throw e;
        }
        return errors;
    }
}
