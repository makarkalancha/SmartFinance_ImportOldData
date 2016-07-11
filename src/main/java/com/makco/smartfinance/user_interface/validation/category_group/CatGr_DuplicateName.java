package com.makco.smartfinance.user_interface.validation.category_group;

import com.makco.smartfinance.persistence.entity.CategoryGroup;
import com.makco.smartfinance.services.CategoryGroupService;
import com.makco.smartfinance.services.CategoryGroupServiceImpl;
import com.makco.smartfinance.user_interface.validation.ErrorEnum;
import com.makco.smartfinance.user_interface.validation.Rule;

import java.util.EnumSet;
import java.util.List;

/**
 * Created by Makar Kalancha on 2016-04-08.
 */
public class CatGr_DuplicateName implements Rule<CategoryGroup> {
    private CategoryGroupService categoryGroupService = new CategoryGroupServiceImpl();
    @Override
    public EnumSet<ErrorEnum> validate(CategoryGroup categoryGroup) throws Exception{
        EnumSet<ErrorEnum> errors = EnumSet.noneOf(ErrorEnum.class);
        try {
            if (categoryGroup != null) {
                List<CategoryGroup> categoryGroupsByName = categoryGroupService.getCategoryGroupByName(categoryGroup.getName());
                categoryGroupsByName.removeIf(cg -> cg.getId().equals(categoryGroup.getId()));
                categoryGroupsByName.removeIf(cg -> !cg.getCategoryGroupType().equals(categoryGroup.getCategoryGroupType()));
                if (!categoryGroupsByName.isEmpty()) {
                    errors.add(ErrorEnum.CatGr_NAME_DUPLICATE);
                }
            }
        }catch (Exception e){
            throw e;
        }
        return errors;
    }
}
