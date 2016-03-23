package com.makco.smartfinance.db.entities;

import com.makco.smartfinance.db.utils.TestPersistenceManager;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.Random;

import static org.junit.Assert.assertEquals;

/**
 * Created by mcalancea on 2016-03-02.
 */
public class FamilyMemberTest {
    private final static Logger LOG = LogManager.getLogger(FamilyMemberTest.class);
    private static EntityManager em;
    private String familyMemberName = "Freddy";

    @BeforeClass
    public static void setUpClass() throws Exception {
        em = TestPersistenceManager.INSTANCE.getEntityManager();
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
        em.close();
        TestPersistenceManager.INSTANCE.close();
    }

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void testPersist() throws Exception{
        LOG.info("start->testPersist");
        em.getTransaction().begin();
        FamilyMember husband = new FamilyMember();
        husband.setName(familyMemberName);
        husband.setDescription("husband");

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

    @Test
    public void testUpdate() throws Exception {
        LOG.info("start->testUpdate");
        em.getTransaction().begin();
        Query qId = em.createQuery("SELECT max(f.id) as num from FamilyMember f");
        Long id = ((Long) qId.getSingleResult());

        LOG.debug("max num = " + id);

        FamilyMember husband = em.find(FamilyMember.class, id);
        Random random = new Random();
        //min 0 and max 100
        int randomInt = random.nextInt((100 - 0) + 1 + 0);
        husband.setName("Freddy" + randomInt);
        husband.setDescription("husband" + randomInt);

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
    public void testDelete() throws Exception {
        LOG.info("start->testDelete");
        em.getTransaction().begin();
        Query qId = em.createQuery("SELECT min(f.id) as num from FamilyMember f where f.isDeleted = false");
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