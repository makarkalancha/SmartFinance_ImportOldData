package com.makco.smartfinance.persistence.dao;

import com.makco.smartfinance.persistence.entity.Tax;
import com.makco.smartfinance.persistence.utils.HibernateUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.resource.transaction.spi.TransactionStatus;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mcalancea on 2016-06-05.
 */
//http://programmers.stackexchange.com/questions/220909/service-layer-vs-dao-why-both
public class TaxDAOImpl implements TaxDAO {
    private final static Logger LOG = LogManager.getLogger(TaxDAOImpl.class);

    @Override
    public Tax getTaxById(Long id) throws Exception {
        Session session = null;
        Tax tax = null;
        try{
            session = HibernateUtil.openSession();
            session.beginTransaction();
            tax = session.get(Tax.class, id);
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
        return tax;
    }

    @Override
    public List<Tax> taxList() throws Exception {
        Session session = null;
        List<Tax> list = new ArrayList<>();
        try {
            session = HibernateUtil.openSession();
            session.beginTransaction();
            list = session.createQuery("SELECT t FROM Tax t ORDER BY t.name").list();
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
    public List<Tax> taxListWithChildren() throws Exception {
        Session session = null;
        List<Tax> list = new ArrayList<>();
        try {
            session = HibernateUtil.openSession();
            session.beginTransaction();
            ////http://stackoverflow.com/questions/12425835/jpql-manytomany-select
            list = session.createQuery("SELECT t FROM Tax t left join fetch t.childTaxes ORDER BY t.name").list();
//            //https://docs.jboss.org/hibernate/orm/3.3/reference/en/html/queryhql.html
//            list = session.createQuery("SELECT t FROM Tax t all properties t.childTaxes ORDER BY t.name").list();
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
    public List<Tax> getTaxByName(String name) throws Exception {
        Session session = null;
        List<Tax> taxs = new ArrayList<>();
        try {
            session = HibernateUtil.openSession();
            session.beginTransaction();
            taxs = session.createQuery("SELECT t FROM Tax t WHERE name = :name ORDER BY t.name")
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
        return taxs;
    }

    @Override
    public void removeTax(Long id) throws Exception {
        Session session = null;
        try{
            session = HibernateUtil.openSession();
            session.beginTransaction();
            Tax tax = session.load(Tax.class, id);
            session.delete(tax);
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
    public void saveOrUpdateTax(Tax tax) throws Exception {
        Session session = null;
        try {
//            tax.refreshDenormalizeFormula(true);
            session = HibernateUtil.openSession();
            session.beginTransaction();
            session.saveOrUpdate(tax);
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
