package com.makco.smartfinance.db.entities;

import com.makco.smartfinance.db.utils.TestPersistenceManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.Random;

import static org.junit.Assert.assertEquals;

/**
 * Created by Makar Kalancha on 2016-03-28.
 */
public class CurrencyTest {
    private final static Logger LOG = LogManager.getLogger(CurrencyTest.class);
    private static EntityManager em;
    private String currencyCode = "CAD";
    private String currencyName = "Canadian Dollar";
    private String currencyDesc = "Dollar that is used in Canada";

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

    @Test
    public void testPersist() throws Exception{
        LOG.info("start->testPersist");
        em.getTransaction().begin();
        Currency cad = new Currency();
        cad.setCode(currencyCode);
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
    public void testUpdate() throws Exception {
        LOG.info("start->testUpdate");
        em.getTransaction().begin();
        Query qId = em.createQuery("SELECT max(c.id) as num from Currency c");
        Long id = ((Long) qId.getSingleResult());

        LOG.debug("max num = " + id);

        Currency cad = em.find(Currency.class, id);
        Random random = new Random();
        //min 0 and max 100
        int randomInt = random.nextInt((100 - 0) + 1 + 0);
        String newName = currencyName + randomInt;
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
}