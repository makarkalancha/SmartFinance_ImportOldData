package com.makco.smartfinance.user_interface.validation.tax_rules;

import com.makco.smartfinance.persistence.entity.Tax;
import com.makco.smartfinance.services.TaxService;
import com.makco.smartfinance.services.TaxServiceImpl;
import com.makco.smartfinance.user_interface.validation.ErrorEnum;
import com.makco.smartfinance.user_interface.validation.Rule;

import java.util.EnumSet;
import java.util.List;

/**
 * Created by Makar Kalancha on 2016-04-22.
 */
public class TAX_DuplicateName implements Rule<Tax> {
    private TaxService taxService = new TaxServiceImpl();
    @Override
    public EnumSet<ErrorEnum> validate(Tax tax) throws Exception{
        EnumSet<ErrorEnum> errors = EnumSet.noneOf(ErrorEnum.class);
        try {
            List<Tax> taxes = taxService.getTaxByName(tax.getName());
            taxes.removeIf(org -> org.getId().equals(tax.getId()));
            if (!taxes.isEmpty()) {
                errors.add(ErrorEnum.TAX_NAME_DUPLICATE);
            }
        }catch (Exception e){
            throw e;
        }
        return errors;
    }
}
