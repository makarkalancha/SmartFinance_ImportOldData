package com.makco.smartfinance.persistence.entity;

import com.makco.smartfinance.persistence.utils.TestPersistenceManager;
import com.makco.smartfinance.utils.RandomWithinRange;
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
    private String currencyName = "Canadian Dollar";
    private String currencyDesc = "Dollar that is used in Canada or Canada's dollar";

    private static final int MIN = 100;
    private static final int MAX = 999;
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

    }

    @After
    public void tearDown() throws Exception {
//        em.close();
    }

    @Test
    public void test_11_Persist() throws Exception{
        LOG.info("start->test_11_Persist");
        em = TestPersistenceManager.INSTANCE.getEntityManager();
        em.getTransaction().begin();

        Currency cad = new Currency();
        Integer randomInt = randomWithinRange.getRandom();
        LOG.debug("testPersist.randomInt=" + randomInt);
        cad.setCode(randomInt.toString());
        cad.setName(currencyName);
        cad.setDescription(currencyDesc);

        em.persist(cad);
        em.getTransaction().commit();
        LOG.debug("INS: cad: .getId()=" + cad.getId());
        LOG.debug("INS: cad.getCreatedOn()=" + cad.getCreatedOn());
        LOG.debug("INS: cad.getUpdatedOn()=" + cad.getUpdatedOn());

        em.close();
        assertEquals(true, cad.getCreatedOn() != null);
        assertEquals(true, cad.getUpdatedOn() != null);
        LOG.info("end->testPersist");
    }

    @Test(expected = RollbackException.class)//persistence, not transaction
    //"IDX_UNQ_ORGNZTN_NM ON TEST.ORGANIZATION(NAME) VALUES ('TwinShop880921', 7)"
    public void test_12_PersistDuplicateName() throws Exception {
        LOG.info("start->test_12_PersistDuplicateName");
        em = TestPersistenceManager.INSTANCE.getEntityManager();
        em.getTransaction().begin();

        int randomInt = randomWithinRange.getRandom();
        LOG.debug("test_12_PersistDuplicateName.randomInt=" + randomInt);
        String currencyDuplicate = "TWN";
        String currencyDuplicateDesc = "twin desc";
        try {
            Currency currency1 = new Currency();
            currency1.setName(currencyDuplicate);
            currency1.setDescription(currencyDuplicateDesc);
            em.persist(currency1);

            Currency currency2 = new Currency();
            currency2.setName(currencyDuplicate);
            currency2.setDescription(currencyDuplicateDesc);
            em.persist(currency2);
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
        } finally {
            em.close();
        }
    }

    @Test
    public void test_13_Persist_multiple() throws Exception {
        LOG.info("start->test_13_Persist_multiple");
        em = TestPersistenceManager.INSTANCE.getEntityManager();
        em.getTransaction().begin();

        String currencyDescription = "multiple";
        for (int i = 0; i < 10; i++) {
            Currency cad = new Currency();
            Integer randomInt = randomWithinRange.getRandom();
            LOG.debug("testPersist.randomInt=" + randomInt);

            cad.setCode(randomInt.toString());
            cad.setName(currencyName + "_" + i);
            cad.setDescription(currencyDescription);

            em.persist(cad);
        }
        em.getTransaction().commit();

        List<Currency> multipleCurrencies = em.createQuery("select c from Currency c where c.description = :description")
                .setParameter("description", currencyDescription)
                .getResultList();

        em.close();
        LOG.info("test_13_Persist_multiple: multipleCurrencies.size() = " + multipleCurrencies.size());
        assertEquals(true, multipleCurrencies.size() > 0);
        LOG.info("end->test_13_Persist_multiple");
    }

    @Test
    public void test_21_Update() throws Exception {
        LOG.info("start->test_21_Update");
        em = TestPersistenceManager.INSTANCE.getEntityManager();
        em.getTransaction().begin();

        Query qId = em.createQuery("SELECT max(c.id) as num from Currency c");
        Long id = ((Long) qId.getSingleResult());

        LOG.debug("max num = " + id);

        Currency cad = em.find(Currency.class, id);
        Integer randomInt = randomWithinRange.getRandom();
        LOG.debug("testUpdate.randomInt=" + randomInt);
        String newName = randomInt.toString();
        String newDesc = currencyDesc + randomInt;
        cad.setName(newName);
        cad.setDescription(newDesc);

        em.persist(cad);
        em.getTransaction().commit();
        em.close();
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
        LOG.info("start->test_31_Delete");
        em = TestPersistenceManager.INSTANCE.getEntityManager();
        em.getTransaction().begin();

        Query qId = em.createQuery("SELECT min(c.id) as num from Currency c");
        Long id = ((Long) qId.getSingleResult());

        LOG.debug("min id = " + id);

        Currency cad = em.find(Currency.class, id);

        em.remove(cad);
        em.getTransaction().commit();

        Currency cadJustDeleted = em.find(Currency.class, id);
        em.close();
        LOG.debug(">>>cadJustDeleted=" + cadJustDeleted);
        LOG.debug(cad);

        assertEquals(null, cadJustDeleted);
        LOG.info("end->testDelete");
    }
}