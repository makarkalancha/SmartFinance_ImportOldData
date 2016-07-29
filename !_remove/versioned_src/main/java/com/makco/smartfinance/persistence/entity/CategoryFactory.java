package com.makco.smartfinance.persistence.entity;

/**
 * Created by Makar Kalancha on 2016-06-02.
 */
public class CategoryFactory {

    public Category getCategory(CategoryGroup categoryGroup, String name, String description) throws Exception {
        Category categoryResult = null;
        if (categoryGroup != null && categoryGroup.getCategoryGroupType() != null) {
            switch (categoryGroup.getCategoryGroupType()) {
                case CREDIT:
                    categoryResult = new CategoryCredit(categoryGroup, name, description);
                    break;
                case DEBIT:
                    categoryResult = new CategoryDebit(categoryGroup, name, description);
                    break;
                default:
                    throw new Exception("This type of Category Group is not supported!");
            }
        }
        return categoryResult;
    }

}
