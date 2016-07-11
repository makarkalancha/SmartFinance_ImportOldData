package com.makco.smartfinance.user_interface.validation.rule_sets;

import com.makco.smartfinance.persistence.entity.Organization;
import com.makco.smartfinance.user_interface.validation.ErrorEnum;
import com.makco.smartfinance.user_interface.validation.Rule;
import com.makco.smartfinance.user_interface.validation.RuleSet;
import com.makco.smartfinance.user_interface.validation.organization_rules.ORG_DescLength;
import com.makco.smartfinance.user_interface.validation.organization_rules.ORG_DuplicateName;
import com.makco.smartfinance.user_interface.validation.organization_rules.ORG_EmptyName;
import com.makco.smartfinance.user_interface.validation.organization_rules.ORG_NameLength;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

/**
 * Created by Makar Kalancha on 2016-04-22.
 */
public class OrganizationRuleSet implements RuleSet<Organization>{

    @Override
    public List<Rule> getRuleSet() {
        List<Rule> rules = new ArrayList<>();
        rules.add(new ORG_EmptyName());
        rules.add(new ORG_NameLength());
        rules.add(new ORG_DescLength());
        rules.add(new ORG_DuplicateName());//DB call so last
        return new ArrayList<>(rules);
    }

    @Override
    public EnumSet<ErrorEnum> validate(Organization organization) throws Exception{
        EnumSet<ErrorEnum> errors = EnumSet.noneOf(ErrorEnum.class);
        try {
            for (Rule rule : getRuleSet()) {
                errors.addAll(rule.validate(organization));
            }
        }catch (Exception e){
            throw e;
        }
        return errors;
    }
}
