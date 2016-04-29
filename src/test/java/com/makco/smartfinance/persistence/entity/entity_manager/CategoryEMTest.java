package com.makco.smartfinance.persistence.entity.entity_manager;

import com.google.common.collect.Lists;
import com.makco.smartfinance.persistence.entity.Category;
import com.makco.smartfinance.persistence.entity.FamilyMember;
import com.makco.smartfinance.persistence.entity.entity_manager.test_entities.*;
import com.makco.smartfinance.persistence.entity.CategoryGroup;
import com.makco.smartfinance.utils.RandomWithinRange;
import com.makco.smartfinance.utils.rules.EntityManagerRule;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Persistence;
import javax.persistence.PersistenceUtil;
import javax.persistence.Query;
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

import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * Created by mcalancea on 2016-04-25.
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class CategoryEMTest {
    private final static Logger LOG = LogManager.getLogger(CategoryEMTest.class);
    private String categoryDebitName1 = "CategoryDebit1";
    private String categoryCreditName1 = "CategoryCredit1";
    private String duplicateName = "TwinCategory";
    private String defaultDescription = "shop's description";

    private static int MIN = 1;
    private static int MAX = 1_000_000;
    private static RandomWithinRange randomWithinRange = new RandomWithinRange(MIN, MAX);
    private static CategoryGroup categoryGroupDebit1 = new CategoryGroupDebit("CatGrD1", "debit category group's number is 1");
    private static CategoryGroup categoryGroupCredit1 = new CategoryGroupCredit("CatGrC1", "credit category group's number is 1");
    private static PersistenceUtil persistenceUtil = Persistence.getPersistenceUtil();

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
        setCategoryGroupDebit();
        setCategoryGroupCredit();
    }

    @After
    public void tearDown() throws Exception {

    }

    private void setCategoryGroupDebit(){
        EntityManager em = entityManagerRule.getEntityManager();
        if(categoryGroupDebit1.getId() == null) {
            CategoryGroupDebit tmp = null;
            try {
                tmp = (CategoryGroupDebit) em.createQuery("select cd from CategoryGroupDebit cd where cd.name = :catGrName")
                        .setParameter("catGrName", categoryGroupDebit1.getName())
                        .getSingleResult();
            } catch (NoResultException e) {
                LOG.error(e, e);
            }
            if (tmp != null) {
                categoryGroupDebit1 = tmp;
            }
        } else if (persistenceUtil.isLoaded(categoryGroupDebit1)) {
            //Hibernate.isInitialized()
            categoryGroupDebit1 = em.merge(categoryGroupDebit1);
        }
    }

    private void setCategoryGroupCredit(){
        EntityManager em = entityManagerRule.getEntityManager();
        if(categoryGroupCredit1.getId() == null) {
            CategoryGroupCredit tmp = null;
            try {
                tmp = (CategoryGroupCredit) em.createQuery("select cc from CategoryGroupCredit cc where cc.name = :catGrName")
                        .setParameter("catGrName", categoryGroupCredit1.getName())
                        .getSingleResult();
            } catch (NoResultException e) {
                LOG.error(e, e);
            }
            if (tmp != null) {
                categoryGroupCredit1 = tmp;
            }
        } else if (persistenceUtil.isLoaded(categoryGroupCredit1)) {
            //Hibernate.isInitialized()
            categoryGroupCredit1 = em.merge(categoryGroupCredit1);
        }
    }

    @Test
    public void test_11_Persist_categoryGroupAndCategoryWithOnePersist_debit() throws Exception {
        LOG.info("start->test_11_Persist_categoryGroupAndCategoryWithOnePersist_debit");
        EntityManager em = entityManagerRule.getEntityManager();

        int randomInt = randomWithinRange.getRandom();
        LOG.debug("testPersist.randomInt=" + randomInt);

        //Saves the bids automatically (later, at flush time)
        //Can I reattach a detached instance? p252
        //Session.saveOrUpdateCopy() == EnitityManager.merge()
        em.persist(categoryGroupDebit1);

//        Category category1 = new CategoryDebit(categoryGroupDebit1, categoryDebitName1 + randomInt, defaultDescription);
        CategoryDebit category1 = new CategoryDebit();
        category1.setName(categoryDebitName1 + randomInt);
        category1.setDescription(defaultDescription);

        //bidirectional association
        category1.setCategoryGroup(categoryGroupDebit1);
        categoryGroupDebit1.addCategory(category1);

        //Dirty checking; SQL execution
        entityManagerRule.commit();
        LOG.debug("category1.getId()=" + category1.getId());
        LOG.debug("category1.getCreatedOn()=" + category1.getCreatedOn());
        LOG.debug("category1.getUpdatedOn()=" + category1.getUpdatedOn());

        assertEquals(true, category1.getCreatedOn() != null);
        assertEquals(true, category1.getUpdatedOn() != null);
        LOG.info("end->test_11_Persist_categoryGroupAndCategoryWithOnePersist_debit");
    }

