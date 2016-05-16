package com.makco.smartfinance.persistence.entity.entity_manager;

import com.makco.smartfinance.persistence.entity.CategoryGroup;
import com.makco.smartfinance.persistence.entity.entity_manager.test_entities.CategoryGroupCredit;
import com.makco.smartfinance.persistence.entity.entity_manager.test_entities.CategoryGroupDebit;
import com.makco.smartfinance.persistence.utils.rules.EntityManagerRule;
import com.makco.smartfinance.utils.RandomWithinRange;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.RollbackException;

import static org.junit.Assert.assertEquals;

/**
 * Created by mcalancea on 2016-04-25.
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class CategoryGroupEMTest {
    private final static Logger LOG = LogManager.getLogger(CategoryGroupEMTest.class);
    private String categoryGroupDebit1 = "CategoryGroupDebit1";
    private String categoryGroupCredit1 = "CategoryGroupCredit1";
    private String dublicateName = "TwinCategoryGroup";
    private String defaultDescription = "shop's description";

    private static int MIN = 1;
    private static int MAX = 1_000_000;
    private static RandomWithinRange randomWithinRange = new RandomWithinRange(MIN, MAX);

    @Rule
    public final EntityManagerRule entityManagerRule = new EntityManagerRule();
    
    @BeforeClass
    public static void setUpClass() throws Exception {

    }

    @AfterClass
    public static void tearDownClass() throws Exception {
        
    }

    @Before
    public void setUp() throws Exception {
        
    }

    @After
    public void tearDown() throws Exception {
        
    }

    @Test
    public void test_11_Persist_debit() throws Exception {
        LOG.info("start->testPersist");
        EntityManager em = entityManagerRule.getEntityManager();
        
        CategoryGroup categoryGroup1 = new CategoryGroupDebit();
        int randomInt = randomWithinRange.getRandom();
        LOG.debug("testPersist.randomInt=" + randomInt);
        categoryGroup1.setName(categoryGroupDebit1 + randomInt);
        categoryGroup1.setDescription(defaultDescription);

        em.persist(categoryGroup1);
        entityManagerRule.commit();
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
        EntityManager em = entityManagerRule.getEntityManager();

        int randomInt = randomWithinRange.getRandom();
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
            entityManagerRule.commit();
        } catch (Exception e) {
            try {
                if (entityManagerRule.isActive()) {
                    entityManagerRule.rollback();
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
        LOG.info("start->testPersist");
        EntityManager em = entityManagerRule.getEntityManager();

        CategoryGroup categoryGroup1 = new CategoryGroupCredit();
        int randomInt = randomWithinRange.getRandom();
        LOG.debug("testPersist.randomInt=" + randomInt);
        categoryGroup1.setName(categoryGroupCredit1 + randomInt);
        categoryGroup1.setDescription(defaultDescription);

        em.persist(categoryGroup1);
        entityManagerRule.commit();
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
        EntityManager em = entityManagerRule.getEntityManager();

        int randomInt = randomWithinRange.getRandom();
        CategoryGroup categoryGroupDebit1 = new CategoryGroupDebit();
        categoryGroupDebit1.setName(dublicateName + randomInt);
        categoryGroupDebit1.setDescription(defaultDescription);
        em.persist(categoryGroupDebit1);

        CategoryGroup categoryGroupCredit2 = new CategoryGroupCredit();
        categoryGroupCredit2.setName(dublicateName + randomInt);
        categoryGroupCredit2.setDescription(defaultDescription);
        em.persist(categoryGroupCredit2);
        entityManagerRule.commit();

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
        EntityManager em = entityManagerRule.getEntityManager();

        Query qId = em.createQuery("SELECT min(cg.id) from CategoryGroup cg");
        Long id = ((Long) qId.getSingleResult());

        LOG.debug("min num = " + id);

        CategoryGroup categoryGroup = em.find(CategoryGroup.class, id);

        //min 0 and max 100
        int randomInt = randomWithinRange.getRandom();
        LOG.debug("testUpdate.randomInt=" + randomInt);
        categoryGroup.setName(categoryGroupDebit1 + randomInt);
        categoryGroup.setDescription(defaultDescription + randomInt);

        em.persist(categoryGroup);
        entityManagerRule.commit();

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
        EntityManager em = entityManagerRule.getEntityManager();

        Query qId = em.createQuery("SELECT min(cg.id) as num from CategoryGroup cg");
        Long id = ((Long) qId.getSingleResult());

        LOG.debug("min id = " + id);

        CategoryGroup categoryGroup = em.find(CategoryGroup.class, id);

        em.remove(categoryGroup);
        entityManagerRule.commit();

        CategoryGroup organizationJustDeleted = em.find(CategoryGroup.class, id);
        LOG.debug(">>>categoryGroup=" + organizationJustDeleted);
        LOG.debug(categoryGroup);

        assertEquals(null, organizationJustDeleted);
        LOG.info("end->testDelete");
    }
}