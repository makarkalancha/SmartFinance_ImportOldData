package com.makco.smartfinance.user_interface.validation.rule_sets;

import com.makco.smartfinance.persistence.entity.Currency;
import com.makco.smartfinance.user_interface.constants.DialogMessages;
import com.makco.smartfinance.user_interface.validation.ErrorEnum;
import com.makco.smartfinance.user_interface.validation.Rule;
import com.makco.smartfinance.user_interface.validation.RuleSet;
import com.makco.smartfinance.user_interface.validation.currency_rules.Cur_CodeLength;
import com.makco.smartfinance.user_interface.validation.currency_rules.Cur_DescLength;
import com.makco.smartfinance.user_interface.validation.currency_rules.Cur_DuplicateCode;
import com.makco.smartfinance.user_interface.validation.currency_rules.Cur_EmptyCode;
import com.makco.smartfinance.user_interface.validation.currency_rules.Cur_NameLength;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

/**
 * Created by mcalancea on 2016-04-12.
 */
public class CurrencyRuleSet implements RuleSet<Currency>{

    @Override
    public List<Rule> getRuleSet() {
        List<Rule> rules = new ArrayList<>();
        rules.add(new Cur_EmptyCode());
        rules.add(new Cur_CodeLength());
        rules.add(new Cur_NameLength());
        rules.add(new Cur_DescLength());
        rules.add(new Cur_DuplicateCode());//DB call so last
        return new ArrayList<>(rules);
    }

    @Override
    public EnumSet<ErrorEnum> validate(Currency currency) {
        EnumSet<ErrorEnum> errors = EnumSet.noneOf(ErrorEnum.class);
        try {
            for (Rule rule : getRuleSet()) {
                errors.addAll(rule.validate(currency));
            }
        }catch (Exception e){
            DialogMessages.showExceptionAlert(e);
        }
        return errors;
    }
}
