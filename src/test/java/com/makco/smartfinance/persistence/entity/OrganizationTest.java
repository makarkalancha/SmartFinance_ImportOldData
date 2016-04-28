package com.makco.smartfinance.persistence.entity;

import com.makco.smartfinance.persistence.utils.TestPersistenceManager;
import com.makco.smartfinance.utils.RandomWithinRange;
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
 * Created by mcalancea on 2016-04-25.
 */

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class OrganizationTest {
    private final static Logger LOG = LogManager.getLogger(OrganizationTest.class);
    private static EntityManager em;
    private String organizationName = "Shop1";
    private String dublicateName = "TwinShop";
    private String defaultDescription = "shop's description";

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
        em = TestPersistenceManager.INSTANCE.getEntityManager();
        em.getTransaction().begin();
    }

    @After
    public void tearDown() throws Exception {
        em.close();
    }

    @Test
    public void test_11_Persist() throws Exception {
        LOG.info("start->test_11_Persist");
        Organization shop1 = new Organization();
        int randomInt = randomWithinRange.getRandom();
        LOG.debug("testPersist.randomInt=" + randomInt);
        shop1.setName(organizationName + randomInt);
        shop1.setDescription(defaultDescription);

        em.persist(shop1);
        em.getTransaction().commit();
        LOG.debug("shop1.getId()=" + shop1.getId());
        LOG.debug("shop1.getCreatedOn()=" + shop1.getCreatedOn());
        LOG.debug("shop1.getUpdatedOn()=" + shop1.getUpdatedOn());

        assertEquals(true, shop1.getCreatedOn() != null);
        assertEquals(true, shop1.getUpdatedOn() != null);
        LOG.info("end->testPersist");
    }

    @Test(expected = RollbackException.class)//persistence, not transaction
    //"IDX_UNQ_ORGNZTN_NM ON TEST.ORGANIZATION(NAME) VALUES ('TwinShop880921', 7)"
    public void test_12_PersistDuplicateName() throws Exception {
        LOG.info("start->test_12_PersistDuplicateName");
        int randomInt = randomWithinRange.getRandom();
        LOG.debug("test_12_PersistDuplicateName.randomInt=" + randomInt);
        try {
            Organization organization1 = new Organization();
            organization1.setName(dublicateName + randomInt);
            organization1.setDescription(defaultDescription);
            em.persist(organization1);

            Organization organization2 = new Organization();
            organization2.setName(dublicateName + randomInt);
            organization2.setDescription(defaultDescription);
            em.persist(organization2);
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
        }
    }

    @Test
    public void test_21_Update() throws Exception {
        LOG.info("start->test_21_Update");
        Query qId = em.createQuery("SELECT min(o.id) from Organization o");
        Long id = ((Long) qId.getSingleResult());

        LOG.debug("min num = " + id);

        Organization organization = em.find(Organization.class, id);

        int randomInt = randomWithinRange.getRandom();
        LOG.debug("testUpdate.randomInt=" + randomInt);
        organization.setName(organizationName + randomInt);
        organization.setDescription(defaultDescription + randomInt);

        em.persist(organization);
        em.getTransaction().commit();

        LOG.debug(">>>organization.getId()=" + organization.getId());
        LOG.debug(">>>organization.getCreatedOn()=" + organization.getCreatedOn());
        LOG.debug(">>>organization.getUpdatedOn()=" + organization.getUpdatedOn());
        LOG.debug(organization);

        assertEquals(true, organization.getCreatedOn() != null);
        assertEquals(true, organization.getUpdatedOn() != null);
        assertEquals(true, !organization.getCreatedOn().equals(organization.getUpdatedOn()));
        LOG.info("end->testUpdate");
    }

    @Test
    public void test_31_Delete() throws Exception {
        LOG.info("start->test_31_Delete");
        Query qId = em.createQuery("SELECT min(o.id) as num from Organization o");
        Long id = ((Long) qId.getSingleResult());

        LOG.debug("min id = " + id);

        Organization organization = em.find(Organization.class, id);

        em.remove(organization);
        em.getTransaction().commit();

        Organization organizationJustDeleted = em.find(Organization.class, id);
        LOG.debug(">>>organization=" + organizationJustDeleted);
        LOG.debug(organization);

        assertEquals(null, organizationJustDeleted);
        LOG.info("end->testDelete");
    }
}