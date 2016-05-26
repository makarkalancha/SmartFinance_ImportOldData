package com.makco.smartfinance.persistence.dao;

import com.makco.smartfinance.constants.DataBaseConstants;
import com.makco.smartfinance.persistence.dao.dao_implementations.CategoryDAOImplForTest;
import com.makco.smartfinance.persistence.dao.dao_implementations.CategoryGroupDAOImplForTest;
import com.makco.smartfinance.persistence.entity.session.category_management.v1.CategoryCredit_v1;
import com.makco.smartfinance.persistence.entity.session.category_management.v1.CategoryDebit_v1;
import com.makco.smartfinance.persistence.entity.session.category_management.v1.CategoryGroupCredit_v1;
import com.makco.smartfinance.persistence.entity.session.category_management.v1.CategoryGroupDebit_v1;
import com.makco.smartfinance.persistence.entity.session.category_management.v1.CategoryGroup_v1;
import com.makco.smartfinance.persistence.entity.session.category_management.v1.Category_v1;
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
    public void test_11_saveOrUpdateCategoryDebit_v1() throws Exception {
        int randomInt = randomWithinRange.getRandom();
        String name = categoryGroupName + randomInt;
        CategoryGroup_v1 categoryGroup = new CategoryGroupDebit_v1(name, categoryGroupDesc);
        categoryGroupDAOImplForTest.saveOrUpdateCategoryGroup_v1(categoryGroup);

        Category_v1 category = new CategoryDebit_v1(categoryGroup, name, categoryDesc);
        categoryDAOImplForTest.saveOrUpdateCategory(category);

        LOG.debug("category: " + category);
        assertEquals(true, category.getId() != null);
        assertEquals(DataBaseConstants.CATEGORY_GROUP_TYPE.DEBIT.getDiscriminator(), category.getCategoryGroupType());
        assertEquals(name, category.getName());
        assertEquals(categoryDesc, category.getDescription());
        assertEquals(true, category.getCreatedOn() != null);
        assertEquals(true, category.getUpdatedOn() != null);

        CategoryGroup_v1 categoryGroupInserted = category.getCategoryGroup();
        LOG.debug("categoryGroup: " + categoryGroupInserted);
        assertEquals(true, categoryGroupInserted.getId() != null);
        assertEquals(DataBaseConstants.CATEGORY_GROUP_TYPE.DEBIT.getDiscriminator(), categoryGroupInserted.getCategoryGroupType());
        assertEquals(name, categoryGroupInserted.getName());
        assertEquals(categoryGroupDesc, categoryGroupInserted.getDescription());
        assertEquals(true, categoryGroupInserted.getCreatedOn() != null);
        assertEquals(true, categoryGroupInserted.getUpdatedOn() != null);
    }

    @Test
    public void test_12_saveCategoryWithCategoryGroup() throws Exception {
        //see CategoryGroupDAOImplTest.test_12_saveCategoryGroupWithCategories
    }

    @Test
    public void test_13_saveCategoriesWithCategoryGroup() throws Exception {
        //see CategoryGroupDAOImplTest.test_12_saveCategoryGroupWithCategories
    }

    @Test
    public void test_21_updateCategory() throws Exception {
        int randomInt = randomWithinRange.getRandom();

        Session session = TestPersistenceSession.openSession();
        session.beginTransaction();
        String name1 = categoryGroupName + "_1_" + randomInt;
        CategoryGroup_v1 categoryGroup1 = new CategoryGroupDebit_v1(name1, categoryGroupDesc);
        categoryGroupDAOImplForTest.saveOrUpdateCategoryGroup_v1(categoryGroup1);

        Category_v1 category = new CategoryDebit_v1(categoryGroup1, name1, categoryDesc);
        categoryDAOImplForTest.saveOrUpdateCategory(category);

        String name2 = categoryGroupName + "_2_" + randomInt;
        CategoryGroup_v1 categoryGroup2 = new CategoryGroupDebit_v1(name2, categoryGroupDesc);
        categoryGroupDAOImplForTest.saveOrUpdateCategoryGroup_v1(categoryGroup2);

        session.getTransaction().commit();
        session.close();

        long categoryId = category.getId();

        Category_v1 categoryJustCreated = categoryDAOImplForTest.getCategoryById(categoryId);
        LOG.debug("categoryGroup1: " + categoryGroup1);
        LOG.debug("categoryGroup2: " + categoryGroup2);
        LOG.debug("categoryJustCreated: " + categoryJustCreated);

        String newName = categoryJustCreated.getName() + "_changed_v" + randomInt;

        newName = (newName.length() > DataBaseConstants.CAT_NAME_MAX_LGTH) ? newName.substring(newName.length() - DataBaseConstants.CAT_NAME_MAX_LGTH) : newName;
        categoryJustCreated.setName(newName);
        categoryJustCreated.setCategoryGroup(categoryGroup2);
        categoryDAOImplForTest.saveOrUpdateCategory(categoryJustCreated);

        Category_v1 categoryJustUpdated = categoryDAOImplForTest.getCategoryById(categoryId);
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
        CategoryGroup_v1 categoryGroupCredit = new CategoryGroupCredit_v1("catGr to del " + randomInt,
                "category group to delete " + randomInt);

        List<Category_v1> categories = new ArrayList<>();
        //put service: putting category_group in category and category in category_group
        for (int i = 0; i < categoriesQty; i++) {
            Category_v1 category = new CategoryCredit_v1(categoryGroupCredit, "cat cr " + i + "_v" + randomInt,
                    "credit category #" + i + " 'description'");
            categories.add(category);
        }
        categoryGroupCredit.setCategories(categories);

        categoryGroupDAOImplForTest.saveOrUpdateCategoryGroup_v1(categoryGroupCredit);
        LOG.debug(">>>category group to delete: " + categoryGroupCredit);
        LOG.debug(">>>categories to delete: " + categories);

        Category_v1 categoryToDelete = categories.get(0);
        categoryDAOImplForTest.removeCategory(categoryToDelete.getId());
        ////////////////////////////1//////////////////////////////////
        CategoryGroup_v1 categoryGroup1 = categoryGroupDAOImplForTest.getCategoryGroup_v1ById(categoryGroupCredit.getId(), false);
        assertEquals(true, categoryGroup1 != null);
        for (Category_v1 category : categories) {
            Category_v1 categoryDeleted = categoryDAOImplForTest.getCategoryById(category.getId());
            if (category.getId().equals(categoryToDelete.getId())) {
                assertEquals(true, categoryDeleted == null);
            } else {
                assertEquals(true, categoryDeleted != null);
            }
        }
        ////////////////////////////2//////////////////////////////////
        CategoryGroup_v1 categoryGroup2 = categoryGroupDAOImplForTest.getCategoryGroup_v1ById(categoryGroupCredit.getId(), true);
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
        CategoryGroup_v1 categoryGroupCredit = new CategoryGroupCredit_v1("cr catGr to sel " + randomInt,
                "credit category group to select " + randomInt);
        List<Category_v1> creditCategories = new ArrayList<>();
        //put service: putting category_group in category and category in category_group
        String categoryName = "cat cr 47_v" + randomInt;
        Category_v1 category = new CategoryCredit_v1(categoryGroupCredit, categoryName,
                "credit category test #47 'description'");
        creditCategories.add(category);
        categoryGroupCredit.setCategories(creditCategories);
        categoryGroupDAOImplForTest.saveOrUpdateCategoryGroup_v1(categoryGroupCredit);

        LOG.debug(">>>category group to delete: " + categoryGroupCredit);
        LOG.debug(">>>categories to delete: " + creditCategories);

        List<Category_v1> categoryByNameList = categoryDAOImplForTest.getCategoryByName(categoryName);
        LOG.debug(">>>categoryByNameList: " + categoryByNameList);
        assertEquals(true, categoryByNameList.size() > 0);
        for (Category_v1 cat : categoryByNameList) {
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
        CategoryGroup_v1 categoryGroupDebit = new CategoryGroupDebit_v1("dt catGr to sel " + randomInt,
                "debit category group to select " + randomInt);
        List<Category_v1> debitCategories = new ArrayList<>();
        //put service: putting category_group in category and category in category_group
        for(int i = 0 ; i < categoriesQty;i++) {
            Category_v1 category = new CategoryDebit_v1(categoryGroupDebit, "cat dt " + i + "_v" + randomInt,
                    "debit category #" + i + " 'description'");
            debitCategories.add(category);
        }
        categoryGroupDebit.setCategories(debitCategories);
        categoryGroupDAOImplForTest.saveOrUpdateCategoryGroup_v1(categoryGroupDebit);

        LOG.debug(">>>category group to delete: " + categoryGroupDebit);
        LOG.debug(">>>categories to delete: " + debitCategories);

        List<CategoryDebit_v1> categoryDebits = categoryDAOImplForTest.categoryByType(CategoryDebit_v1.class);
        LOG.debug(">>>categoryDebits: " + categoryDebits);
        assertEquals(true, categoryDebits.size() > 0);

        LOG.debug(">>>qtyCategory: " + categoryDebits.size());
        assertEquals(true, categoryDebits.size() > 0);
    }

//    @Test
    //under investigation all methods with "WithLeftJoinFetch" suffix
    public void test_47_seleteCategoryList() throws Exception {
//        int randomInt = randomWithinRange.getRandom();
//        int categoriesQty = 3;
//        String categoryGroupName = "categoryGroup nameAndType " + randomInt;
//        CategoryGroup_v1 categoryGroupDebit = new CategoryGroupDebit_v1(categoryGroupName,
//                "debit category group to select " + randomInt);
//        List<Category_v1> debitCategories = new ArrayList<>();
//        //put service: putting category_group in category and category in category_group
//        for(int i = 0 ; i < categoriesQty;i++) {
//            Category_v1 category = new CategoryDebit_v1(categoryGroupDebit, "cat db same name " + i + "_v" + randomInt,
//                    "credit category #" + i + " 'description'");
//            debitCategories.add(category);
//        }
//        categoryGroupDebit.setCategories(debitCategories);
//        LOG.debug(">>>category group name and type: " + categoryGroupDebit);
//        LOG.debug(">>>categories of category group: " + debitCategories);
//        categoryGroupDAOImplForTest.saveOrUpdateCategoryGroup_v1(categoryGroupDebit);

        //byName return list as it might be debit or credit and return categories
        List<CategoryGroup_v1> categoryGroupByNameAndTypeResult = categoryGroupDAOImplForTest
                .categoryGroup_v1ListWithLeftJoinFetch(true);

        LOG.debug(">>>category group select by id: " + categoryGroupByNameAndTypeResult);

//        assertEquals(true, categoryGroupByNameAndTypeResult.getName().equals(categoryGroupName));
//        assertEquals(true, categoryGroupByNameAndTypeResult.getCategoryGroupType().equals(DataBaseConstants.CATEGORY_GROUP_TYPE.DEBIT.getDiscriminator()));
//
//        LOG.debug(">>>categoryGroupList: " + categoryGroupByNameAndTypeResult.getCategories());
//        assertEquals(true, categoryGroupByNameAndTypeResult.getCategories().size() > 0);
    }
}
