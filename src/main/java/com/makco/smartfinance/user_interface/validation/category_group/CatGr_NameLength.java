package com.makco.smartfinance.user_interface.validation.category_group;

import com.makco.smartfinance.constants.DataBaseConstants;
import com.makco.smartfinance.persistence.entity.CategoryGroup;
import com.makco.smartfinance.user_interface.validation.ErrorEnum;
import com.makco.smartfinance.user_interface.validation.Rule;

import java.util.EnumSet;

/**
 * Created by Makar Kalancha on 2016-04-08.
 */
public class CatGr_NameLength implements Rule<CategoryGroup> {

    @Override
    public EnumSet<ErrorEnum> validate(CategoryGroup categoryGroup) throws Exception{
        EnumSet<ErrorEnum> errors = EnumSet.noneOf(ErrorEnum.class);
        try {
            if (categoryGroup != null && categoryGroup.getName().length() > DataBaseConstants.CG_NAME_MAX_LGTH) {
                errors.add(ErrorEnum.CatGr_NAME_LGTH);
            }
        }catch (Exception e){
            throw e;
        }
        return errors;
    }
}