//    @Test(expected = RollbackException.class)//persistence, not transaction
    @Test
    //no exception like Unique index or primary key violation: "IDX_UNQ_CTGR_CGIDNM ON TEST.CATEGORY
    //because CategoryGroup puts categories in a Set and NOT a List
    public void test_12_PersistDuplicateName_debit() throws Exception {
        LOG.info("start->test_12_PersistDuplicateName_debit");
        EntityManager em = entityManagerRule.getEntityManager();

        int randomInt = randomWithinRange.getRandom();
        String generatedDuplicateName = duplicateName + randomInt;
        LOG.debug("test_12_PersistDuplicateName.randomInt=" + randomInt);
        try {
            //Saves the bids automatically (later, at flush time)
            em.persist(categoryGroupDebit1);

            Category categoryDebit1 = new CategoryDebit(categoryGroupDebit1, generatedDuplicateName, defaultDescription);

            Category categoryDebit2 = new CategoryDebit(categoryGroupDebit1, generatedDuplicateName, defaultDescription);
            //http://stackoverflow.com/questions/858572/how-to-make-a-new-list-in-java
//            categoryGroupDebit1.addCategories(Arrays.asList(categoryDebitName1, categoryDebit2));
            categoryGroupDebit1.addCategories(Lists.newArrayList(categoryDebit1, categoryDebit2));

            LOG.debug("categoryDebitName1=" + categoryDebit1);
            LOG.debug("categoryDebit2=" + categoryDebit2);
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
        LOG.info("start->test_13_Persist_credit");
        EntityManager em = entityManagerRule.getEntityManager();

        //Saves the bids automatically (later, at flush time)
        //Can I reattach a detached instance? p252
        //Session.saveOrUpdateCopy() == EnitityManager.merge()
        em.persist(categoryGroupCredit1);


        Category category1 = new CategoryCredit();
        int randomInt = randomWithinRange.getRandom();
        LOG.debug("testPersist.randomInt=" + randomInt);
        category1.setName(categoryCreditName1 + randomInt);
        category1.setDescription(defaultDescription);

        //bidirectional association
        category1.setCategoryGroup(categoryGroupCredit1);
        categoryGroupCredit1.addCategory(category1);

        entityManagerRule.commit();
        LOG.debug("category1.getId()=" + category1.getId());
        LOG.debug("category1.getCreatedOn()=" + category1.getCreatedOn());
        LOG.debug("category1.getUpdatedOn()=" + category1.getUpdatedOn());

        assertEquals(true, category1.getCreatedOn() != null);
        assertEquals(true, category1.getUpdatedOn() != null);
        LOG.info("end->test_13_Persist_credit");
    }

    @Test
    public void test_14_Persist_creditWithDebitName() throws Exception {
        LOG.info("start->test_14_Persist_creditWithDebitName");
        EntityManager em = entityManagerRule.getEntityManager();

        int randomInt = randomWithinRange.getRandom();
        String generatedDuplicateName = duplicateName + randomInt;

        //Saves the bids automatically (later, at flush time)
        //Can I reattach a detached instance? p252
        //Session.saveOrUpdateCopy() == EnitityManager.merge()
        em.persist(categoryGroupDebit1);
        em.persist(categoryGroupCredit1);

        Category categoryDebit1 = new CategoryDebit();
        categoryDebit1.setName(generatedDuplicateName);
        categoryDebit1.setDescription(defaultDescription);
        categoryDebit1.setCategoryGroup(categoryGroupDebit1);

        Category categoryCredit2 = new CategoryCredit();
        categoryCredit2.setName(generatedDuplicateName);
        categoryCredit2.setDescription(defaultDescription);
        categoryCredit2.setCategoryGroup(categoryGroupCredit1);

        entityManagerRule.commit();

        LOG.debug("categoryDebitName1.getId()=" + categoryDebit1.getId());
        LOG.debug("categoryDebitName1.getName()=" + categoryDebit1.getName());
        LOG.debug("categoryCredit2.getId()=" + categoryCredit2.getId());
        LOG.debug("categoryCredit2.getName()=" + categoryCredit2.getName());

        assertEquals(true, categoryDebit1.getName().equals(categoryCredit2.getName()));
        LOG.info("end->test_14_Persist_creditWithDebitName");
    }

    @Test
    public void test_15_getLoop() throws Exception{
        //TODO get query of:
        //catGr eager, cat eager and bidirect;
        //get cat -> get cat gr -> get collection of cat => cartesian product
        LOG.info("start->test_15_getLoop");
        EntityManager em = entityManagerRule.getEntityManager();
        int randomInt = randomWithinRange.getRandom();

        EagerCategoryGroupDebit eagerCategoryGroupDebit =
                new EagerCategoryGroupDebit("eager_cg_name_" + randomInt, "eager_cg_desc_" + randomInt);
        em.persist(eagerCategoryGroupDebit);

        String eagerCategoryDescription = "multiple";
        for (int i = 0; i < 10; i++) {
            EagerCategoryDebit eagerCategoryDebit = new EagerCategoryDebit(
                            eagerCategoryGroupDebit,
                            i + "eager_c_name_" + randomInt,
                            i + "eager_c_desc_" + randomInt);
            eagerCategoryGroupDebit.addCategory(eagerCategoryDebit);
        }
        entityManagerRule.commit();
//        entityManagerRule.getEntityManager().close();

///////////////////////////////////////////////////////////
        EntityManager em1= entityManagerRule.getEntityManager();


        Query qId = em.createQuery("SELECT min(a.id) from EagerCategoryDebit a");
        LOG.debug(">>>EagerCategoryDebit: SELECT min(a.id) from EagerCategoryDebit a");
        Long id = ((Long) qId.getSingleResult());
        LOG.debug(">>>EagerCategoryDebit min id = " + id);

        EagerCategoryDebit eagerCategoryDebit = em.find(EagerCategoryDebit.class, id);

        LOG.debug(">>>eagerCategoryDebit.getCategoryGroup()");
        EagerCategoryGroupDebit eagerCategoryGroupDebit1 = (EagerCategoryGroupDebit) eagerCategoryDebit.getCategoryGroup();
        LOG.debug(">>>eagerCategoryGroupDebit1.getDebitCategories().size(): "+eagerCategoryGroupDebit1.getDebitCategories().size());

        //EAGER SQL QUERY
//        select
//        min(eagercateg0_.ID) as col_0_0_
//        from
//        TEST.CATEGORY eagercateg0_
//        where
//        eagercateg0_.CATEGORY_GROUP_TYPE='E'
//                -------------
//                select
//        eagercateg0_.ID as ID2_0_0_,
//                eagercateg0_.T_CREATEDON as T_CREATE3_0_0_,
//        eagercateg0_.DESCRIPTION as DESCRIPT4_0_0_,
//                eagercateg0_.NAME as NAME5_0_0_,
//        eagercateg0_.T_UPDATEDON as T_UPDATE6_0_0_,
//                eagercateg0_.CATEGORY_GROUP_ID as CATEGORY7_0_0_,
//        eagercateg1_.ID as ID2_1_1_,
//                eagercateg1_.T_CREATEDON as T_CREATE3_1_1_,
//        eagercateg1_.DESCRIPTION as DESCRIPT4_1_1_,
//                eagercateg1_.NAME as NAME5_1_1_,
//        eagercateg1_.T_UPDATEDON as T_UPDATE6_1_1_
//                from
//        TEST.CATEGORY eagercateg0_
//        inner join
//        TEST.CATEGORY_GROUP eagercateg1_
//        on eagercateg0_.CATEGORY_GROUP_ID=eagercateg1_.ID
//        where
//        eagercateg0_.ID=?
//        and eagercateg0_.CATEGORY_GROUP_TYPE='E'
//                -------------
//                select
//        debitcateg0_.CATEGORY_GROUP_ID as CATEGORY7_0_0_,
//                debitcateg0_.ID as ID2_0_0_,
//        debitcateg0_.ID as ID2_0_1_,
//                debitcateg0_.T_CREATEDON as T_CREATE3_0_1_,
//        debitcateg0_.DESCRIPTION as DESCRIPT4_0_1_,
//                debitcateg0_.NAME as NAME5_0_1_,
//        debitcateg0_.T_UPDATEDON as T_UPDATE6_0_1_,
//                debitcateg0_.CATEGORY_GROUP_ID as CATEGORY7_0_1_
//        from
//        TEST.CATEGORY debitcateg0_
//        where
//        debitcateg0_.CATEGORY_GROUP_ID=?
//        order by
//        debitcateg0_.NAME
//                -------------


        LOG.info("eagerCategoryDebit.getId(): " + eagerCategoryDebit.getId());
        LOG.info("eagerCategoryDebit.getCategoryGroupType(): " + eagerCategoryDebit.getCategoryGroupType());
        LOG.info("eagerCategoryDebit.getCategoryGroup(): " + eagerCategoryDebit.getCategoryGroup());

//        entityManagerRule.commit();

        LOG.info("end->test_15_getLoop");
    }

    @Test
    public void test_21_Update_debit() throws Exception {
        LOG.info("start->test_21_Update_debit");
        EntityManager em = entityManagerRule.getEntityManager();

        Query qId = em.createQuery("SELECT min(cg.id) from Category cg");
        Long id = ((Long) qId.getSingleResult());

        LOG.debug("min num = " + id);

        Category category = em.find(Category.class, id);

        //min 0 and max 100
        int randomInt = randomWithinRange.getRandom();
        LOG.debug("testUpdate.randomInt=" + randomInt);
        category.setName(categoryDebitName1 + randomInt);
        category.setDescription(defaultDescription + randomInt);

        em.persist(category);
        entityManagerRule.commit();

        LOG.debug(">>>category.getId()=" + category.getId());
        LOG.debug(">>>category.getCreatedOn()=" + category.getCreatedOn());
        LOG.debug(">>>category.getUpdatedOn()=" + category.getUpdatedOn());
        LOG.debug(category);

        assertEquals(true, category.getCreatedOn() != null);
        assertEquals(true, category.getUpdatedOn() != null);
        assertEquals(true, !category.getCreatedOn().equals(category.getUpdatedOn()));
        LOG.info("end->test_21_Update_debit");
    }

    @Test
    public void test_31_Delete_debit() throws Exception {
        LOG.info("start->test_31_Delete_debit");
        EntityManager em = entityManagerRule.getEntityManager();

        Query qId = em.createQuery("SELECT min(cg.id) as num from Category cg");
        Long id = ((Long) qId.getSingleResult());

        LOG.debug("min id = " + id);

        Category category = em.find(Category.class, id);
        CategoryGroup categoryGroup = category.getCategoryGroup();

        categoryGroup.removeCategory(category);

        em.persist(categoryGroup);
        em.remove(category);

        entityManagerRule.commit();

        Category categoryJustDeleted = em.find(Category.class, id);
        LOG.debug(">>>category=" + categoryJustDeleted);
        LOG.debug(category);

        assertEquals(null, categoryJustDeleted);
        LOG.info("end->test_31_Delete_debit");
    }
}