package com.makco.smartfinance.persistence.entity;

import com.makco.smartfinance.constants.DataBaseConstants;

/**
 * Created by Makar Kalancha on 2016-05-25.
 */
public class CategoryGroupFactory {

    public CategoryGroup getCategoryGroup(DataBaseConstants.CATEGORY_GROUP_TYPE type, String name, String description) throws Exception {
        CategoryGroup categoryGroupResult = null;
        if (type != null) {
            switch (type) {
                case CREDIT:
                    categoryGroupResult = new CategoryGroupCredit(name, description);
                    break;
                case DEBIT:
                    categoryGroupResult = new CategoryGroupDebit(name, description);
                    break;
                default:
                    throw new Exception("This type of Category Group is not supported!");
            }
        }
        return categoryGroupResult;
    }

}
