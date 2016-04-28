package com.makco.smartfinance.persistence.entity;

import com.makco.smartfinance.persistence.utils.TestPersistenceManager;
import com.makco.smartfinance.utils.RandomWithinRange;
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
 * Created by mcalancea on 2016-03-02.
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class FamilyMemberTest {

    private final static Logger LOG = LogManager.getLogger(FamilyMemberTest.class);
    private static EntityManager em;
    private String familyMemberName = "Freddy";
    private String dublicateName = "Twin";
    private String defaultDescription = "husband or wife's man";

    private static int MIN = 1;
    private static int MAX = 1_000_000;
    private static RandomWithinRange randomWithinRange = new RandomWithinRange(MIN, MAX);

    @BeforeClass
    public static void setUpClass() throws Exception {

    }

    @AfterClass
    public static void tearDownClass() throws Exception {
        TestPersistenceManager.INSTANCE.close();
    }

    @Before
    public void setUp() throws Exception {
        //if opened in setUp and closed in tearDown, data is not saved in Db
//        em = TestPersistenceManager.INSTANCE.getEntityManager();
//        em.getTransaction().begin();
    }

    @After
    public void tearDown() throws Exception {
        //if opened in setUp and closed in tearDown, data is not saved in Db
//        em.close();
    }

    @Test
    public void test_11_Persist() throws Exception {
        LOG.info("start->test_11_Persist");
        FamilyMember husband = new FamilyMember();
        int randomInt = randomWithinRange.getRandom();
        LOG.debug("testPersist.randomInt=" + randomInt);
        husband.setName(familyMemberName + randomInt);
        husband.setDescription(defaultDescription);

        em.persist(husband);
//        em.flush();
        em.getTransaction().commit();
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
//            throw new RuntimeException(e);
            throw e;
        } finally {
//            if (em != null && em.isOpen()) {
//                em.close();
//            }
        }
    }

    @Test
    public void test_21_Update() throws Exception {
        LOG.info("start->test_21_Update");
        Query qId = em.createQuery("SELECT min(f.id) from FamilyMember f");
        Long id = ((Long) qId.getSingleResult());

        LOG.debug("min num = " + id);

        FamilyMember husband = em.find(FamilyMember.class, id);

        int randomInt = randomWithinRange.getRandom();
        LOG.debug("testUpdate.randomInt=" + randomInt);
        husband.setName(familyMemberName + randomInt);
        husband.setDescription(defaultDescription + randomInt);

        em.persist(husband);
        em.getTransaction().commit();

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
        Query qId = em.createQuery("SELECT min(f.id) as num from FamilyMember f");
        Long id = ((Long) qId.getSingleResult());

        LOG.debug("min id = " + id);

        FamilyMember husband = em.find(FamilyMember.class, id);

        em.remove(husband);
        em.getTransaction().commit();

        FamilyMember husbandJustDeleted = em.find(FamilyMember.class, id);
        LOG.debug(">>>husband=" + husbandJustDeleted);
        LOG.debug(husband);

        assertEquals(null, husbandJustDeleted);
        LOG.info("end->testDelete");
    }
}