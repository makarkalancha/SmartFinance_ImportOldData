package com.makco.smartfinance.user_interface.validation.rule_sets;

import com.makco.smartfinance.persistence.entity.AccountGroup;
import com.makco.smartfinance.user_interface.validation.ErrorEnum;
import com.makco.smartfinance.user_interface.validation.Rule;
import com.makco.smartfinance.user_interface.validation.RuleSet;
import com.makco.smartfinance.user_interface.validation.account_group.AccGr_DescLength;
import com.makco.smartfinance.user_interface.validation.account_group.AccGr_DuplicateName;
import com.makco.smartfinance.user_interface.validation.account_group.AccGr_EmptyAccountGroupType;
import com.makco.smartfinance.user_interface.validation.account_group.AccGr_EmptyName;
import com.makco.smartfinance.user_interface.validation.account_group.AccGr_NameLength;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

/**
 * Created by mcalancea on 2016-06-06.
 */
public class AccountGroupRuleSet implements RuleSet<AccountGroup>{

    @Override
    public List<Rule> getRuleSet() {
        List<Rule> rules = new ArrayList<>();
        rules.add(new AccGr_DescLength());
        rules.add(new AccGr_EmptyName());
        rules.add(new AccGr_EmptyAccountGroupType());
        rules.add(new AccGr_NameLength());
        rules.add(new AccGr_DuplicateName());//DB call so last
        return new ArrayList<>(rules);
    }

    @Override
    public EnumSet<ErrorEnum> validate(AccountGroup accountGroup) throws Exception{
        EnumSet<ErrorEnum> errors = EnumSet.noneOf(ErrorEnum.class);
        try {
            for (Rule rule : getRuleSet()) {
                errors.addAll(rule.validate(accountGroup));
            }
        }catch (Exception e){
            throw e;
        }
        return errors;
    }
}
