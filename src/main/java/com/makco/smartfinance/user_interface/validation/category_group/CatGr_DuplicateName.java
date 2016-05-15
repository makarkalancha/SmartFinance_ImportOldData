package com.makco.smartfinance.user_interface.validation.category_group;

import com.makco.smartfinance.persistence.entity.CategoryGroup;
import com.makco.smartfinance.services.CategoryGroupService;
import com.makco.smartfinance.services.CategoryGroupServiceImpl;
import com.makco.smartfinance.user_interface.utility_screens.DialogMessages;
import com.makco.smartfinance.user_interface.validation.ErrorEnum;
import com.makco.smartfinance.user_interface.validation.Rule;

import java.util.EnumSet;
import java.util.List;

/**
 * Created by mcalancea on 2016-04-08.
 */
public class CatGr_DuplicateName implements Rule<CategoryGroup> {
    private CategoryGroupService categoryGroupService = new CategoryGroupServiceImpl();
    @Override
    public EnumSet<ErrorEnum> validate(CategoryGroup categoryGroup) {
        EnumSet<ErrorEnum> errors = EnumSet.noneOf(ErrorEnum.class);
        try {
            List<CategoryGroup> categoryGroupsByName = categoryGroupService.getCategoryGroupByName(categoryGroup.getName(), false);
            categoryGroupsByName.removeIf(f -> f.getId().equals(categoryGroup.getId()));
            if (!categoryGroupsByName.isEmpty()) {
                errors.add(ErrorEnum.CatGr_NAME_DUPLICATE);
            }
        }catch (Exception e){
            DialogMessages.showExceptionAlert(e);
        }
        return errors;
    }
}
