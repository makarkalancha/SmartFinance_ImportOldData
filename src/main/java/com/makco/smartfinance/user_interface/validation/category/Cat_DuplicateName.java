package com.makco.smartfinance.user_interface.validation.category;

import com.makco.smartfinance.persistence.entity.Category;
import com.makco.smartfinance.services.CategoryService;
import com.makco.smartfinance.services.CategoryServiceImpl;
import com.makco.smartfinance.user_interface.validation.ErrorEnum;
import com.makco.smartfinance.user_interface.validation.Rule;

import java.util.EnumSet;
import java.util.List;

/**
 * Created by mcalancea on 2016-04-08.
 */
public class Cat_DuplicateName implements Rule<Category> {
    private CategoryService categoryService = new CategoryServiceImpl();
    @Override
    public EnumSet<ErrorEnum> validate(Category category) throws Exception{
        EnumSet<ErrorEnum> errors = EnumSet.noneOf(ErrorEnum.class);
        try {
            if (category != null) {
                List<Category> categoryByName = categoryService.getCategoryByName(category.getName());
                categoryByName.removeIf(c -> c.getId().equals(category.getId()));
                categoryByName.removeIf(c -> !c.getCategoryGroupType().equals(category.getCategoryGroupType()));
                if (!categoryByName.isEmpty()) {
                    errors.add(ErrorEnum.Cat_NAME_DUPLICATE);
                }
            }
        }catch (Exception e){
            throw e;
        }
        return errors;
    }
}
