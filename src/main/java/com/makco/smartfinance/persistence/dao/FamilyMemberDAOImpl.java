package com.makco.smartfinance.persistence.dao;

import com.makco.smartfinance.persistence.entity.FamilyMember;
import com.makco.smartfinance.persistence.utils.HibernateUtil;
import java.util.ArrayList;
import java.util.List;
import org.hibernate.Session;

/**
 * Created by mcalancea on 2016-04-05.
 */
public class FamilyMemberDAOImpl implements FamilyMemberDAO {

    @Override
    public void addFamilyMember(FamilyMember familyMember) {
        Session session = HibernateUtil.openSession();
        session.beginTransaction();
        session.save(familyMember);
        session.getTransaction().commit();
        session.close();
    }

    @Override
    public List<FamilyMember> listFamilyMembers() {
        List<FamilyMember> list = new ArrayList<>();
        Session session = HibernateUtil.openSession();
        session.beginTransaction();
        list = session.createQuery("FROM FamilyMember AS f ORDER BY f.name").list();
        session.getTransaction().commit();
        session.close();
        return list;
    }

    @Override
    public void removeFamilyMember(Long id) {
        Session session = HibernateUtil.openSession();
        session.beginTransaction();
        FamilyMember familyMember = (FamilyMember) session.load(FamilyMember.class, id);
        session.delete(familyMember);
        session.getTransaction().commit();
        session.close();
    }

    @Override
    public void updateFamilyMember(FamilyMember familyMember) {
        Session session = HibernateUtil.openSession();
        session.beginTransaction();
        session.update(familyMember);
        session.getTransaction().commit();
        session.close();
    }
}
