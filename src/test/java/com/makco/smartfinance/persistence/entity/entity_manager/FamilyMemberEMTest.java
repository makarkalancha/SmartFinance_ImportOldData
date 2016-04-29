package com.makco.smartfinance.persistence.entity.entity_manager;

import com.makco.smartfinance.persistence.entity.Currency;
import com.makco.smartfinance.persistence.entity.FamilyMember;
import com.makco.smartfinance.utils.RandomWithinRange;
import com.makco.smartfinance.utils.rules.EntityManagerRule;
import java.util.List;
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
import org.junit.Rule;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import static org.junit.Assert.assertEquals;

/**
 * Created by mcalancea on 2016-03-02.
 */
//http://blog.schauderhaft.de/2011/03/13/testing-databases-with-junit-and-hibernate-part-1-one-to-rule-them/
//:database_engine:flywayClean is run after every test class, so DB is clean after testing
//if you want to see data in DB, comment 2 rows in database_engine/build.gradle: flywayMigrate.dependsOn(flywayClean) & test.dependsOn(flywayMigrate)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class FamilyMemberEMTest {
    private final static Logger LOG = LogManager.getLogger(FamilyMemberEMTest.class);

    private static int MIN = 1;
    private static int MAX = 1_000_000;
    private static RandomWithinRange randomWithinRange = new RandomWithinRange(MIN, MAX);

    private String familyMemberName = "Freddy";
    private String dublicateName = "Twin";
    private String defaultDescription = "husband or wife's man";

    @Rule
    public final EntityManagerRule entityManagerRule = new EntityManagerRule();

    @BeforeClass
    public static void setUpClass() throws Exception {
        LOG.debug(">>>FamilyMemberEMTest.setUpClass");
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
        LOG.debug(">>>FamilyMemberEMTest.tearDownClass");
    }

    @Before
    public void setUp() throws Exception {
        LOG.debug(">>>FamilyMemberEMTest.setUp");
    }

    @After
    public void tearDown() throws Exception {
        LOG.debug(">>>FamilyMemberEMTest.tearDown");
    }

    @Test
    public void test_11_Persist() throws Exception {
        LOG.info("start->test_11_Persist");
        EntityManager em = entityManagerRule.getEntityManager();
        
        FamilyMember husband = new FamilyMember();
        int randomInt = randomWithinRange.getRandom();
        LOG.debug("testPersist.randomInt=" + randomInt);
        husband.setName(familyMemberName + randomInt);
        husband.setDescription(defaultDescription);

        em.persist(husband);
//        em.flush();
        entityManagerRule.commit();
        LOG.debug("husband.getId()=" + husband.getId());
        LOG.debug("husband.getCreatedOn()=" + husband.getCreatedOn());
        LOG.debug("husband.getUpdatedOn()=" + husband.getUpdatedOn());

        assertEquals(true, husband.getCreatedOn() != null);
        assertEquals(true, husband.getUpdatedOn() != null);
        LOG.info("end->testPersist");
    }

    @Test(expected = RollbackException.class)//persistence or transaction
    //Unique index or primary key violation: "IDX_UNQ_FMLMMBR_NM ON TEST.FAMILY_MEMBER(NAME) VALUES ('Twin516576', 2)"
    public void test_12_PersistDuplicateName() throws Exception {
        LOG.info("start->test_12_PersistDuplicateName");
        EntityManager em = entityManagerRule.getEntityManager();

        int randomInt = randomWithinRange.getRandom();
        LOG.debug("test_12_PersistDuplicateName.randomInt=" + randomInt);
        try {
            FamilyMember husband1 = new FamilyMember();
            husband1.setName(dublicateName + randomInt);
            husband1.setDescription(defaultDescription);
            em.persist(husband1);

            FamilyMember husband2 = new FamilyMember();
            husband2.setName(dublicateName + randomInt);
            husband2.setDescription(defaultDescription);
            em.persist(husband2);
            entityManagerRule.commit();
        } catch (Exception e) {
            try {
                if (entityManagerRule.isActive()) {
                    entityManagerRule.rollback();
                }
            } catch (Exception rbEx) {
                System.err.println("Rollback of transaction failed, trace follows!");
                rbEx.printStackTrace(System.err);
            }
//            throw new RuntimeException(e);
            throw e;
//        } finally {
//            if (em != null && em.isOpen()) {
//                em.close();
//            }
        }
    }

    @Test
    public void test_13_Persist_multiple() throws Exception {
        LOG.info("start->test_13_Persist_multiple");
        EntityManager em = entityManagerRule.getEntityManager();

        String fmDescription = "multiple";
        for (int i = 0; i < 10; i++) {
            FamilyMember fm = new FamilyMember();
            fm.setName(familyMemberName + "_" + i);
            fm.setDescription(fmDescription);

            em.persist(fm);
        }
        entityManagerRule.commit();

        List<Currency> multipleFM = em.createQuery("select f from FamilyMember f where f.description = :description")
                .setParameter("description", fmDescription)
                .getResultList();

//        entityManagerRule.commit();

        LOG.info("test_13_Persist_multiple: multipleFM.size() = " + multipleFM.size());
        assertEquals(true, multipleFM.size() > 0);
        LOG.info("end->test_13_Persist_multiple");
    }

    @Test
    public void test_21_Update() throws Exception {
        LOG.info("start->test_21_Update");
        EntityManager em = entityManagerRule.getEntityManager();

        Query qId = em.createQuery("SELECT min(f.id) from FamilyMember f");
        Long id = ((Long) qId.getSingleResult());

        LOG.debug("min num = " + id);

        FamilyMember husband = em.find(FamilyMember.class, id);

        int randomInt = randomWithinRange.getRandom();
        LOG.debug("testUpdate.randomInt=" + randomInt);
        husband.setName(familyMemberName + randomInt);
        husband.setDescription(defaultDescription + randomInt);

        em.persist(husband);
        entityManagerRule.commit();

        LOG.debug(">>>husband.getId()=" + husband.getId());
        LOG.debug(">>>husband.getCreatedOn()=" + husband.getCreatedOn());
        LOG.debug(">>>husband.getUpdatedOn()=" + husband.getUpdatedOn());
        LOG.debug(husband);

        assertEquals(true, husband.getCreatedOn() != null);
        assertEquals(true, husband.getUpdatedOn() != null);
        assertEquals(true, !husband.getCreatedOn().equals(husband.getUpdatedOn()));
        LOG.info("end->testUpdate");
    }

    @Test
    public void test_31_Delete() throws Exception {
        LOG.info("start->test_31_Delete");
        EntityManager em = entityManagerRule.getEntityManager();

        Query qId = em.createQuery("SELECT min(f.id) as num from FamilyMember f");
        Long id = ((Long) qId.getSingleResult());

        LOG.debug("min id = " + id);

        FamilyMember husband = em.find(FamilyMember.class, id);

        em.remove(husband);
        entityManagerRule.commit();

        FamilyMember husbandJustDeleted = em.find(FamilyMember.class, id);
        LOG.debug(">>>husband=" + husbandJustDeleted);
        LOG.debug(husband);

        assertEquals(null, husbandJustDeleted);
        LOG.info("end->testDelete");
    }
}