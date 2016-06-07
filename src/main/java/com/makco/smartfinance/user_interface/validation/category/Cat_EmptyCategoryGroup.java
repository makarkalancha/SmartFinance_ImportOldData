package com.makco.smartfinance.user_interface.validation.category;

import com.makco.smartfinance.persistence.entity.Category;
import com.makco.smartfinance.persistence.entity.CategoryGroup;
import com.makco.smartfinance.user_interface.validation.ErrorEnum;
import com.makco.smartfinance.user_interface.validation.Rule;
import org.apache.commons.lang3.StringUtils;

import java.util.EnumSet;

/**
 * Created by mcalancea on 2016-04-08.
 */
public class Cat_EmptyCategoryGroup implements Rule<Category> {

    /**
     * occurs when there's parent {category_group} at all in db
     * and you try to create child {category}
     * @param category
     * @return
     * @throws Exception
     */
    @Override
    public EnumSet<ErrorEnum> validate(Category category) throws Exception{
        EnumSet<ErrorEnum> errors = EnumSet.noneOf(ErrorEnum.class);
        try {
            if (category == null || category.getCategoryGroup() == null) {
                errors.add(ErrorEnum.Cat_CG_EMPTY);
            }
        }catch (Exception e){
            throw e;
        }
        return errors;
    }
}
