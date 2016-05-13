package com.makco.smartfinance.services;

import com.makco.smartfinance.persistence.entity.CategoryGroup;
import com.makco.smartfinance.persistence.entity.CategoryGroupCredit;
import com.makco.smartfinance.persistence.entity.CategoryGroupDebit;
import com.makco.smartfinance.persistence.entity.Currency;
import com.makco.smartfinance.user_interface.validation.ErrorEnum;

import java.util.EnumSet;
import java.util.List;

/**
 * Created by mcalancea on 2016-05-13.
 */
public interface CategoryGroupService {
    List<CategoryGroup> categoryGroupList(boolean initializeCategories) throws Exception;
    <T extends CategoryGroup> List<T> categoryGroupByType(Class<T> type, boolean initializeCategories) throws Exception;
    void saveOrUpdateCategoryGroup(CategoryGroup categoryGroup) throws Exception;

    EnumSet<ErrorEnum> validate(CategoryGroup categoryGroup) throws Exception;
}
