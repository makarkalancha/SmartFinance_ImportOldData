package com.makco.smartfinance.persistence.entity.session;

import com.google.common.collect.Lists;
import com.makco.smartfinance.persistence.entity.Category;
import com.makco.smartfinance.persistence.entity.CategoryCredit;
import com.makco.smartfinance.persistence.entity.CategoryDebit;
import com.makco.smartfinance.persistence.entity.CategoryGroup;
import com.makco.smartfinance.persistence.entity.CategoryGroupCredit;
import com.makco.smartfinance.persistence.entity.CategoryGroupDebit;
import com.makco.smartfinance.utils.RandomWithinRange;
import com.makco.smartfinance.persistence.utils.rules.SessionRule;
import javax.persistence.NonUniqueResultException;
import javax.persistence.Persistence;
import javax.persistence.PersistenceUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Query;
import org.hibernate.Session;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import static org.junit.Assert.assertEquals;

/**
 * Created by mcalancea on 2016-04-25.
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class CategorySessionTest {
    private final static Logger LOG = LogManager.getLogger(CategorySessionTest.class);
    private String categoryDebitName1 = "CategoryDebitSession1";
    private String categoryCreditName1 = "CategoryCreditSession1";
    private String duplicateName = "TwinCategorySession";
    private String defaultDescription = "shop's description Session";

    private static int MIN = 1;
    private static int MAX = 1_000_000;
    private static RandomWithinRange randomWithinRange = new RandomWithinRange(MIN, MAX);
    private static CategoryGroup categoryGroupDebit1 = new CategoryGroupDebit("CatGrD1 Session", "Session debit category group's number is 1");
    private static CategoryGroup categoryGroupCredit1 = new CategoryGroupCredit("CatGrC1 Session", "Session credit category group's number is 1");
    private static PersistenceUtil persistenceUtil = Persistence.getPersistenceUtil();

    @Rule
    public final SessionRule sessionRule = new SessionRule();

    @BeforeClass
    public static void setUpClass() throws Exception {

    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() throws Exception {
        setCategoryGroupDebit();
        setCategoryGroupCredit();
    }

    @After
    public void tearDown() throws Exception {

    }

    private void setCategoryGroupDebit(){
        Session session = sessionRule.getSession();
        if(categoryGroupDebit1.getId() == null) {
            CategoryGroupDebit tmp = null;
            try {
                tmp = (CategoryGroupDebit) session.createQuery("select cd from CategoryGroupDebit cd where cd.name = :catGrName")
                        .setParameter("catGrName", categoryGroupDebit1.getName())
                        .uniqueResult();
            } catch (NonUniqueResultException e) {
                LOG.error(e, e);
            }
            if (tmp != null) {
                categoryGroupDebit1 = tmp;
            }
//        } else if (Hibernate.isInitialized(categoryGroupDebit1)) {
////            persistenceUtil.isLoaded
//            categoryGroupDebit1 = (CategoryGroupDebit)session.merge(categoryGroupDebit1);
        }
    }

    private void setCategoryGroupCredit(){
        Session session = sessionRule.getSession();
        if(categoryGroupCredit1.getId() == null) {
            CategoryGroupCredit tmp = null;
            try {
                tmp = (CategoryGroupCredit) session.createQuery("select cc from CategoryGroupCredit cc where cc.name = :catGrName")
                        .setParameter("catGrName", categoryGroupCredit1.getName())
                        .uniqueResult();
            } catch (NonUniqueResultException e) {

            }
            if (tmp != null) {
                categoryGroupCredit1 = tmp;
            }
//        } else if (Hibernate.isInitialized(categoryGroupCredit1)) {
//            categoryGroupCredit1 = (CategoryGroupCredit)session.merge(categoryGroupCredit1);
        }
    }

    @Test
    public void test_11_Persist_categoryGroupAndCategoryWithOnePersist_debit() throws Exception {
        LOG.info("start->test_11_Persist_categoryGroupAndCategoryWithOnePersist_debit");
        Session session = sessionRule.getSession();

        int randomInt = randomWithinRange.getRandom();
        LOG.debug("testPersist.randomInt=" + randomInt);

        //Saves the bids automatically (later, at flush time)
        //Can I reattach a detached instance? p252
        //Session.saveOrUpdateCopy() == EnitityManager.merge()
        session.saveOrUpdate(categoryGroupDebit1);

//        Category category1 = new CategoryDebit(categoryGroupDebit1, categoryDebitName1 + randomInt, defaultDescription);
        CategoryDebit category1 = new CategoryDebit();
        category1.setName(categoryDebitName1 + randomInt);
        category1.setDescription(defaultDescription);

        //bidirectional association
        category1.setCategoryGroup(categoryGroupDebit1);
        categoryGroupDebit1.getCategories().add(category1);

        //Dirty checking; SQL execution
        sessionRule.commit();
        LOG.debug("category1.getId()=" + category1.getId());
        LOG.debug("category1.getCreatedOn()=" + category1.getCreatedOn());
        LOG.debug("category1.getUpdatedOn()=" + category1.getUpdatedOn());

        assertEquals(true, category1.getCreatedOn() != null);
        assertEquals(true, category1.getUpdatedOn() != null);
        LOG.info("end->testPersist");
    }

//    @Test(expected = RollbackException.class)//persistence, not transaction
    @Test
    //no exception like Unique index or primary key violation: "IDX_UNQ_CTGR_CGIDNM ON TEST.CATEGORY
    //because CategoryGroup puts categories in a Set and NOT a List
    public void test_12_PersistDuplicateName_debit() throws Exception {
        LOG.info("start->test_12_PersistDuplicateName_debit");
        Session session = sessionRule.getSession();

        int randomInt = randomWithinRange.getRandom();
        String generatedDuplicateName = duplicateName + randomInt;
        LOG.debug("test_12_PersistDuplicateName.randomInt=" + randomInt);
        try {
            //Saves the bids automatically (later, at flush time)
            session.saveOrUpdate(categoryGroupDebit1);

            Category categoryDebit1 = new CategoryDebit(categoryGroupDebit1, generatedDuplicateName, defaultDescription);

            Category categoryDebit2 = new CategoryDebit(categoryGroupDebit1, generatedDuplicateName, defaultDescription);
            //http://stackoverflow.com/questions/858572/how-to-make-a-new-list-in-java
//            categoryGroupDebit1.addCategories(Arrays.asList(categoryDebitName1, categoryDebit2));
            categoryGroupDebit1.getCategories().add(Lists.newArrayList(categoryDebit1, categoryDebit2));

            LOG.debug("categoryDebitName1=" + categoryDebit1);
            LOG.debug("categoryDebit2=" + categoryDebit2);
            sessionRule.commit();
        } catch (Exception e) {
            try {
                if (sessionRule.isActiveOrMarkedRollback()) {
                    sessionRule.rollback();
                }
            } catch (Exception rbEx) {
                LOG.error("Rollback of transaction failed, trace follows!");
                LOG.error(rbEx, rbEx);
            }
            throw e;
        }
    }

    @Test
    public void test_13_Persist_credit() throws Exception {
        LOG.info("start->test_13_Persist_credit");
        Session session = sessionRule.getSession();

        //Saves the bids automatically (later, at flush time)
        //Can I reattach a detached instance? p252
        //Session.saveOrUpdateCopy() == EnitityManager.merge()
        session.saveOrUpdate(categoryGroupCredit1);


        Category category1 = new CategoryCredit();
        int randomInt = randomWithinRange.getRandom();
        LOG.debug("testPersist.randomInt=" + randomInt);
        category1.setName(categoryCreditName1 + randomInt);
        category1.setDescription(defaultDescription);

        //bidirectional association
        category1.setCategoryGroup(categoryGroupCredit1);
        categoryGroupCredit1.getCategories().add(category1);

        sessionRule.commit();
        LOG.debug("category1.getId()=" + category1.getId());
        LOG.debug("category1.getCreatedOn()=" + category1.getCreatedOn());
        LOG.debug("category1.getUpdatedOn()=" + category1.getUpdatedOn());

        assertEquals(true, category1.getCreatedOn() != null);
        assertEquals(true, category1.getUpdatedOn() != null);
        LOG.info("end->testPersist");
    }

    @Test
    public void test_14_Persist_creditWithDebitName() throws Exception {
        LOG.info("start->testPersist");
        Session session = sessionRule.getSession();

        int randomInt = randomWithinRange.getRandom();
        String generatedDuplicateName = duplicateName + randomInt;

        //Saves the bids automatically (later, at flush time)
        //Can I reattach a detached instance? p252
        //Session.saveOrUpdateCopy() == EnitityManager.merge()
        session.saveOrUpdate(categoryGroupDebit1);
        session.saveOrUpdate(categoryGroupCredit1);

        Category categoryDebit1 = new CategoryDebit();
        categoryDebit1.setName(generatedDuplicateName);
        categoryDebit1.setDescription(defaultDescription);
        categoryDebit1.setCategoryGroup(categoryGroupDebit1);

        Category categoryCredit2 = new CategoryCredit();
        categoryCredit2.setName(generatedDuplicateName);
        categoryCredit2.setDescription(defaultDescription);
        categoryCredit2.setCategoryGroup(categoryGroupCredit1);

        sessionRule.commit();

        LOG.debug("categoryDebitName1.getId()=" + categoryDebit1.getId());
        LOG.debug("categoryDebitName1.getName()=" + categoryDebit1.getName());
        LOG.debug("categoryCredit2.getId()=" + categoryCredit2.getId());
        LOG.debug("categoryCredit2.getName()=" + categoryCredit2.getName());

        assertEquals(true, categoryDebit1.getName().equals(categoryCredit2.getName()));
        LOG.info("end->testPersist");
    }

    @Test
    public void test_21_Update_debit() throws Exception {
        LOG.info("start->testUpdate");
        Session session = sessionRule.getSession();

        Query qId = session.createQuery("SELECT min(cg.id) from Category cg");
        Long id = ((Long) qId.uniqueResult());

        LOG.debug("min num = " + id);

        Category category = session.get(Category.class, id);

        //min 0 and max 100
        int randomInt = randomWithinRange.getRandom();
        LOG.debug("testUpdate.randomInt=" + randomInt);
        category.setName(categoryDebitName1 + randomInt);
        category.setDescription(defaultDescription + randomInt);

        session.saveOrUpdate(category);
        sessionRule.commit();

        LOG.debug(">>>category.getId()=" + category.getId());
        LOG.debug(">>>category.getCreatedOn()=" + category.getCreatedOn());
        LOG.debug(">>>category.getUpdatedOn()=" + category.getUpdatedOn());
        LOG.debug(category);

        assertEquals(true, category.getCreatedOn() != null);
        assertEquals(true, category.getUpdatedOn() != null);
        assertEquals(true, !category.getCreatedOn().equals(category.getUpdatedOn()));
        LOG.info("end->testUpdate");
    }

    @Test
    public void test_31_Delete_debit() throws Exception {
        LOG.info("start->testDelete");
        Session session = sessionRule.getSession();

        Query qId = session.createQuery("SELECT min(cg.id) as num from Category cg");
        Long id = ((Long) qId.uniqueResult());

        LOG.debug("min id = " + id);

        Category category = session.get(Category.class, id);

        session.delete(category);
        sessionRule.commit();

        Category categoryJustDeleted = session.get(Category.class, id);
        LOG.debug(">>>category=" + categoryJustDeleted);
        LOG.debug(category);

        assertEquals(null, categoryJustDeleted);
        LOG.info("end->testDelete");
    }
}