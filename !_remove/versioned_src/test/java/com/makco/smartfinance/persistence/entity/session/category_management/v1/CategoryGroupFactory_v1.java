package com.makco.smartfinance.persistence.entity.session.category_management.v1;

import com.makco.smartfinance.constants.DataBaseConstants;

/**
 * Created by Makar Kalancha on 2016-05-25.
 */
public class CategoryGroupFactory_v1 {

    public CategoryGroup_v1 getCategoryGroup(DataBaseConstants.CATEGORY_GROUP_TYPE type, String name, String description) throws Exception {
        CategoryGroup_v1 categoryGroupResult = null;
        if (type != null) {
            switch (type) {
                case CREDIT:
                    categoryGroupResult = new CategoryGroupCredit_v1(name, description);
                    break;
                case DEBIT:
                    categoryGroupResult = new CategoryGroupDebit_v1(name, description);
                    break;
                default:
                    throw new Exception("This type of Category Group is not supported!");
            }
        }
        return categoryGroupResult;
    }

}
