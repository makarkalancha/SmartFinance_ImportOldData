package com.smartfinance.db.entities;

import com.smartfinance.db.utils.FinancePersistenceManager;
import javax.persistence.EntityManager;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Created by mcalancea on 2016-03-02.
 */
public class FamilyMemberTest {

    @Test
    public void testPersist() throws Exception{
        FamilyMember husband = new FamilyMember();
        husband.setName("Freddy");
        husband.setDescription("husband");

//        SessionFactory sf = new conf
//        Session session =
//        session.getTransaction().begin();
        EntityManager em = FinancePersistenceManager.INSTANCE.getEntityManager();
        em.getTransaction().begin();

        em.persist(husband);
        em.getTransaction().commit();

        System.out.println("husband.getId()=" + husband.getId());
        System.out.println("husband.getCreatedOn()=" + husband.getCreatedOn());
        System.out.println("husband.getUpdatedOn()=" + husband.getUpdatedOn());

        assertEquals(husband.getCreatedOn() != null, true);
        assertEquals(husband.getUpdatedOn() != null, true);

        em.close();
        FinancePersistenceManager.INSTANCE.close();

        assert (true);
    }

    @Test
    public void testGetDescription() throws Exception {

    }

    @Test
    public void testSetDescription() throws Exception {

    }

    @Test
    public void testGetId() throws Exception {

    }

    @Test
    public void testSetId() throws Exception {

    }

    @Test
    public void testGetName() throws Exception {

    }

    @Test
    public void testSetName() throws Exception {

    }

    @Test
    public void testEquals() throws Exception {

    }

    @Test
    public void testHashCode() throws Exception {

    }

    @Test
    public void testToString() throws Exception {

    }
}