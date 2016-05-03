package com.makco.smartfinance.persistence.dao;

import com.makco.smartfinance.constants.DataBaseConstants;
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
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import java.util.ArrayList;
import java.util.Comparator;
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

    //java.lang.ClassCastException: com.makco.smartfinance.persistence.entity.CategoryGroupDebit cannot be cast to com.makco.smartfinance.persistence.entity.CategoryGroupCredit
    @Test(expected = ClassCastException.class)
//    @Test
    public void test_13_saveCategoryGroupWithCategoriesFromAnotherGroupType() throws Exception {
        int randomInt = randomWithinRange.getRandom();
        String name = debitCategoryGroupDebitName + randomInt;
        CategoryGroup categoryGroup = new CategoryGroupDebit(name, debitCategoryGroupDebitDesc);

        List<Category> categories = new ArrayList<>();

        //put service: putting category_group in category and category in category_group
        for(int i = 1 ; i < 5;i++) {
            Category category = new CategoryCredit(categoryGroup, "cat cred " + i + "->" + randomInt,
                    "credit category #" + i + " 'description'");
            categories.add(category);
        }
        categoryGroup.setCategories(categories);

        categoryGroupDAOImplForTest.saveOrUpdateCategoryGroup(categoryGroup);
//        java.lang.ClassCastException: com.makco.smartfinance.persistence.entity.CategoryGroupDebit cannot be cast to com.makco.smartfinance.persistence.entity.CategoryGroupCredit
//        at com.makco.smartfinance.persistence.entity.CategoryCredit.<init>(CategoryCredit.java:25)
//        at com.makco.smartfinance.persistence.dao.CategoryGroupDAOImplTest.test_13_saveCategoryGroupWithCategoriesFromAnotherGroupType(CategoryGroupDAOImplTest.java:97)
    }

    @Test
    //TODO test_21_updateCategoryGroup
    public void test_21_updateCategoryGroup() throws Exception {
        int randomInt = randomWithinRange.getRandom();

        Session session = TestPersistenceSession.openSession();
        session.beginTransaction();
//        http://stackoverflow.com/questions/15957441/jpql-with-subquery-to-select-max-count
//        List<CategoryGroup> categoryGroupList = session
//                .createQuery("SELECT cg FROM CategoryGroup cg WHERE cg.comments.size = (SELECT c.categoryGroupCredit.id COUNT(u2.comments.size) FROM Category c)")
//                .list();

        List<Object[]> catGrCrWithQtyOfCatCr = session
//                .createQuery("SELECT TYPE(c) as type, COUNT(c) as count FROM Category c where TYPE(c) <> 'E' group by TYPE(c)")
                .createQuery("SELECT c.categoryGroup, COUNT(c) FROM CategoryCredit c group by c.categoryGroup")
                .list();

        LOG.debug(">>>categoryGroupList: "+catGrCrWithQtyOfCatCr);

        CategoryGroupCredit minCategoryGroupCredit = (CategoryGroupCredit)catGrCrWithQtyOfCatCr
                .stream()
                .min(Comparator.comparingLong(obj -> (long)obj[1]))
                .map(min -> min[0])
                .orElse(null);
        LOG.debug(">>>minCategoryGroupCredit: "+minCategoryGroupCredit);
        session.getTransaction().commit();
        session.close();

        CategoryGroup categoryGroup = categoryGroupDAOImplForTest.getCategoryGroupById(minCategoryGroupCredit.getId(), false);

        String newName = categoryGroup.getName()+"_changed_v"+randomInt;

        newName = (newName.length() > DataBaseConstants.CG_NAME_MAX_LGTH) ? newName.substring(newName.length() - DataBaseConstants.CG_NAME_MAX_LGTH) : newName;
        categoryGroup.setName(newName);

        categoryGroupDAOImplForTest.saveOrUpdateCategoryGroup(categoryGroup);

        LOG.debug("categoryGroup: " + categoryGroup);
        assertEquals(true, categoryGroup.getId() != null);
        assertEquals(CategoryGroup.CATEGORY_GROUP_TYPE_CREDIT, categoryGroup.getCategoryGroupType());
        assertEquals(newName, categoryGroup.getName());
        assertEquals(true, categoryGroup.getCreatedOn() != null);
        assertEquals(true, categoryGroup.getUpdatedOn() != null);
        assertEquals(true, categoryGroup.getUpdatedOn() != categoryGroup.getCreatedOn());
    }

    @Test
    //TODO test_21_updateCategoryGroup
    public void test_22_updateCategoryGroupWithCategories() throws Exception {
        int randomInt = randomWithinRange.getRandom();

        Session session = TestPersistenceSession.openSession();
        session.beginTransaction();

        List<Object[]> catGrCrWithQtyOfCatCr = session
//                .createQuery("SELECT TYPE(c) as type, COUNT(c) as count FROM Category c where TYPE(c) <> 'E' group by TYPE(c)")
                .createQuery("SELECT c.categoryGroup.id, COUNT(c) as max_count FROM CategoryCredit c group by c.categoryGroup.id")
                .list();

        LOG.debug(">>>categoryGroupList: "+catGrCrWithQtyOfCatCr);

        Long maxCategoryGroupCreditId = (Long)catGrCrWithQtyOfCatCr
                .stream()
                .max(Comparator.comparingLong(obj -> (long)obj[1]))
                .map(max -> max[0])
                .orElse(null);
        LOG.debug(">>>maxCategoryGroupCreditId: "+maxCategoryGroupCreditId);
        session.getTransaction().commit();
        session.close();

        CategoryGroup categoryGroup = categoryGroupDAOImplForTest.getCategoryGroupById(maxCategoryGroupCreditId, true);

        categoryGroup.setName(categoryGroup.getName()+"_chaged_v"+randomInt);

        List<Category> categories = new ArrayList<>();
        String name = debitCategoryGroupDebitName + randomInt;
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
