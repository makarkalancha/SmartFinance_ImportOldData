package com.makco.smartfinance.user_interface.validation.category;

import com.makco.smartfinance.persistence.entity.Category;
import com.makco.smartfinance.user_interface.validation.ErrorEnum;
import com.makco.smartfinance.user_interface.validation.Rule;
import org.apache.commons.lang3.StringUtils;

import java.util.EnumSet;

/**
 * Created by mcalancea on 2016-04-08.
 */
public class Cat_EmptyName implements Rule<Category> {

    @Override
    public EnumSet<ErrorEnum> validate(Category category) throws Exception{
        EnumSet<ErrorEnum> errors = EnumSet.noneOf(ErrorEnum.class);
        try {
            if (category != null && StringUtils.isBlank(category.getName())) {
                errors.add(ErrorEnum.Cat_NULL_NAME);
            }
        }catch (Exception e){
            throw e;
        }
        return errors;
    }
}
