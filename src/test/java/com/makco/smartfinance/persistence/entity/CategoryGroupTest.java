package com.makco.smartfinance.persistence.entity;

import com.makco.smartfinance.persistence.utils.TestPersistenceManager;
import java.util.Random;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.RollbackException;
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
public class CategoryGroupTest {
    private final static Logger LOG = LogManager.getLogger(CategoryGroupTest.class);
    private static EntityManager em;
    private String categoryGroupDebit1 = "CategoryGroupDebit1";
    private String categoryGroupCredit1 = "CategoryGroupCredit1";
    private String dublicateName = "TwinCategoryGroup";
    private String defaultDescription = "shop's description";

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
    }

    @After
    public void tearDown() throws Exception {
        em.close();
    }

    @Test
    public void test_11_Persist_debit() throws Exception {
        LOG.info("start->testPersist");
        CategoryGroup categoryGroup1 = new CategoryGroupDebit();
        Random random = new Random();
        int randomInt = random.nextInt((MAX - 0) + MIN + 0);
        LOG.debug("testPersist.randomInt=" + randomInt);
        categoryGroup1.setName(categoryGroupDebit1 + randomInt);
        categoryGroup1.setDescription(defaultDescription);

        em.persist(categoryGroup1);
        em.getTransaction().commit();
        LOG.debug("categoryGroup1.getId()=" + categoryGroup1.getId());
        LOG.debug("categoryGroup1.getCreatedOn()=" + categoryGroup1.getCreatedOn());
        LOG.debug("categoryGroup1.getUpdatedOn()=" + categoryGroup1.getUpdatedOn());

        assertEquals(true, categoryGroup1.getCreatedOn() != null);
        assertEquals(true, categoryGroup1.getUpdatedOn() != null);
        LOG.info("end->testPersist");
    }

    @Test(expected = RollbackException.class)//persistence, not transaction
    //Unique index or primary key violation: "IDX_UNQ_CTGRGRP_TPNM ON TEST.CATEGORY_GROUP(TYPE, NAME) VALUES ('D', 'TwinCategoryGroup771883', 2)"
    public void test_12_PersistDuplicateName_debit() throws Exception {
        LOG.info("start->test_12_PersistDuplicateName");
        Random random = new Random();
        int randomInt = random.nextInt((MAX - 0) + MIN + 0);
        LOG.debug("test_12_PersistDuplicateName.randomInt=" + randomInt);
        try {
            CategoryGroup categoryGroupDebit1 = new CategoryGroupDebit();
            categoryGroupDebit1.setName(dublicateName + randomInt);
            categoryGroupDebit1.setDescription(defaultDescription);
            em.persist(categoryGroupDebit1);

            CategoryGroup categoryGroupDebit2 = new CategoryGroupDebit();
            categoryGroupDebit2.setName(dublicateName + randomInt);
            categoryGroupDebit2.setDescription(defaultDescription);
            em.persist(categoryGroupDebit2);
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
        CategoryGroup categoryGroup1 = new CategoryGroupCredit();
        Random random = new Random();
        int randomInt = random.nextInt((MAX - 0) + MIN + 0);
        LOG.debug("testPersist.randomInt=" + randomInt);
        categoryGroup1.setName(categoryGroupCredit1 + randomInt);
        categoryGroup1.setDescription(defaultDescription);

        em.persist(categoryGroup1);
        em.getTransaction().commit();
        LOG.debug("categoryGroup1.getId()=" + categoryGroup1.getId());
        LOG.debug("categoryGroup1.getCreatedOn()=" + categoryGroup1.getCreatedOn());
        LOG.debug("categoryGroup1.getUpdatedOn()=" + categoryGroup1.getUpdatedOn());

        assertEquals(true, categoryGroup1.getCreatedOn() != null);
        assertEquals(true, categoryGroup1.getUpdatedOn() != null);
        LOG.info("end->testPersist");
    }

    @Test
    public void test_14_Persist_creditWithDebitName() throws Exception {
        LOG.info("start->testPersist");
        Random random = new Random();
        int randomInt = random.nextInt((MAX - 0) + MIN + 0);

        CategoryGroup categoryGroupDebit1 = new CategoryGroupDebit();
        categoryGroupDebit1.setName(dublicateName + randomInt);
        categoryGroupDebit1.setDescription(defaultDescription);
        em.persist(categoryGroupDebit1);

        CategoryGroup categoryGroupCredit2 = new CategoryGroupCredit();
        categoryGroupCredit2.setName(dublicateName + randomInt);
        categoryGroupCredit2.setDescription(defaultDescription);
        em.persist(categoryGroupCredit2);
        em.getTransaction().commit();

        LOG.debug("categoryGroupDebit1.getId()=" + categoryGroupDebit1.getId());
        LOG.debug("categoryGroupDebit1.getName()=" + categoryGroupDebit1.getName());
        LOG.debug("categoryGroupCredit2.getId()=" + categoryGroupCredit2.getId());
        LOG.debug("categoryGroupCredit2.getName()=" + categoryGroupCredit2.getName());

        assertEquals(true, categoryGroupDebit1.getName().equals(categoryGroupCredit2.getName()));
        LOG.info("end->testPersist");
    }

    @Test
    public void test_21_Update_debit() throws Exception {
        LOG.info("start->testUpdate");
        Query qId = em.createQuery("SELECT min(cg.id) from CategoryGroup cg");
        Long id = ((Long) qId.getSingleResult());

        LOG.debug("min num = " + id);

        CategoryGroup categoryGroup = em.find(CategoryGroup.class, id);

        //min 0 and max 100
        Random random = new Random();
        int randomInt = random.nextInt((MAX - 0) + MIN + 0);
        LOG.debug("testUpdate.randomInt=" + randomInt);
        categoryGroup.setName(categoryGroupDebit1 + randomInt);
        categoryGroup.setDescription(defaultDescription + randomInt);

        em.persist(categoryGroup);
        em.getTransaction().commit();

        LOG.debug(">>>categoryGroup.getId()=" + categoryGroup.getId());
        LOG.debug(">>>categoryGroup.getCreatedOn()=" + categoryGroup.getCreatedOn());
        LOG.debug(">>>categoryGroup.getUpdatedOn()=" + categoryGroup.getUpdatedOn());
        LOG.debug(categoryGroup);

        assertEquals(true, categoryGroup.getCreatedOn() != null);
        assertEquals(true, categoryGroup.getUpdatedOn() != null);
        assertEquals(true, !categoryGroup.getCreatedOn().equals(categoryGroup.getUpdatedOn()));
        LOG.info("end->testUpdate");
    }

    @Test
    public void test_31_Delete_debit() throws Exception {
        LOG.info("start->testDelete");
        Query qId = em.createQuery("SELECT min(cg.id) as num from CategoryGroup cg");
        Long id = ((Long) qId.getSingleResult());

        LOG.debug("min id = " + id);

        CategoryGroup categoryGroup = em.find(CategoryGroup.class, id);

        em.remove(categoryGroup);
        em.getTransaction().commit();

        CategoryGroup organizationJustDeleted = em.find(CategoryGroup.class, id);
        LOG.debug(">>>categoryGroup=" + organizationJustDeleted);
        LOG.debug(categoryGroup);

        assertEquals(null, organizationJustDeleted);
        LOG.info("end->testDelete");
    }
}