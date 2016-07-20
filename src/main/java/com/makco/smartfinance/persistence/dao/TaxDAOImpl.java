package com.makco.smartfinance.persistence.dao;

import com.makco.smartfinance.persistence.entity.Tax;
import com.makco.smartfinance.persistence.utils.HibernateUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.resource.transaction.spi.TransactionStatus;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by Makar Kalancha on 2016-06-05.
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
    public List<Tax> taxListWithAssociations() throws Exception {
        Session session = null;
        List<Tax> list = new ArrayList<>();
        try {
            session = HibernateUtil.openSession();
            session.beginTransaction();
            ////http://stackoverflow.com/questions/12425835/jpql-manytomany-select
            /*
            SmartFinance\src\test\java\com\makco\smartfinance\persistence\dao\dao_implementations\TaxDAOImpl_v1.java
            taxListWithChildrenAndParents_leftJoinFetch
            see test_42_select_benchMark.ods
            cartesian product
             */
            list = session.createQuery("SELECT t FROM Tax t left join fetch t.childTaxes left join fetch t.parentTaxes ORDER BY t.name").list();
            session.getTransaction().commit();

            //clean cartesian product: LinkedHashSet to save the order
            Set<Tax> set = new LinkedHashSet<>(list);
            list = new ArrayList<>(set);
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
        /*
        see details in
        SmartFinance\src\test\java\com\makco\smartfinance\persistence\dao\dao_implementations\TaxDAOImpl_v1.java
         */
        Session session = null;
        try{
            session = HibernateUtil.openSession();
            session.beginTransaction();

            Tax tax = session.get(Tax.class, id);
            tax.setChildTaxes(new HashSet<>());
            tax.setParentTaxes(new HashSet<>());
            session.saveOrUpdate(tax);

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
//            tax.refreshDenormalizedFormula(true);
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
