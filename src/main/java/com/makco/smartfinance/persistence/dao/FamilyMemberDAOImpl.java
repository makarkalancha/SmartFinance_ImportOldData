package com.makco.smartfinance.persistence.dao;

import com.makco.smartfinance.persistence.entity.FamilyMember;
import com.makco.smartfinance.persistence.utils.HibernateUtil;
import com.makco.smartfinance.user_interface.constants.DialogMessages;
import java.util.ArrayList;
import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;

/**
 * Created by mcalancea on 2016-04-05.
 */
//http://programmers.stackexchange.com/questions/220909/service-layer-vs-dao-why-both
public class FamilyMemberDAOImpl implements FamilyMemberDAO {
    private final static Logger LOG = LogManager.getLogger(FamilyMemberDAOImpl.class);

    @Override
    public synchronized List<FamilyMember> familyMemberList() {
        Session session = null;
        List<FamilyMember> list = new ArrayList<>();
        try{
            session = HibernateUtil.openSession();
            session.beginTransaction();
            list = session.createQuery("SELECT f FROM FamilyMember f ORDER BY f.name").list();
            session.getTransaction().commit();

//        List<FamilyMember> list = new ArrayList<>();
//        EntityManager em = FinancePersistenceManager.INSTANCE.getEntityManager();
//        em.getTransaction().begin();
//        list = em.createQuery("FROM FamilyMember AS f ORDER BY f.name").getResultList();
//        em.getTransaction().commit();
//        em.close();
//        return list;
        } catch (Exception e) {
            session.getTransaction().rollback();
            DialogMessages.showExceptionAlert(e);
        } finally {
            if(session != null){
                session.close();
            }
        }
        return list;
    }

    @Override
    public synchronized void removeFamilyMember(Long id) {
        Session session = null;
        try{
            session = HibernateUtil.openSession();
            session.beginTransaction();
            FamilyMember familyMember = (FamilyMember) session.load(FamilyMember.class, id);
            session.delete(familyMember);
            session.getTransaction().commit();

//        EntityManager em = FinancePersistenceManager.INSTANCE.getEntityManager();
//        em.getTransaction().begin();
//        FamilyMember familyMember = (FamilyMember) em.find(FamilyMember.class, id);
//        em.remove(familyMember);
//        em.getTransaction().commit();
//        em.close();
        } catch (Exception e) {
            session.getTransaction().rollback();
            DialogMessages.showExceptionAlert(e);
        } finally {
            if(session != null){
                session.close();
            }
        }
    }

    @Override
    public synchronized FamilyMember getFamilyMemberById(Long id) {
        Session session = null;
        FamilyMember familyMember = null;
        try{
            session = HibernateUtil.openSession();
            session.beginTransaction();
            familyMember = (FamilyMember) session.get(FamilyMember.class, id);
            session.getTransaction().commit();

//        FamilyMember familyMember = null;
//        EntityManager em = FinancePersistenceManager.INSTANCE.getEntityManager();
//        em.getTransaction().begin();
//        familyMember = (FamilyMember) em.find(FamilyMember.class, id);
//        em.getTransaction().commit();
//        em.close();
//        return familyMember;
        } catch (Exception e) {
            session.getTransaction().rollback();
            DialogMessages.showExceptionAlert(e);
        } finally {
            if(session != null){
                session.close();
            }
        }
        return familyMember;
    }

    @Override
    public synchronized List<FamilyMember> getFamilyMemberByName(String name) {
        Session session = null;
        List<FamilyMember> familyMembers = new ArrayList<>();
        try {
            session = HibernateUtil.openSession();
            session.beginTransaction();
            familyMembers = session.createQuery("SELECT f FROM FamilyMember f WHERE name = :name ORDER BY f.name")
                    .setString("name", name)
                    .list();
            session.getTransaction().commit();

//        FamilyMember familyMember = null;
//        EntityManager em = FinancePersistenceManager.INSTANCE.getEntityManager();
//        em.getTransaction().begin();
//        familyMember = (FamilyMember) em.find(FamilyMember.class, id);
//        em.getTransaction().commit();
//        em.close();
//        return familyMember;
        } catch (Exception e) {
            session.getTransaction().rollback();
            DialogMessages.showExceptionAlert(e);
        } finally {
            if(session != null){
                session.close();
            }
        }
        return familyMembers;
    }

    @Override
    public synchronized void saveOrUpdateFamilyMember(FamilyMember familyMember) {
        Session session = null;
        try {
            session = HibernateUtil.openSession();
            session.beginTransaction();
            session.saveOrUpdate(familyMember);
            session.getTransaction().commit();
        } catch (Exception e) {
            session.getTransaction().rollback();
            DialogMessages.showExceptionAlert(e);
        } finally {
            if(session != null){
                session.close();
            }
        }
    }
}
