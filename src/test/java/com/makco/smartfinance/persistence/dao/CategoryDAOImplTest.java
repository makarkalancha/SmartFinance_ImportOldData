package com.makco.smartfinance.persistence.dao;

import com.makco.smartfinance.constants.DataBaseConstants;
import com.makco.smartfinance.persistence.dao.dao_implementations.CategoryDAOImplForTest;
import com.makco.smartfinance.persistence.dao.dao_implementations.CategoryGroupDAOImplForTest;
import com.makco.smartfinance.persistence.entity.Category;
import com.makco.smartfinance.persistence.entity.CategoryCredit;
import com.makco.smartfinance.persistence.entity.CategoryDebit;
import com.makco.smartfinance.persistence.entity.CategoryGroup;
import com.makco.smartfinance.persistence.entity.CategoryGroupCredit;
import com.makco.smartfinance.persistence.entity.CategoryGroupDebit;
import com.makco.smartfinance.persistence.utils.TestPersistenceSession;
import com.makco.smartfinance.utils.RandomWithinRange;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * User: Makar Kalancha
 * Date: 30/04/2016
 * Time: 23:52
 */
public class CategoryDAOImplTest {
    private static final Logger LOG = LogManager.getLogger(CategoryDAOImplTest.class);
    private static final String categoryGroupName = "categoryGroup_name";
    private static final String categoryGroupDesc = "categoryGroup_description";
    private static final String categoryDesc = "category_description";

    private static int MIN = 1;
    private static int MAX = 1_000_000;
    private static RandomWithinRange randomWithinRange = new RandomWithinRange(MIN, MAX);

    private CategoryGroupDAOImplForTest categoryGroupDAOImplForTest = new CategoryGroupDAOImplForTest();
    private CategoryDAOImplForTest categoryDAOImplForTest = new CategoryDAOImplForTest();

    @Test
    public void test_11_saveOrUpdateCategoryDebit() throws Exception {
        int randomInt = randomWithinRange.getRandom();
        String name = categoryGroupName + randomInt;
        CategoryGroup categoryGroup = new CategoryGroupDebit(name, categoryGroupDesc);
        categoryGroupDAOImplForTest.saveOrUpdateCategoryGroup(categoryGroup);

        Category category = new CategoryDebit(categoryGroup, name, categoryDesc);
        categoryDAOImplForTest.saveOrUpdateCategory(category);

        LOG.debug("category: " + category);
        assertEquals(true, category.getId() != null);
        assertEquals(DataBaseConstants.CATEGORY_GROUP_TYPE.DEBIT.getDiscriminator(), category.getCategoryGroupType());
        assertEquals(name, category.getName());
        assertEquals(categoryDesc, category.getDescription());
        assertEquals(true, category.getCreatedOn() != null);
        assertEquals(true, category.getUpdatedOn() != null);

        CategoryGroup categoryGroupInserted = category.getCategoryGroup();
        LOG.debug("categoryGroup: " + categoryGroupInserted);
        assertEquals(true, categoryGroupInserted.getId() != null);
        assertEquals(DataBaseConstants.CATEGORY_GROUP_TYPE.DEBIT.getDiscriminator(), categoryGroupInserted.getCategoryGroupType());
        assertEquals(name, categoryGroupInserted.getName());
        assertEquals(categoryGroupDesc, categoryGroupInserted.getDescription());
        assertEquals(true, categoryGroupInserted.getCreatedOn() != null);
        assertEquals(true, categoryGroupInserted.getUpdatedOn() != null);
    }

    @Test
    //TODO test_12_saveCategoryWithCategoryGroup
    public void test_12_saveCategoryWithCategoryGroup() throws Exception {

    }

    @Test
    //TODO test_13_saveCategoriesWithCategoryGroup
    public void test_13_saveCategoriesWithCategoryGroup() throws Exception {

    }

