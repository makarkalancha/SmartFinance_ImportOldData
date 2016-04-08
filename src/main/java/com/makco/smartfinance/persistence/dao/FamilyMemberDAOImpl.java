package com.makco.smartfinance.persistence.dao;

import com.makco.smartfinance.persistence.entity.FamilyMember;
import com.makco.smartfinance.persistence.utils.HibernateUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mcalancea on 2016-04-05.
 */
public class FamilyMemberDAOImpl implements FamilyMemberDAO {
    private final static Logger LOG = LogManager.getLogger(FamilyMemberDAOImpl.class);
    @Override
    public void addFamilyMember(FamilyMember familyMember) {
        try {
            Session session = HibernateUtil.openSession();
            session.beginTransaction();
            session.save(familyMember);
            session.getTransaction().commit();
            session.close();

//        EntityManager em = FinancePersistenceManager.INSTANCE.getEntityManager();
//        em.getTransaction().begin();
//        em.persist(familyMember);
////        em.flush();
//        em.getTransaction().commit();
//        em.close();
        } catch (Exception e) {
            LOG.error(e, e);
            throw e;
        }
    }

    @Override
    public List<FamilyMember> listFamilyMembers() {
        try{
            List<FamilyMember> list = new ArrayList<>();
            Session session = HibernateUtil.openSession();
            session.beginTransaction();
            list = session.createQuery("FROM FamilyMember AS f ORDER BY f.name").list();
            session.getTransaction().commit();
            session.close();
            return list;
//        List<FamilyMember> list = new ArrayList<>();
//        EntityManager em = FinancePersistenceManager.INSTANCE.getEntityManager();
//        em.getTransaction().begin();
//        list = em.createQuery("FROM FamilyMember AS f ORDER BY f.name").getResultList();
//        em.getTransaction().commit();
//        em.close();
//        return list;
        } catch (Exception e) {
            LOG.error(e, e);
            throw e;
        }
    }

    @Override
    public void removeFamilyMember(Long id) {
        try{
            Session session = HibernateUtil.openSession();
            session.beginTransaction();
            FamilyMember familyMember = (FamilyMember) session.load(FamilyMember.class, id);
            session.delete(familyMember);
            session.getTransaction().commit();
            session.close();

//        EntityManager em = FinancePersistenceManager.INSTANCE.getEntityManager();
//        em.getTransaction().begin();
//        FamilyMember familyMember = (FamilyMember) em.find(FamilyMember.class, id);
//        em.remove(familyMember);
//        em.getTransaction().commit();
//        em.close();
        } catch (Exception e) {
            LOG.error(e, e);
            throw e;
        }
    }

    @Override
    public void updateFamilyMember(FamilyMember familyMember) {
        try{
            Session session = HibernateUtil.openSession();
            session.beginTransaction();
            session.update(familyMember);
            session.getTransaction().commit();
            session.close();

//        EntityManager em = FinancePersistenceManager.INSTANCE.getEntityManager();
//        em.getTransaction().begin();
//        em.persist(familyMember);
////        em.flush();
//        em.getTransaction().commit();
//        em.close();
        } catch (Exception e) {
            LOG.error(e, e);
            throw e;
        }
    }

    @Override
    public FamilyMember getFamilyMemberById(Long id) {
        try{
            FamilyMember familyMember = null;
            Session session = HibernateUtil.openSession();
            session.beginTransaction();
            familyMember = (FamilyMember) session.get(FamilyMember.class, id);
            session.getTransaction().commit();
            session.close();
            return familyMember;

//        FamilyMember familyMember = null;
//        EntityManager em = FinancePersistenceManager.INSTANCE.getEntityManager();
//        em.getTransaction().begin();
//        familyMember = (FamilyMember) em.find(FamilyMember.class, id);
//        em.getTransaction().commit();
//        em.close();
//        return familyMember;
        } catch (Exception e) {
            LOG.error(e, e);
            throw e;
        }
    }

    @Override
    public void saveOrUpdateFamilyMember(FamilyMember familyMember) {
        try {
            Session session = HibernateUtil.openSession();
            session.beginTransaction();
            session.saveOrUpdate(familyMember);
            session.getTransaction().commit();
            session.close();
        } catch (Exception e) {
            LOG.error(e, e);
            throw e;
        }
    }
}
