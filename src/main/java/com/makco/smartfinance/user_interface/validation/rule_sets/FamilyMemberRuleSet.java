package com.makco.smartfinance.user_interface.validation.rule_sets;

import com.makco.smartfinance.persistence.entity.FamilyMember;
import com.makco.smartfinance.user_interface.validation.ErrorEnum;
import com.makco.smartfinance.user_interface.validation.Rule;
import com.makco.smartfinance.user_interface.validation.RuleSet;
import com.makco.smartfinance.user_interface.validation.family_member_rules.FM_DescLength;
import com.makco.smartfinance.user_interface.validation.family_member_rules.FM_DuplicateName;
import com.makco.smartfinance.user_interface.validation.family_member_rules.FM_EmptyName;
import com.makco.smartfinance.user_interface.validation.family_member_rules.FM_NameLength;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

/**
 * Created by mcalancea on 2016-04-08.
 */
public class FamilyMemberRuleSet implements RuleSet<FamilyMember>{

    @Override
    public List<Rule> getRuleSet() {
        List<Rule> rules = new ArrayList<>();
        rules.add(new FM_EmptyName());
        rules.add(new FM_NameLength());
        rules.add(new FM_DescLength());
        rules.add(new FM_DuplicateName());//DB call so last
        return new ArrayList<>(rules);
    }

    @Override
    public EnumSet<ErrorEnum> validate(FamilyMember familyMember) throws Exception {
        EnumSet<ErrorEnum> errors = EnumSet.noneOf(ErrorEnum.class);
        try {
            for (Rule rule : getRuleSet()) {
                errors.addAll(rule.validate(familyMember));
            }
        }catch (Exception e){
            throw e;
        }
        return errors;
    }
}
