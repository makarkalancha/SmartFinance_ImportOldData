package com.makco.smartfinance.user_interface.validation.tax_rules;

import com.makco.smartfinance.user_interface.validation.ErrorEnum;
import com.makco.smartfinance.user_interface.validation.Rule;
import org.apache.commons.lang3.StringUtils;

import java.util.EnumSet;

/**
 * Created by Makar Kalancha on 2016-04-22.
 */
public class TAX_RateNumber implements Rule<String> {

    @Override
    public EnumSet<ErrorEnum> validate(String rateString) throws Exception{
        EnumSet<ErrorEnum> errors = EnumSet.noneOf(ErrorEnum.class);
        try {
//            String excludeValidCharactersRegexPattern = new StringBuilder()
//                    .append("[^0-9")
//                    .append("\\")
//                    .append(BigDecimalUtils.getGroupingSeparator())
//                    .append("\\")
//                    .append(BigDecimalUtils.getDecimalSeparator())
//                    .append("]+")
//                    .toString();
//            String separatorsOnlyOneTime = new StringBuilder()
//                    .
//            BigDecimalUtils.getGroupingSeparator();
//            BigDecimalUtils.getDecimalSeparator();
            if (!StringUtils.isEmpty(rateString) && rateString.matches("[a-zA-Z]+")) {
                errors.add(ErrorEnum.TAX_NAME_NULL);
            }
        }catch (Exception e){
            throw e;
        }
        return errors;
    }
}
