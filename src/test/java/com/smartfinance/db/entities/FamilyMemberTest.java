package com.smartfinance.db.entities;

import com.smartfinance.db.utils.FinancePersistenceManager;
import javax.persistence.EntityManager;
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

    private static EntityManager em;

    @BeforeClass
    public static void setUpClass() throws Exception {
        em = FinancePersistenceManager.INSTANCE.getEntityManager();
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
        em.close();
        FinancePersistenceManager.INSTANCE.close();
    }

    @Before
    public void setUp() throws Exception {
        em.getTransaction().begin();

    }

    @After
    public void tearDown() throws Exception {
        em.getTransaction().commit();
    }

    @Test
    public void testPersist() throws Exception{

        FamilyMember husband = new FamilyMember();
        husband.setName("Freddy");
        husband.setDescription("husband");

//        SessionFactory sf = new conf
//        Session session =
//        session.getTransaction().begin();


        em.persist(husband);


        System.out.println("husband.getId()=" + husband.getId());
        System.out.println("husband.getCreatedOn()=" + husband.getCreatedOn());
        System.out.println("husband.getUpdatedOn()=" + husband.getUpdatedOn());
        System.out.println(husband);

        assertEquals(husband.getCreatedOn() != null, true);
        assertEquals(husband.getUpdatedOn() != null, true);
    }


    @Test
    public void testUpdate() throws Exception {
        FamilyMember husband = em.getReference(FamilyMember.class, 18L);
        husband.setName("Freddy18");
        husband.setDescription("husband18");

//        SessionFactory sf = new conf
//        Session session =
//        session.getTransaction().begin();

        em.persist(husband);

        System.out.println("husband.getId()=" + husband.getId());
        System.out.println("husband.getCreatedOn()=" + husband.getCreatedOn());
        System.out.println("husband.getUpdatedOn()=" + husband.getUpdatedOn());
        System.out.println(husband);

        assertEquals(husband.getCreatedOn() != null, true);
        assertEquals(husband.getUpdatedOn() != null, true);
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