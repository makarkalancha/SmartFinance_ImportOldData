package com.makco.smartfinance.user_interface.validation.category_group;

import com.makco.smartfinance.persistence.entity.CategoryGroup;
import com.makco.smartfinance.user_interface.validation.ErrorEnum;
import com.makco.smartfinance.user_interface.validation.Rule;
import org.apache.commons.lang3.StringUtils;

import java.util.EnumSet;

/**
 * Created by Makar Kalancha on 2016-04-08.
 */
public class CatGr_EmptyCategoryGroupType implements Rule<CategoryGroup> {

    @Override
    public EnumSet<ErrorEnum> validate(CategoryGroup categoryGroup) throws Exception{
        EnumSet<ErrorEnum> errors = EnumSet.noneOf(ErrorEnum.class);
        try {
            if (categoryGroup == null || StringUtils.isBlank(categoryGroup.getCategoryGroupType().getDiscriminator())) {
                errors.add(ErrorEnum.CatGr_NULL_CG_TYPE);
            }
        }catch (Exception e){
            throw e;
        }
        return errors;
    }
}
