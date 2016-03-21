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
//        em.getTransaction().begin();

    }

    @After
    public void tearDown() throws Exception {
//        em.getTransaction().commit();
    }

    @Test
    public void testPersist() throws Exception{
        em.getTransaction().begin();
        LOG.info("start->testPersist");
        FamilyMember husband = new FamilyMember();
        husband.setName(familyMemberName);
        husband.setDescription("husband");

//        SessionFactory sf = new conf
//        Session session =
//        session.getTransaction().begin();


        em.persist(husband);
//        em.flush();
        em.getTransaction().commit();


        Long id = husband.getId();
        FamilyMember husbandJustInserted = em.find(FamilyMember.class, id);
        em.refresh(husbandJustInserted);
        System.out.println("husband.getId()=" + husbandJustInserted.getId());
        System.out.println("husband.getCreatedOn()=" + husbandJustInserted.getCreatedOn());
        System.out.println("husband.getUpdatedOn()=" + husbandJustInserted.getUpdatedOn());
        System.out.println(husband);

        assertEquals(husbandJustInserted.getCreatedOn() != null, true);
        assertEquals(husbandJustInserted.getUpdatedOn() != null, true);
        LOG.info("end->testPersist");
    }


    @Test
    public void testUpdate() throws Exception {
        em.getTransaction().begin();
//        //http://stackoverflow.com/questions/3574029/what-does-jpa-entitymanager-getsingleresult-return-for-a-count-query
//        NB : there's a difference between JQPL and Native query
//        Query query = em.createQuery("SELECT COUNT(p) FROM PersonEntity p " );
//        query.getSingleResult().getClass().getCanonicalName() --> java.lang.Long
//        Query query = em.createNativeQuery("SELECT COUNT(*) FROM PERSON " );
//        query.getSingleResult().getClass().getCanonicalName() --> java.math.BigInteger


//        Query qId = em.createNativeQuery("SELECT CURRVAL('TEST.SEQ_FAMILY_MEMBER') as num");
        Query qId = em.createQuery("SELECT max(f.id) as num from FamilyMember f");
        Long id = ((Long) qId.getSingleResult());

        System.out.println("seq curr num = " + id);

        FamilyMember husband = em.find(FamilyMember.class, id);
////OR
//        FamilyMember husband = (FamilyMember) em.createQuery("from FamilyMember as f where f.name = :familyMemberName")
//                .setParameter("familyMemberName", familyMemberName)
//                .setMaxResults(1) //I have multiple Freddy in table, so getSingleResult returns javax.persistence.NonUniqueResultException: result returns more than one elements
//                .getSingleResult();
//        Long id = husband.getId();
        husband.setName("Freddy18");
        husband.setDescription("husband18");

        em.persist(husband);
        em.getTransaction().commit();

        FamilyMember husbandJustUpdated = em.find(FamilyMember.class, id);
        em.refresh(husbandJustUpdated);

        System.out.println(">>>husband.getId()=" + husbandJustUpdated.getId());
        System.out.println(">>>husband.getCreatedOn()=" + husbandJustUpdated.getCreatedOn());
        System.out.println(">>>husband.getUpdatedOn()=" + husbandJustUpdated.getUpdatedOn());
        System.out.println(husband);

        assertEquals(husbandJustUpdated.getCreatedOn() != null, true);
        assertEquals(husbandJustUpdated.getUpdatedOn() != null, true);
        assertEquals(husbandJustUpdated.getCreatedOn() != husband.getUpdatedOn(), true);
    }

//    @Test
//    public void testGetDescription() throws Exception {
//
//    }
//
//    @Test
//    public void testSetDescription() throws Exception {
//
//    }
//
//    @Test
//    public void testGetId() throws Exception {
//
//    }
//
//    @Test
//    public void testSetId() throws Exception {
//
//    }
//
//    @Test
//    public void testGetName() throws Exception {
//
//    }
//
//    @Test
//    public void testSetName() throws Exception {
//
//    }
//
//    @Test
//    public void testEquals() throws Exception {
//
//    }
//
//    @Test
//    public void testHashCode() throws Exception {
//
//    }
//
//    @Test
//    public void testToString() throws Exception {
//
//    }
}