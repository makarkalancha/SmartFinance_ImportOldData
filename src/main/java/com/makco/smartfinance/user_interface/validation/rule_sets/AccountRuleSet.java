package com.makco.smartfinance.user_interface.validation.rule_sets;

import com.makco.smartfinance.persistence.entity.Account;
import com.makco.smartfinance.user_interface.validation.ErrorEnum;
import com.makco.smartfinance.user_interface.validation.Rule;
import com.makco.smartfinance.user_interface.validation.RuleSet;
import com.makco.smartfinance.user_interface.validation.account.Acc_DescLength;
import com.makco.smartfinance.user_interface.validation.account.Acc_DuplicateName;
import com.makco.smartfinance.user_interface.validation.account.Acc_EmptyName;
import com.makco.smartfinance.user_interface.validation.account.Acc_NameLength;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

/**
 * Created by mcalancea on 2016-06-06.
 */
public class AccountRuleSet implements RuleSet<Account>{

    @Override
    public List<Rule> getRuleSet() {
        List<Rule> rules = new ArrayList<>();
        rules.add(new Acc_DescLength());
        rules.add(new Acc_EmptyName());
//        rules.add(new Acc_EmptyAccountGroup());
        rules.add(new Acc_NameLength());
        rules.add(new Acc_DuplicateName());//DB call so last
        return new ArrayList<>(rules);
    }

    @Override
    public EnumSet<ErrorEnum> validate(Account account) throws Exception{
        EnumSet<ErrorEnum> errors = EnumSet.noneOf(ErrorEnum.class);
        try {
            for (Rule rule : getRuleSet()) {
                errors.addAll(rule.validate(account));
            }
        }catch (Exception e){
            throw e;
        }
        return errors;
    }
}
