package com.makco.smartfinance.user_interface.validation.category_group;

import com.makco.smartfinance.persistence.entity.CategoryGroup;
import com.makco.smartfinance.user_interface.utility_screens.DialogMessages;
import com.makco.smartfinance.user_interface.validation.ErrorEnum;
import com.makco.smartfinance.user_interface.validation.Rule;
import org.apache.commons.lang3.StringUtils;

import java.util.EnumSet;

/**
 * Created by mcalancea on 2016-04-08.
 */
public class CatGr_EmptyName implements Rule<CategoryGroup> {

    @Override
    public EnumSet<ErrorEnum> validate(CategoryGroup categoryGroup) {
        EnumSet<ErrorEnum> errors = EnumSet.noneOf(ErrorEnum.class);
        try {
            if (StringUtils.isBlank(categoryGroup.getName())) {
                errors.add(ErrorEnum.CatGr_NAME_NULL);
            }
        }catch (Exception e){
            DialogMessages.showExceptionAlert(e);
        }
        return errors;
    }
}
