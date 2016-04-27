package com.makco.smartfinance.persistence.entity;

import com.google.common.collect.Lists;
import com.makco.smartfinance.persistence.utils.TestPersistenceManager;
import java.util.Random;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import static org.junit.Assert.assertEquals;

/**
 * Created by mcalancea on 2016-04-25.
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class CategoryTest {
    private final static Logger LOG = LogManager.getLogger(CategoryTest.class);
    private static EntityManager em;
    private String categoryDebit1 = "CategoryDebit1";
    private String categoryCredit1 = "CategoryCredit1";
    private String dublicateName = "TwinCategory";
    private String defaultDescription = "shop's description";
    private CategoryGroup categoryGroupDebit1 = new CategoryGroupDebit("CatGrD1", "debit category group's number is 1");
    private CategoryGroup categoryGroupCredit1 = new CategoryGroupCredit("CatGrC1", "credit category group's number is 1");

    private static int MIN = 1;
    private static int MAX = 1_000_000;

    @BeforeClass
    public static void setUpClass() throws Exception {

    }

    @AfterClass
    public static void tearDownClass() throws Exception {
        TestPersistenceManager.INSTANCE.close();
    }

    @Before
    public void setUp() throws Exception {
        em = TestPersistenceManager.INSTANCE.getEntityManager();
        em.getTransaction().begin();
        setCategoryGroupDebit();
    }

    @After
    public void tearDown() throws Exception {
        em.close();
    }

    private void setCategoryGroupDebit(){
        if(categoryGroupDebit1.getId() == null) {
            CategoryGroupDebit tmp = null;
            try {
                tmp = (CategoryGroupDebit) em.createQuery("select cd from CategoryGroupDebit cd where cd.name = :catGrName")
                        .setParameter("catGrName", categoryGroupDebit1.getName())
                        .getSingleResult();
            } catch (NoResultException e) {

            }
            if (tmp != null) {
                categoryGroupDebit1 = tmp;
            }
        }
    }

    @Test
    public void test_11_Persist_categoryGroupAndCategoryWithOnePersist_debit() throws Exception {
        LOG.info("start->testPersist");
        Random random = new Random();
        int randomInt = random.nextInt((MAX - 0) + MIN + 0);
        LOG.debug("testPersist.randomInt=" + randomInt);

        //Saves the bids automatically (later, at flush time)
        em.persist(categoryGroupDebit1);

//        Category category1 = new CategoryDebit(categoryGroupDebit1, categoryDebit1 + randomInt, defaultDescription);
        CategoryDebit category1 = new CategoryDebit();
        category1.setName(categoryDebit1 + randomInt);
        category1.setDescription(defaultDescription);

        //bidirectional association
        category1.setCategoryGroup(categoryGroupDebit1);
        categoryGroupDebit1.addCategory(category1);

        //Dirty checking; SQL execution
        em.getTransaction().commit();
        LOG.debug("category1.getId()=" + category1.getId());
        LOG.debug("category1.getCreatedOn()=" + category1.getCreatedOn());
        LOG.debug("category1.getUpdatedOn()=" + category1.getUpdatedOn());

        assertEquals(true, category1.getCreatedOn() != null);
        assertEquals(true, category1.getUpdatedOn() != null);
        LOG.info("end->testPersist");
    }

//    @Test(expected = RollbackException.class)//persistence, not transaction
    @Test
//    Unique index or primary key violation: "IDX_UNQ_CTGRGRP_TPNM ON TEST.CATEGORY_GROUP(TYPE, NAME) VALUES ('D', 'TwinCategory771883', 2)"
    public void test_12_PersistDuplicateName_debit() throws Exception {
        LOG.info("start->test_12_PersistDuplicateName");
        Random random = new Random();
        int randomInt = random.nextInt((MAX - 0) + MIN + 0);
        LOG.debug("test_12_PersistDuplicateName.randomInt=" + randomInt);
        try {
            //Saves the bids automatically (later, at flush time)
            em.persist(categoryGroupDebit1);

            Category categoryDebit1 = new CategoryDebit(categoryGroupDebit1, dublicateName + randomInt, defaultDescription);

            Category categoryDebit2 = new CategoryDebit(categoryGroupDebit1, dublicateName + randomInt, defaultDescription);
            //http://stackoverflow.com/questions/858572/how-to-make-a-new-list-in-java
//            categoryGroupDebit1.addCategories(Arrays.asList(categoryDebit1, categoryDebit2));
            categoryGroupDebit1.addCategories(Lists.newArrayList(categoryDebit1, categoryDebit2));

            LOG.debug("categoryDebit1=" + categoryDebit1);
            LOG.debug("categoryDebit2=" + categoryDebit2);
            em.getTransaction().commit();
        } catch (Exception e) {
            try {
                if (em.getTransaction().isActive()) {
                    em.getTransaction().rollback();
                }
            } catch (Exception rbEx) {
                System.err.println("Rollback of transaction failed, trace follows!");
                rbEx.printStackTrace(System.err);
            }
            throw e;
        }
    }

    @Test
    public void test_13_Persist_credit() throws Exception {
        LOG.info("start->testPersist");
        Category category1 = new CategoryCredit();
        Random random = new Random();
        int randomInt = random.nextInt((MAX - 0) + MIN + 0);
        LOG.debug("testPersist.randomInt=" + randomInt);
        category1.setName(categoryCredit1 + randomInt);
        category1.setDescription(defaultDescription);

        em.persist(category1);
        em.getTransaction().commit();
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
        Random random = new Random();
        int randomInt = random.nextInt((MAX - 0) + MIN + 0);

        Category categoryDebit1 = new CategoryDebit();
        categoryDebit1.setName(dublicateName + randomInt);
        categoryDebit1.setDescription(defaultDescription);
        em.persist(categoryDebit1);

        Category categoryCredit2 = new CategoryCredit();
        categoryCredit2.setName(dublicateName + randomInt);
        categoryCredit2.setDescription(defaultDescription);
        em.persist(categoryCredit2);
        em.getTransaction().commit();

        LOG.debug("categoryDebit1.getId()=" + categoryDebit1.getId());
        LOG.debug("categoryDebit1.getName()=" + categoryDebit1.getName());
        LOG.debug("categoryCredit2.getId()=" + categoryCredit2.getId());
        LOG.debug("categoryCredit2.getName()=" + categoryCredit2.getName());

        assertEquals(true, categoryDebit1.getName().equals(categoryCredit2.getName()));
        LOG.info("end->testPersist");
    }

    @Test
    public void test_21_Update_debit() throws Exception {
        LOG.info("start->testUpdate");
        Query qId = em.createQuery("SELECT min(cg.id) from Category cg");
        Long id = ((Long) qId.getSingleResult());

        LOG.debug("min num = " + id);

        Category category = em.find(Category.class, id);

        //min 0 and max 100
        Random random = new Random();
        int randomInt = random.nextInt((MAX - 0) + MIN + 0);
        LOG.debug("testUpdate.randomInt=" + randomInt);
        category.setName(categoryDebit1 + randomInt);
        category.setDescription(defaultDescription + randomInt);

        em.persist(category);
        em.getTransaction().commit();

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
        Query qId = em.createQuery("SELECT min(cg.id) as num from Category cg");
        Long id = ((Long) qId.getSingleResult());

        LOG.debug("min id = " + id);

        Category category = em.find(Category.class, id);

        em.remove(category);
        em.getTransaction().commit();

        Category organizationJustDeleted = em.find(Category.class, id);
        LOG.debug(">>>category=" + organizationJustDeleted);
        LOG.debug(category);

        assertEquals(null, organizationJustDeleted);
        LOG.info("end->testDelete");
    }

}