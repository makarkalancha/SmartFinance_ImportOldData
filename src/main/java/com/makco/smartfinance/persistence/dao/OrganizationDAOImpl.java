package com.makco.smartfinance.persistence.dao;

import com.makco.smartfinance.persistence.entity.Organization;
import com.makco.smartfinance.persistence.utils.HibernateUtil;
import java.util.ArrayList;
import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.resource.transaction.spi.TransactionStatus;

/**
 * Created by mcalancea on 2016-04-05.
 */
//http://programmers.stackexchange.com/questions/220909/service-layer-vs-dao-why-both
public class OrganizationDAOImpl implements OrganizationDAO {
    private final static Logger LOG = LogManager.getLogger(OrganizationDAOImpl.class);

    @Override
    public synchronized Organization getOrganizationById(Long id) throws Exception {
        Session session = null;
        Organization organization = null;
        try{
            session = HibernateUtil.openSession();
            session.beginTransaction();
            organization = session.get(Organization.class, id);
            session.getTransaction().commit();
        } catch (Exception e) {
            try {
                if (session.getTransaction().getStatus() == TransactionStatus.ACTIVE
                        || session.getTransaction().getStatus() == TransactionStatus.MARKED_ROLLBACK)
                    session.getTransaction().rollback();
            } catch (Exception rbEx) {
                LOG.error("Rollback of transaction failed, trace follows!");
                LOG.error(rbEx, rbEx);
            }
            throw new RuntimeException(e);
        } finally {
            if(session != null){
                session.close();
            }
        }
        return organization;
    }

    @Override
    public synchronized List<Organization> organizationList() throws Exception {
        Session session = null;
        List<Organization> list = new ArrayList<>();
        try {
            session = HibernateUtil.openSession();
            session.beginTransaction();
            list = session.createQuery("SELECT o FROM Organization o ORDER BY o.name").list();
            session.getTransaction().commit();
        } catch (Exception e) {
            try {
                if (session.getTransaction().getStatus() == TransactionStatus.ACTIVE
                        || session.getTransaction().getStatus() == TransactionStatus.MARKED_ROLLBACK)
                    session.getTransaction().rollback();
            } catch (Exception rbEx) {
                LOG.error("Rollback of transaction failed, trace follows!");
                LOG.error(rbEx, rbEx);
            }
            throw new RuntimeException(e);
        } finally {
            if (session != null) {
                session.close();
            }
        }
        return list;
    }

    @Override
    public synchronized List<Organization> getOrganizationByName(String name) throws Exception {
        Session session = null;
        List<Organization> organizations = new ArrayList<>();
        try {
            session = HibernateUtil.openSession();
            session.beginTransaction();
            organizations = session.createQuery("SELECT o FROM Organization o WHERE name = :name ORDER BY o.name")
                    .setString("name", name)
                    .list();
            session.getTransaction().commit();
        } catch (Exception e) {
            try {
                if (session.getTransaction().getStatus() == TransactionStatus.ACTIVE
                        || session.getTransaction().getStatus() == TransactionStatus.MARKED_ROLLBACK)
                    session.getTransaction().rollback();
            } catch (Exception rbEx) {
                LOG.error("Rollback of transaction failed, trace follows!");
                LOG.error(rbEx, rbEx);
            }
            throw new RuntimeException(e);
        } finally {
            if(session != null){
                session.close();
            }
        }
        return organizations;
    }

    @Override
    public synchronized void removeOrganization(Long id) throws Exception {
        Session session = null;
        try{
            session = HibernateUtil.openSession();
            session.beginTransaction();
            Organization organization = session.load(Organization.class, id);
            session.delete(organization);
            session.getTransaction().commit();
        } catch (Exception e) {
            try {
                if (session.getTransaction().getStatus() == TransactionStatus.ACTIVE
                        || session.getTransaction().getStatus() == TransactionStatus.MARKED_ROLLBACK)
                    session.getTransaction().rollback();
            } catch (Exception rbEx) {
                LOG.error("Rollback of transaction failed, trace follows!");
                LOG.error(rbEx, rbEx);
            }
            throw new RuntimeException(e);
        } finally {
            if(session != null){
                session.close();
            }
        }
    }

    @Override
    public synchronized void saveOrUpdateOrganization(Organization organization) throws Exception {
        Session session = null;
        try {
            session = HibernateUtil.openSession();
            session.beginTransaction();
            session.saveOrUpdate(organization);
            session.getTransaction().commit();
        } catch (Exception e) {
            try {
                if (session.getTransaction().getStatus() == TransactionStatus.ACTIVE
                        || session.getTransaction().getStatus() == TransactionStatus.MARKED_ROLLBACK)
                    session.getTransaction().rollback();
            } catch (Exception rbEx) {
                LOG.error("Rollback of transaction failed, trace follows!");
                LOG.error(rbEx, rbEx);
            }
            throw new RuntimeException(e);
        } finally {
            if(session != null){
                session.close();
            }
        }
    }
}
