package com.makco.smartfinance.user_interface.validation.category;

import com.makco.smartfinance.constants.DataBaseConstants;
import com.makco.smartfinance.persistence.entity.Category;
import com.makco.smartfinance.user_interface.validation.ErrorEnum;
import com.makco.smartfinance.user_interface.validation.Rule;

import java.util.EnumSet;

/**
 * Created by mcalancea on 2016-04-08.
 */
public class Cat_DescLength implements Rule<Category> {

    @Override
    public EnumSet<ErrorEnum> validate(Category category) throws Exception{
        EnumSet<ErrorEnum> errors = EnumSet.noneOf(ErrorEnum.class);
        try {
            if (category != null && category.getDescription().length() > DataBaseConstants.CAT_DESCRIPTION_MAX_LGTH) {
                errors.add(ErrorEnum.Cat_DESC_LGTH);
            }
        }catch (Exception e){
            throw e;
        }
        return errors;
    }
}
