package com.makco.smartfinance.user_interface.validation.currency_rules;

import com.makco.smartfinance.persistence.entity.Currency;
import com.makco.smartfinance.services.CurrencyService;
import com.makco.smartfinance.services.CurrencyServiceImpl;
import com.makco.smartfinance.user_interface.constants.DialogMessages;
import com.makco.smartfinance.user_interface.validation.ErrorEnum;
import com.makco.smartfinance.user_interface.validation.Rule;
import java.util.EnumSet;
import java.util.List;

/**
 * Created by mcalancea on 2016-04-12.
 */
public class Cur_DuplicateCode implements Rule<Currency> {
    private CurrencyService currencyService = new CurrencyServiceImpl();
    @Override
    public EnumSet<ErrorEnum> validate(Currency currency) {
        EnumSet<ErrorEnum> errors = EnumSet.noneOf(ErrorEnum.class);
        try {
            List<Currency> currencies = currencyService.getCurrencyByCode(currency.getCode());
            currencies.removeIf(c -> c.getId().equals(currency.getId()));
            if (!currencies.isEmpty()) {
                errors.add(ErrorEnum.Cur_CODE_DUPLICATE);
            }
        }catch (Exception e){
            DialogMessages.showExceptionAlert(e);
        }
        return errors;
    }
}
