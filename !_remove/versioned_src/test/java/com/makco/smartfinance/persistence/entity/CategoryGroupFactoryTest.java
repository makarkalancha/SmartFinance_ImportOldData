package com.makco.smartfinance.persistence.entity;

import com.makco.smartfinance.constants.DataBaseConstants;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Created by Makar Kalancha on 2016-05-25.
 */
public class CategoryGroupFactoryTest {

    private CategoryGroupFactory categoryGroupFactory = new CategoryGroupFactory();
    private String name = "any name";
    private String description = "any description";

    @Before
    public void setUp() throws Exception {

    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void testGetCategoryGroup_withTypeNull() throws Exception {
        DataBaseConstants.CATEGORY_GROUP_TYPE type = null;
        CategoryGroup actualResult = categoryGroupFactory.getCategoryGroup(type, name, description);

        assertEquals(null, actualResult);
    }

    @Test
    public void testGetCategoryGroup_withTypeDebit() throws Exception {
        DataBaseConstants.CATEGORY_GROUP_TYPE type = DataBaseConstants.CATEGORY_GROUP_TYPE.DEBIT;
        CategoryGroup actualResult = categoryGroupFactory.getCategoryGroup(type, name, description);

        assertEquals(true, actualResult instanceof CategoryGroupDebit);
    }

    @Test
    public void testGetCategoryGroup_withTypeCredit() throws Exception {
        DataBaseConstants.CATEGORY_GROUP_TYPE type = DataBaseConstants.CATEGORY_GROUP_TYPE.CREDIT;
        CategoryGroup actualResult = categoryGroupFactory.getCategoryGroup(type, name, description);

        assertEquals(true, actualResult instanceof CategoryGroupCredit);
    }
}