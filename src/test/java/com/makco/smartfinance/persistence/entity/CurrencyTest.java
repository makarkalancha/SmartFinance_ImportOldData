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
 * Created by mcalancea on 2016-03-28.
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class CurrencyTest {
    private final static Logger LOG = LogManager.getLogger(CurrencyTest.class);
    private static EntityManager em;
    private String currencyCode = "CAD";
    private String currencyName = "Canadian Dollar";
    private String currencyDesc = "Dollar that is used in Canada or Canada's dollar";

    private static int MIN = 1;
    private static int MAX = 100;

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
    public void test_11_Persist() throws Exception{
        LOG.info("start->testPersist");
        em.getTransaction().begin();
        Currency cad = new Currency();
        Random random = new Random();
        Integer randomInt = random.nextInt((MAX - 0) + MIN + 0);
        LOG.debug("testPersist.randomInt=" + randomInt);
        cad.setCode(randomInt.toString());
        cad.setName(currencyName);
        cad.setDescription(currencyDesc);

        em.persist(cad);
//        em.flush();
        em.getTransaction().commit();
        LOG.debug("INS: cad: .getId()=" + cad.getId());
        LOG.debug("INS: cad.getCreatedOn()=" + cad.getCreatedOn());
        LOG.debug("INS: cad.getUpdatedOn()=" + cad.getUpdatedOn());

        assertEquals(true, cad.getCreatedOn() != null);
        assertEquals(true, cad.getUpdatedOn() != null);
        LOG.info("end->testPersist");
    }

    @Test
    public void test_21_Update() throws Exception {
        LOG.info("start->testUpdate");
        em.getTransaction().begin();
        Query qId = em.createQuery("SELECT max(c.id) as num from Currency c");
        Long id = ((Long) qId.getSingleResult());

        LOG.debug("max num = " + id);

        Currency cad = em.find(Currency.class, id);
        Random random = new Random();
        //min 0 and max 100
        Integer randomInt = random.nextInt((MAX - 0) + MIN + 0);
        LOG.debug("testUpdate.randomInt=" + randomInt);
        String newName = randomInt.toString();
        String newDesc = currencyDesc + randomInt;
        cad.setName(newName);
        cad.setDescription(newDesc);

        em.persist(cad);
        em.getTransaction().commit();

        LOG.debug("UPD: cad.getId()=" + cad.getId());
        LOG.debug("UPD: cad.getCreatedOn()=" + cad.getCreatedOn());
        LOG.debug("UPD: cad.getUpdatedOn()=" + cad.getUpdatedOn());
        LOG.debug(cad);

        assert(cad.getCreatedOn() != null);
        assert(cad.getCreatedOn() != null);
        assert(cad.getCreatedOn() != null);
        assert(cad.getUpdatedOn() != null);
        assertEquals(true, !cad.getCreatedOn().equals(cad.getUpdatedOn()));
        LOG.info("end->testUpdate");
    }

    @Test
    public void test_31_Delete() throws Exception {
        LOG.info("start->testDelete");
        em.getTransaction().begin();
        Query qId = em.createQuery("SELECT min(c.id) as num from Currency c");
        Long id = ((Long) qId.getSingleResult());

        LOG.debug("min id = " + id);

        Currency cad = em.find(Currency.class, id);

        em.remove(cad);
        em.getTransaction().commit();

        Currency cadJustDeleted = em.find(Currency.class, id);
        LOG.debug(">>>cadJustDeleted=" + cadJustDeleted);
        LOG.debug(cad);

        assertEquals(null, cadJustDeleted);
        LOG.info("end->testDelete");
    }
}