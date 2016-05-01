package com.makco.smartfinance.persistence.entity.entity_manager;

import com.makco.smartfinance.persistence.entity.entity_manager.test_entities.EagerCategoryDebit;
import com.makco.smartfinance.persistence.entity.entity_manager.test_entities.EagerCategoryGroupDebit;
import com.makco.smartfinance.persistence.utils.TestPersistenceManager;
import com.makco.smartfinance.utils.RandomWithinRange;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Test;

import javax.persistence.EntityManager;
import javax.persistence.Query;

/**
 * User: Makar Kalancha
 * Date: 30/04/2016
 * Time: 23:22
 */
public class CategoryEMEagerTest {
    private final static Logger LOG = LogManager.getLogger(CategoryEMEagerTest.class);
    private static int MIN = 1;
    private static int MAX = 1_000_000;
    private static RandomWithinRange randomWithinRange = new RandomWithinRange(MIN, MAX);

    @Test
    public void test_15_getLoop() throws Exception{
        //catGr eager, cat eager and bidirect;
        //get cat -> get cat gr -> get collection of cat => cartesian product
        LOG.info("start->test_15_getLoop");
        int randomInt = randomWithinRange.getRandom();

        EntityManager em = TestPersistenceManager.INSTANCE.getEntityManager();
        em.getTransaction().begin();
        EagerCategoryGroupDebit eagerCategoryGroupDebit =
                new EagerCategoryGroupDebit("eager_cg_name_" + randomInt, "eager_cg_desc_" + randomInt);
        em.persist(eagerCategoryGroupDebit);

        for (int i = 0; i < 10; i++) {
            EagerCategoryDebit eagerCategoryDebit = new EagerCategoryDebit(
                    eagerCategoryGroupDebit,
                    i + "eager_cat_name_" + randomInt,
                    i + "eager_cat_desc_" + randomInt);
            eagerCategoryGroupDebit.addCategory(eagerCategoryDebit);
        }
        em.getTransaction().commit();
        em.close();
//        entityManagerRule.getEntityManager().close();

///////////////////////////////////////////////////////////
        EntityManager em1 = TestPersistenceManager.INSTANCE.getEntityManager();
        em1.getTransaction().begin();

        Query qId = em1.createQuery("SELECT min(a.id) from EagerCategoryDebit a");
        LOG.debug(">>>EagerCategoryDebit: SELECT min(a.id) from EagerCategoryDebit a");
        Long id = ((Long) qId.getSingleResult());
        LOG.debug(">>>EagerCategoryDebit min id = " + id);

        EagerCategoryDebit eagerCategoryDebit = em1.find(EagerCategoryDebit.class, id);

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
        em1.getTransaction().commit();
        em1.close();

        LOG.info("eagerCategoryDebit.getId(): " + eagerCategoryDebit.getId());
        LOG.info("eagerCategoryDebit.getCategoryGroupType(): " + eagerCategoryDebit.getCategoryGroupType());
        LOG.info("eagerCategoryDebit.getCategoryGroup(): " + eagerCategoryDebit.getCategoryGroup());

//        entityManagerRule.commit();

        LOG.info("end->test_15_getLoop");
    }
}
