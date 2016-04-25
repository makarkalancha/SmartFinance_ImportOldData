package com.makco.smartfinance.persistence.entity;

import com.makco.smartfinance.persistence.utils.TestPersistenceManager;
import java.util.Random;
import javax.persistence.EntityManager;
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

    @BeforeClass
    public static void setUpClass() throws Exception {
        em = TestPersistenceManager.INSTANCE.getEntityManager();
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
//        em.close();
//        TestPersistenceManager.INSTANCE.close();
    }

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }

//    @Test
//    public void testCRUD() throws Exception{
//        testPersist();
//        testUpdate();
//        testDelete();
//    }

    @Test
    public void test_11_Persist() throws Exception {
        LOG.info("start->testPersist");
        em.getTransaction().begin();
        FamilyMember husband = new FamilyMember();
        Random random = new Random();
        int randomInt = random.nextInt((MAX - 0) + MIN + 0);
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

//    @Test(expected = RollbackException.class)//persistence or transaction
    @Test
    public void test_12_PersistDuplicateName() throws Exception {
        LOG.info("start->testPersistDuplicateName");
        Random random = new Random();
        int randomInt = random.nextInt((MAX - 0) + MIN + 0);
        LOG.debug("testPersistDuplicateName.randomInt=" + randomInt);
        em.getTransaction().begin();
        FamilyMember husband1 = new FamilyMember();
        husband1.setName(dublicateName + randomInt);
        husband1.setDescription(defaultDescription);
        em.persist(husband1);

        FamilyMember husband2 = new FamilyMember();
        husband2.setName(dublicateName + randomInt);
        husband2.setDescription(defaultDescription);
        em.persist(husband2);

        em.getTransaction().commit();
    }

    @Test
    public void test_21_Update() throws Exception {
        LOG.info("start->testUpdate");
        em.getTransaction().begin();
        Query qId = em.createQuery("SELECT min(f.id) from FamilyMember f");
        Long id = ((Long) qId.getSingleResult());

        LOG.debug("min num = " + id);

        FamilyMember husband = em.find(FamilyMember.class, id);

        //min 0 and max 100
        Random random = new Random();
        int randomInt = random.nextInt((MAX - 0) + MIN + 0);
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
        LOG.info("start->testDelete");
        em.getTransaction().begin();
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