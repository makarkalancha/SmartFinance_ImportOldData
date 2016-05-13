package com.makco.smartfinance.user_interface.validation.category_group;

import com.makco.smartfinance.constants.DataBaseConstants;
import com.makco.smartfinance.persistence.entity.CategoryGroup;
import com.makco.smartfinance.persistence.entity.FamilyMember;
import com.makco.smartfinance.user_interface.utility_screens.DialogMessages;
import com.makco.smartfinance.user_interface.validation.ErrorEnum;
import com.makco.smartfinance.user_interface.validation.Rule;

import java.util.EnumSet;

/**
 * Created by mcalancea on 2016-04-08.
 */
public class CatGr_DescLength implements Rule<CategoryGroup> {

    @Override
    public EnumSet<ErrorEnum> validate(CategoryGroup categoryGroup) {
        EnumSet<ErrorEnum> errors = EnumSet.noneOf(ErrorEnum.class);
        try {
            if (categoryGroup.getDescription().length() > DataBaseConstants.CG_DESCRIPTION_MAX_LGTH) {
                errors.add(ErrorEnum.CatGr_DESC_LGTH);
            }
        }catch (Exception e){
            DialogMessages.showExceptionAlert(e);
        }
        return errors;
    }
}
