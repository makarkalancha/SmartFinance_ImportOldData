package com.makco.smartfinance.user_interface.validation.rule_sets;

import com.makco.smartfinance.persistence.entity.Tax;
import com.makco.smartfinance.user_interface.validation.ErrorEnum;
import com.makco.smartfinance.user_interface.validation.Rule;
import com.makco.smartfinance.user_interface.validation.RuleSet;
import com.makco.smartfinance.user_interface.validation.tax_rules.TAX_DescLength;
import com.makco.smartfinance.user_interface.validation.tax_rules.TAX_DuplicateName;
import com.makco.smartfinance.user_interface.validation.tax_rules.TAX_EmptyName;
import com.makco.smartfinance.user_interface.validation.tax_rules.TAX_NameLength;
import com.makco.smartfinance.user_interface.validation.tax_rules.TAX_StartLTEQEndDate;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

/**
 * Created by Makar Kalancha on 2016-04-22.
 */
public class TaxRuleSet implements RuleSet<Tax>{

    @Override
    public List<Rule> getRuleSet() {
        List<Rule> rules = new ArrayList<>();
        rules.add(new TAX_EmptyName());
        rules.add(new TAX_NameLength());
        rules.add(new TAX_DescLength());
        rules.add(new TAX_StartLTEQEndDate());
        rules.add(new TAX_DuplicateName());//DB call so last
        return new ArrayList<>(rules);
    }

    @Override
    public EnumSet<ErrorEnum> validate(Tax tax) throws Exception{
        EnumSet<ErrorEnum> errors = EnumSet.noneOf(ErrorEnum.class);
        try {
            for (Rule rule : getRuleSet()) {
                errors.addAll(rule.validate(tax));
            }
        }catch (Exception e){
            throw e;
        }
        return errors;
    }
}
