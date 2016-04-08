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
            DialogMessages.showExceptionAlert(e);
        }
    }

    @Override
    public List<FamilyMember> listFamilyMembers() {
        List<FamilyMember> list = new ArrayList<>();
        try{
            Session session = HibernateUtil.openSession();
            session.beginTransaction();
            list = session.createQuery("FROM FamilyMember AS f ORDER BY f.name").list();
            session.getTransaction().commit();
            session.close();

//        List<FamilyMember> list = new ArrayList<>();
//        EntityManager em = FinancePersistenceManager.INSTANCE.getEntityManager();
//        em.getTransaction().begin();
//        list = em.createQuery("FROM FamilyMember AS f ORDER BY f.name").getResultList();
//        em.getTransaction().commit();
//        em.close();
//        return list;
        } catch (Exception e) {
            DialogMessages.showExceptionAlert(e);
        }
        return list;
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
            DialogMessages.showExceptionAlert(e);
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
            DialogMessages.showExceptionAlert(e);
        }
    }

    @Override
    public FamilyMember getFamilyMemberById(Long id) {
        FamilyMember familyMember = null;
        try{
            Session session = HibernateUtil.openSession();
            session.beginTransaction();
            familyMember = (FamilyMember) session.get(FamilyMember.class, id);
            session.getTransaction().commit();
            session.close();


//        FamilyMember familyMember = null;
//        EntityManager em = FinancePersistenceManager.INSTANCE.getEntityManager();
//        em.getTransaction().begin();
//        familyMember = (FamilyMember) em.find(FamilyMember.class, id);
//        em.getTransaction().commit();
//        em.close();
//        return familyMember;
        } catch (Exception e) {
            DialogMessages.showExceptionAlert(e);
        }
        return familyMember;
    }

    @Override
    public List<FamilyMember> getFamilyMemberByName(String name) {
        List<FamilyMember> familyMembers = new ArrayList<>();
        try {
            Session session = HibernateUtil.openSession();
            session.beginTransaction();
            familyMembers = session.createQuery("FROM FamilyMember AS f WHERE name = :name ORDER BY f.name")
                    .setString("name", name)
                    .list();
            session.getTransaction().commit();
            session.close();

//        FamilyMember familyMember = null;
//        EntityManager em = FinancePersistenceManager.INSTANCE.getEntityManager();
//        em.getTransaction().begin();
//        familyMember = (FamilyMember) em.find(FamilyMember.class, id);
//        em.getTransaction().commit();
//        em.close();
//        return familyMember;
        } catch (Exception e) {
            DialogMessages.showExceptionAlert(e);
        }
        return familyMembers;
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
            DialogMessages.showExceptionAlert(e);
        }
    }
}
