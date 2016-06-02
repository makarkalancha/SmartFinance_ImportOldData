package com.makco.smartfinance.user_interface.validation.rule_sets;

import com.makco.smartfinance.persistence.entity.Category;
import com.makco.smartfinance.persistence.entity.CategoryGroup;
import com.makco.smartfinance.user_interface.validation.ErrorEnum;
import com.makco.smartfinance.user_interface.validation.Rule;
import com.makco.smartfinance.user_interface.validation.RuleSet;
import com.makco.smartfinance.user_interface.validation.category_group.CatGr_DescLength;
import com.makco.smartfinance.user_interface.validation.category_group.CatGr_DuplicateName;
import com.makco.smartfinance.user_interface.validation.category_group.CatGr_EmptyCategoryGroupType;
import com.makco.smartfinance.user_interface.validation.category_group.CatGr_EmptyName;
import com.makco.smartfinance.user_interface.validation.category_group.CatGr_NameLength;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

/**
 * Created by mcalancea on 2016-06-02.
 */
public class CategoryRuleSet implements RuleSet<Category>{

    @Override
    public List<Rule> getRuleSet() {
        List<Rule> rules = new ArrayList<>();
        rules.add(new Cat_DescLength());
        rules.add(new Cat_EmptyName());
        rules.add(new Cat_EmptyCategoryGroup());
        rules.add(new Cat_NameLength());
        rules.add(new Cat_DuplicateName());//DB call so last
        return new ArrayList<>(rules);
    }

    @Override
    public EnumSet<ErrorEnum> validate(Category category) throws Exception{
        EnumSet<ErrorEnum> errors = EnumSet.noneOf(ErrorEnum.class);
        try {
            for (Rule rule : getRuleSet()) {
                errors.addAll(rule.validate(category));
            }
        }catch (Exception e){
            throw e;
        }
        return errors;
    }
}