    @Test
    public void test_21_updateCategory() throws Exception {
        int randomInt = randomWithinRange.getRandom();

        Session session = TestPersistenceSession.openSession();
        session.beginTransaction();
        String name1 = categoryGroupName + "_1_" + randomInt;
        CategoryGroup categoryGroup1 = new CategoryGroupDebit(name1, categoryGroupDesc);
        categoryGroupDAOImplForTest.saveOrUpdateCategoryGroup(categoryGroup1);

        Category category = new CategoryDebit(categoryGroup1, name1, categoryDesc);
        categoryDAOImplForTest.saveOrUpdateCategory(category);

        String name2 = categoryGroupName + "_2_" + randomInt;
        CategoryGroup categoryGroup2 = new CategoryGroupDebit(name2, categoryGroupDesc);
        categoryGroupDAOImplForTest.saveOrUpdateCategoryGroup(categoryGroup2);

        session.getTransaction().commit();
        session.close();

        long categoryId = category.getId();

        Category categoryJustCreated = categoryDAOImplForTest.getCategoryById(categoryId);
        LOG.debug("categoryGroup1: " + categoryGroup1);
        LOG.debug("categoryGroup2: " + categoryGroup2);
        LOG.debug("categoryJustCreated: " + categoryJustCreated);

        String newName = categoryJustCreated.getName() + "_changed_v" + randomInt;

        newName = (newName.length() > DataBaseConstants.CAT_NAME_MAX_LGTH) ? newName.substring(newName.length() - DataBaseConstants.CAT_NAME_MAX_LGTH) : newName;
        categoryJustCreated.setName(newName);
        categoryJustCreated.setCategoryGroup(categoryGroup2);
        categoryDAOImplForTest.saveOrUpdateCategory(categoryJustCreated);

        Category categoryJustUpdated = categoryDAOImplForTest.getCategoryById(categoryId);
        LOG.debug("categoryJustUpdated: " + categoryJustUpdated);
        assertEquals(true, categoryJustUpdated.getId() != null);
        assertEquals(true, categoryJustUpdated.getId().equals(categoryId));
        assertEquals(DataBaseConstants.CATEGORY_GROUP_TYPE.DEBIT.getDiscriminator(), categoryJustUpdated.getCategoryGroupType());
        assertEquals(newName, categoryJustUpdated.getName());
        assertEquals(true, categoryJustUpdated.getCategoryGroup().equals(categoryGroup2));
        assertEquals(true, categoryJustUpdated.getCreatedOn() != null);
        assertEquals(true, categoryJustUpdated.getUpdatedOn() != null);
        assertEquals(true, !categoryJustUpdated.getUpdatedOn().equals(categoryJustUpdated.getCreatedOn()));

    }

    @Test
    public void test_31_removeCategoryFromCategoryGroup() throws Exception {
        int randomInt = randomWithinRange.getRandom();
        int categoriesQty = 5;
        CategoryGroup categoryGroupCredit = new CategoryGroupCredit("catGr to del " + randomInt,
                "category group to delete " + randomInt);

        List<Category> categories = new ArrayList<>();
        //put service: putting category_group in category and category in category_group
        for (int i = 0; i < categoriesQty; i++) {
            Category category = new CategoryCredit(categoryGroupCredit, "cat cr " + i + "_v" + randomInt,
                    "credit category #" + i + " 'description'");
            categories.add(category);
        }
        categoryGroupCredit.setCategories(categories);

        categoryGroupDAOImplForTest.saveOrUpdateCategoryGroup(categoryGroupCredit);
        LOG.debug(">>>category group to delete: " + categoryGroupCredit);
        LOG.debug(">>>categories to delete: " + categories);

        Category categoryToDelete = categories.get(0);
        categoryDAOImplForTest.removeCategory(categoryToDelete.getId());
        ////////////////////////////1//////////////////////////////////
        CategoryGroup categoryGroup1 = categoryGroupDAOImplForTest.getCategoryGroupById(categoryGroupCredit.getId(), false);
        assertEquals(true, categoryGroup1 != null);
        for (Category category : categories) {
            Category categoryDeleted = categoryDAOImplForTest.getCategoryById(category.getId());
            if (category.getId().equals(categoryToDelete.getId())) {
                assertEquals(true, categoryDeleted == null);
            } else {
                assertEquals(true, categoryDeleted != null);
            }
        }
        ////////////////////////////2//////////////////////////////////
        CategoryGroup categoryGroup2 = categoryGroupDAOImplForTest.getCategoryGroupById(categoryGroupCredit.getId(), true);
        assertEquals(true, categoryGroup2 != null);
        assertEquals(true, !categoryGroup2.getCategories().contains(categoryToDelete));
        assertEquals(true, categoryGroup2.getCategories().size() == (categoriesQty - 1));
    }

    @Test
    //TODO test_41_seleteAllCategories
    public void test_41_seleteAllCategories() throws Exception {
    }

    @Test
    //TODO test_42_seleteCategoryById
    public void test_42_seleteCategoryById() throws Exception {

    }

