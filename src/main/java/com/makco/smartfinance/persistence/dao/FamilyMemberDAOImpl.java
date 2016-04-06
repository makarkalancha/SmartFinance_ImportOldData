package com.makco.smartfinance.persistence.dao;

import com.makco.smartfinance.persistence.entity.FamilyMember;
import com.makco.smartfinance.persistence.utils.FinancePersistenceManager;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;

/**
 * Created by mcalancea on 2016-04-05.
 */
public class FamilyMemberDAOImpl implements FamilyMemberDAO {

    @Override
    public void addFamilyMember(FamilyMember familyMember) {
//        Session session = HibernateUtil.openSession();
//        session.beginTransaction();
//        session.save(familyMember);
//        session.getTransaction().commit();
//        session.close();

        EntityManager em = FinancePersistenceManager.INSTANCE.getEntityManager();
        em.getTransaction().begin();
        em.persist(familyMember);
//        em.flush();
        em.getTransaction().commit();
        em.close();
    }

    @Override
    public List<FamilyMember> listFamilyMembers() {
//        List<FamilyMember> list = new ArrayList<>();
//        Session session = HibernateUtil.openSession();
//        session.beginTransaction();
//        list = session.createQuery("FROM FamilyMember AS f ORDER BY f.name").list();
//        session.getTransaction().commit();
//        session.close();
//        return list;
        List<FamilyMember> list = new ArrayList<>();
        EntityManager em = FinancePersistenceManager.INSTANCE.getEntityManager();
        em.getTransaction().begin();
        list = em.createQuery("FROM FamilyMember AS f ORDER BY f.name").getResultList();
        em.getTransaction().commit();
        em.close();
        return list;
    }

    @Override
    public void removeFamilyMember(Long id) {
//        Session session = HibernateUtil.openSession();
//        session.beginTransaction();
//        FamilyMember familyMember = (FamilyMember) session.load(FamilyMember.class, id);
//        session.delete(familyMember);
//        session.getTransaction().commit();
//        session.close();

        EntityManager em = FinancePersistenceManager.INSTANCE.getEntityManager();
        em.getTransaction().begin();
        FamilyMember familyMember = (FamilyMember) em.find(FamilyMember.class, id);
        em.remove(familyMember);
        em.getTransaction().commit();
        em.close();
    }

    @Override
    public void updateFamilyMember(FamilyMember familyMember) {
//        Session session = HibernateUtil.openSession();
//        session.beginTransaction();
//        session.update(familyMember);
//        session.getTransaction().commit();
//        session.close();

        EntityManager em = FinancePersistenceManager.INSTANCE.getEntityManager();
        em.getTransaction().begin();
        em.persist(familyMember);
//        em.flush();
        em.getTransaction().commit();
        em.close();
    }

    @Override
    public FamilyMember getFamilyMemberById(Long id) {
//        FamilyMember familyMember = null;
//        Session session = HibernateUtil.openSession();
//        session.beginTransaction();
//        familyMember = (FamilyMember) session.load(FamilyMember.class, id);
//        session.getTransaction().commit();
//        session.close();
//        return familyMember;

        FamilyMember familyMember = null;
        EntityManager em = FinancePersistenceManager.INSTANCE.getEntityManager();
        em.getTransaction().begin();
        familyMember = (FamilyMember) em.find(FamilyMember.class, id);
        em.getTransaction().commit();
        em.close();
        return familyMember;
    }
}
