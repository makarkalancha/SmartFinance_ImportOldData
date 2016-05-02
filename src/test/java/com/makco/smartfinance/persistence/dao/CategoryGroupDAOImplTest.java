package com.makco.smartfinance.persistence.dao;

import com.makco.smartfinance.persistence.dao.dao_implementations.CategoryGroupDAOImplForTest;
import com.makco.smartfinance.persistence.entity.Category;
import com.makco.smartfinance.persistence.entity.CategoryDebit;
import com.makco.smartfinance.persistence.entity.CategoryGroup;
import com.makco.smartfinance.persistence.entity.CategoryGroupDebit;
import com.makco.smartfinance.utils.RandomWithinRange;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * User: Makar Kalancha
 * Date: 30/04/2016
 * Time: 23:52
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class CategoryGroupDAOImplTest {
    private final static Logger LOG = LogManager.getLogger(CategoryGroupDAOImplTest.class);
    private static final String debitCategoryGroupDebitName = "categoryGroupDebit_name";
    private static final String debitCategoryGroupDebitDesc = "categoryGroupDebit_description";

    private static int MIN = 1;
    private static int MAX = 1_000_000;
    private static RandomWithinRange randomWithinRange = new RandomWithinRange(MIN, MAX);

    private CategoryGroupDAOImplForTest categoryGroupDAOImplForTest = new CategoryGroupDAOImplForTest();

    @Test
    public void test_11_saveOrUpdateCategoryGroupDebit() throws Exception {
        int randomInt = randomWithinRange.getRandom();
        String name = debitCategoryGroupDebitName + randomInt;
        CategoryGroup categoryGroup = new CategoryGroupDebit(name, debitCategoryGroupDebitDesc);
        categoryGroupDAOImplForTest.saveOrUpdateCategoryGroup(categoryGroup);

        LOG.debug("categoryGroup: " + categoryGroup);
        assertEquals(true, categoryGroup.getId() != null);
        assertEquals(CategoryGroup.CATEGORY_GROUP_TYPE_DEBIT, categoryGroup.getCategoryGroupType());
        assertEquals(name, categoryGroup.getName());
        assertEquals(debitCategoryGroupDebitDesc, categoryGroup.getDescription());
        assertEquals(true, categoryGroup.getCreatedOn() != null);
        assertEquals(true, categoryGroup.getUpdatedOn() != null);
    }

    @Test
    public void test_12_saveCategoryGroupWithCategories() throws Exception {
        int randomInt = randomWithinRange.getRandom();
        String name = debitCategoryGroupDebitName + randomInt;
        CategoryGroup categoryGroup = new CategoryGroupDebit(name, debitCategoryGroupDebitDesc);

        List<Category> categories = new ArrayList<>();

        //put service: putting category_group in category and category in category_group
        for(int i = 1 ; i < 5;i++) {
            Category category = new CategoryDebit(categoryGroup, "cat deb " + i + "->" + randomInt,
                    "debit category #" + i + " 'description'");
            categories.add(category);
        }
        categoryGroup.setCategories(categories);

        categoryGroupDAOImplForTest.saveOrUpdateCategoryGroup(categoryGroup);

        LOG.debug("categoryGroup: " + categoryGroup);
        assertEquals(true, categoryGroup.getId() != null);
        assertEquals(CategoryGroup.CATEGORY_GROUP_TYPE_DEBIT, categoryGroup.getCategoryGroupType());
        assertEquals(name, categoryGroup.getName());
        assertEquals(debitCategoryGroupDebitDesc, categoryGroup.getDescription());
        assertEquals(true, categoryGroup.getCreatedOn() != null);
        assertEquals(true, categoryGroup.getUpdatedOn() != null);
        assertEquals(false, categoryGroup.getCategories().isEmpty());
        for (Category category : categories){
            LOG.debug("category: " + category);
            assertEquals(true, category.getId() != null);
            assertEquals(true, category.getCreatedOn() != null);
            assertEquals(true, category.getUpdatedOn() != null);
        }
    }

    @Test
    //TODO test_13_saveCategoryGroupWithCategoriesFromAnotherGroupType
    public void test_13_saveCategoryGroupWithCategoriesFromAnotherGroupType() throws Exception {

    }

    @Test
    //TODO test_21_updateCategoryGroup
    public void test_21_updateCategoryGroup() throws Exception {

    }

    @Test
    //TODO test_31_removeCategoryGroup
    public void test_31_removeCategoryGroup() throws Exception {

    }

    @Test
    //TODO test_32_removeCategoryGroup
    public void test_32_removeCategoryGroupWithCategories() throws Exception {

    }

    @Test
    //TODO test_41_seleteAllCategoryGroups
    public void test_41_seleteAllCategoryGroups() throws Exception {
//        Session session = null;
//        List<FamilyMember> list = new ArrayList<>();
//        try{
//            session = HibernateUtil.openSession();
//            session.beginTransaction();
//            list = session.createQuery("SELECT f FROM FamilyMember f ORDER BY f.name").list();
//            session.getTransaction().commit();
//
//        } catch (Exception e) {
//            //hibernate persistence p.257
//            try {
//                if (session.getTransaction().getStatus() == TransactionStatus.ACTIVE
//                        || session.getTransaction().getStatus() == TransactionStatus.MARKED_ROLLBACK)
//                    session.getTransaction().rollback();
//            } catch (Exception rbEx) {
//                LOG.error("Rollback of transaction failed, trace follows!");
//                LOG.error(rbEx, rbEx);
//            }
//            throw new RuntimeException(e);
//        } finally {
//            if(session != null){
//                session.close();
//            }
//        }
//        return list;
    }

    @Test
    //TODO test_42_seleteCategoryGroupById
    public void test_42_seleteCategoryGroupById() throws Exception {

    }

    @Test
    //TODO test_43_seleteCategoryGroupByName
    public void test_43_seleteCategoryGroupByName() throws Exception {

    }

    @Test
    //TODO test_44_seleteAllCategoryGroupsWithCategories
    public void test_44_seleteAllCategoryGroupsWithCategories() throws Exception {

    }

    @Test
    //TODO test_45_seleteDebitCategoryGroupsWithCategories
    public void test_45_seleteDebitCategoryGroupsWithCategories() throws Exception {

    }

    @Test
    //TODO test_46_seleteCreditCategoryGroupsWithCategories
    public void test_46_seleteCreditCategoryGroupsWithCategories() throws Exception {

    }
}
