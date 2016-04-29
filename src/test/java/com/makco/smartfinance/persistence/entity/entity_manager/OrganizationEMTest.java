package com.makco.smartfinance.persistence.entity.entity_manager;

import com.makco.smartfinance.persistence.entity.Currency;
import com.makco.smartfinance.persistence.entity.Organization;
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
 * Created by mcalancea on 2016-04-25.
 */

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class OrganizationEMTest {
    private final static Logger LOG = LogManager.getLogger(OrganizationEMTest.class);
    private String organizationName = "Shop1";
    private String dublicateName = "TwinShop";
    private String defaultDescription = "shop's description";

    private static int MIN = 1;
    private static int MAX = 1_000_000;
    private static RandomWithinRange randomWithinRange = new RandomWithinRange(MIN, MAX);

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

    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void test_11_Persist() throws Exception {
        LOG.info("start->test_11_Persist");
        EntityManager em = entityManagerRule.getEntityManager();

        Organization shop1 = new Organization();
        int randomInt = randomWithinRange.getRandom();
        LOG.debug("testPersist.randomInt=" + randomInt);
        shop1.setName(organizationName + randomInt);
        shop1.setDescription(defaultDescription);

        em.persist(shop1);
        entityManagerRule.commit();
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
        EntityManager em = entityManagerRule.getEntityManager();

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
    public void test_13_Persist_multiple() throws Exception {
        LOG.info("start->test_13_Persist_multiple");
        EntityManager em = entityManagerRule.getEntityManager();

        int randomInt = randomWithinRange.getRandom();
        String orgDescription = "multiple";
        for (int i = 0; i < 10; i++) {
            Organization shop1 = new Organization();
            shop1.setName(i + "_" + organizationName + "_" + randomInt);
            shop1.setDescription(orgDescription);

            em.persist(shop1);
        }
        entityManagerRule.commit();

        List<Currency> multipleOrg = em.createQuery("select o from Organization o where o.description = :description")
                .setParameter("description", orgDescription)
                .getResultList();

//        entityManagerRule.commit();

        LOG.info("test_13_Persist_multiple: multipleOrg.size() = " + multipleOrg.size());
        assertEquals(true, multipleOrg.size() > 0);
        LOG.info("end->test_13_Persist_multiple");
    }

    @Test
    public void test_21_Update() throws Exception {
        LOG.info("start->test_21_Update");
        EntityManager em = entityManagerRule.getEntityManager();

        Query qId = em.createQuery("SELECT min(o.id) from Organization o");
        Long id = ((Long) qId.getSingleResult());

        LOG.debug("min num = " + id);

        Organization organization = em.find(Organization.class, id);

        int randomInt = randomWithinRange.getRandom();
        LOG.debug("testUpdate.randomInt=" + randomInt);
        organization.setName(organizationName + randomInt);
        organization.setDescription(defaultDescription + randomInt);

        em.persist(organization);
        entityManagerRule.commit();

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
        EntityManager em = entityManagerRule.getEntityManager();

        Query qId = em.createQuery("SELECT min(o.id) as num from Organization o");
        Long id = ((Long) qId.getSingleResult());

        LOG.debug("min id = " + id);

        Organization organization = em.find(Organization.class, id);

        em.remove(organization);
        entityManagerRule.commit();

        Organization organizationJustDeleted = em.find(Organization.class, id);
        LOG.debug(">>>organization=" + organizationJustDeleted);
        LOG.debug(organization);

        assertEquals(null, organizationJustDeleted);
        LOG.info("end->testDelete");
    }
}