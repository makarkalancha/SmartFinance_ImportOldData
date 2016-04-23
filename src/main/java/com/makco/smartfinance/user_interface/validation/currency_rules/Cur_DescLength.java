package com.makco.smartfinance.user_interface.validation.currency_rules;

import com.makco.smartfinance.constants.DataBaseConstants;
import com.makco.smartfinance.persistence.entity.Currency;
import com.makco.smartfinance.user_interface.utility_screens.DialogMessages;
import com.makco.smartfinance.user_interface.validation.ErrorEnum;
import com.makco.smartfinance.user_interface.validation.Rule;
import java.util.EnumSet;

/**
 * Created by mcalancea on 2016-04-12.
 */
public class Cur_DescLength implements Rule<Currency> {

    @Override
    public EnumSet<ErrorEnum> validate(Currency currency) {
        EnumSet<ErrorEnum> errors = EnumSet.noneOf(ErrorEnum.class);
        try {
            if (currency.getDescription().length() > DataBaseConstants.CUR_DESCRIPTION_MAX_LGTH) {
                errors.add(ErrorEnum.Cur_DESC_LGTH);
            }
        }catch (Exception e){
            DialogMessages.showExceptionAlert(e);
        }
        return errors;
    }
}