    @Test
    public void test_43_seleteCategoryByNameAndItsCategoryGroup() throws Exception {
        int randomInt = randomWithinRange.getRandom();
        CategoryGroup categoryGroupCredit = new CategoryGroupCredit("cr catGr to sel " + randomInt,
                "credit category group to select " + randomInt);
        List<Category> creditCategories = new ArrayList<>();
        //put service: putting category_group in category and category in category_group
        String categoryName = "cat cr 47_v" + randomInt;
        Category category = new CategoryCredit(categoryGroupCredit, categoryName,
                "credit category test #47 'description'");
        creditCategories.add(category);
        categoryGroupCredit.setCategories(creditCategories);
        categoryGroupDAOImplForTest.saveOrUpdateCategoryGroup(categoryGroupCredit);

        LOG.debug(">>>category group to delete: " + categoryGroupCredit);
        LOG.debug(">>>categories to delete: " + creditCategories);

        List<Category> categoryByNameList = categoryDAOImplForTest.getCategoryByName(categoryName);
        LOG.debug(">>>categoryByNameList: " + categoryByNameList);
        assertEquals(true, categoryByNameList.size() > 0);
        for (Category cat : categoryByNameList) {
            assertEquals(true, categoryName.equals(cat.getName()));
        }
    }

    @Test
    //TODO test_44_seleteAllDebitCategories
    public void test_44_seleteAllDebitCategories() throws Exception {

    }

    @Test
    //TODO test_45_seleteAllCreditCategories
    public void test_45_seleteAllCreditCategories() throws Exception {

    }

    @Test
    public void test_46_seleteCategoryByType() throws Exception {
        int randomInt = randomWithinRange.getRandom();
        int categoriesQty = 3;
        CategoryGroup categoryGroupDebit = new CategoryGroupDebit("dt catGr to sel " + randomInt,
                "debit category group to select " + randomInt);
        List<Category> debitCategories = new ArrayList<>();
        //put service: putting category_group in category and category in category_group
        for(int i = 0 ; i < categoriesQty;i++) {
            Category category = new CategoryDebit(categoryGroupDebit, "cat dt " + i + "_v" + randomInt,
                    "debit category #" + i + " 'description'");
            debitCategories.add(category);
        }
        categoryGroupDebit.setCategories(debitCategories);
        categoryGroupDAOImplForTest.saveOrUpdateCategoryGroup(categoryGroupDebit);

        LOG.debug(">>>category group to delete: " + categoryGroupDebit);
        LOG.debug(">>>categories to delete: " + debitCategories);

        List<CategoryDebit> categoryDebits = categoryDAOImplForTest.categoryByType(CategoryDebit.class);
        LOG.debug(">>>categoryDebits: " + categoryDebits);
        assertEquals(true, categoryDebits.size() > 0);

        LOG.debug(">>>qtyCategory: " + categoryDebits.size());
        assertEquals(true, categoryDebits.size() > 0);
    }

    @Test
    public void test_47_seleteCategoryList() throws Exception {
//        int randomInt = randomWithinRange.getRandom();
//        int categoriesQty = 3;
//        String categoryGroupName = "categoryGroup nameAndType " + randomInt;
//        CategoryGroup categoryGroupDebit = new CategoryGroupDebit(categoryGroupName,
//                "debit category group to select " + randomInt);
//        List<Category> debitCategories = new ArrayList<>();
//        //put service: putting category_group in category and category in category_group
//        for(int i = 0 ; i < categoriesQty;i++) {
//            Category category = new CategoryDebit(categoryGroupDebit, "cat db same name " + i + "_v" + randomInt,
//                    "credit category #" + i + " 'description'");
//            debitCategories.add(category);
//        }
//        categoryGroupDebit.setCategories(debitCategories);
//        LOG.debug(">>>category group name and type: " + categoryGroupDebit);
//        LOG.debug(">>>categories of category group: " + debitCategories);
//        categoryGroupDAOImplForTest.saveOrUpdateCategoryGroup(categoryGroupDebit);

        //byName return list as it might be debit or credit and return categories
        List<CategoryGroup> categoryGroupByNameAndTypeResult = categoryGroupDAOImplForTest
                .categoryGroupListWithLeftJoinFetch(true);

        LOG.debug(">>>category group select by id: " + categoryGroupByNameAndTypeResult);

//        assertEquals(true, categoryGroupByNameAndTypeResult.getName().equals(categoryGroupName));
//        assertEquals(true, categoryGroupByNameAndTypeResult.getCategoryGroupType().equals(DataBaseConstants.CATEGORY_GROUP_TYPE.DEBIT.getDiscriminator()));
//
//        LOG.debug(">>>categoryGroupList: " + categoryGroupByNameAndTypeResult.getCategories());
//        assertEquals(true, categoryGroupByNameAndTypeResult.getCategories().size() > 0);
    }
}
