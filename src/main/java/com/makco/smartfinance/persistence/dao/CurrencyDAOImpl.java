package com.makco.smartfinance.persistence.dao;

import com.makco.smartfinance.persistence.entity.Currency;
import com.makco.smartfinance.persistence.utils.HibernateUtil;
import java.util.ArrayList;
import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.resource.transaction.spi.TransactionStatus;

/**
 * Created by mcalancea on 2016-04-12.
 */
//http://programmers.stackexchange.com/questions/220909/service-layer-vs-dao-why-both
public class CurrencyDAOImpl implements CurrencyDAO {
    private final static Logger LOG = LogManager.getLogger(CurrencyDAOImpl.class);

    @Override
    public List<Currency> currencyList() throws Exception {
        List<Currency> list = new ArrayList<>();
        Session session = null;
        try{
            session = HibernateUtil.openSession();
            session.beginTransaction();
            list = session.createQuery("SELECT c FROM Currency c ORDER BY c.code").list();
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
        return list;
    }

    @Override
    public Currency getCurrencyById(Long id) throws Exception {
        Session session = null;
        Currency currency = null;
        try{
            session = HibernateUtil.openSession();
            session.beginTransaction();
            currency = session.get(Currency.class, id);
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
        return currency;
    }

    @Override
    public List<Currency> getCurrencyByCode(String code) throws Exception {
        Session session = null;
        List<Currency> currencies = new ArrayList<>();
        try {
            session = HibernateUtil.openSession();
            session.beginTransaction();
            currencies = session.createQuery("SELECT c FROM Currency c WHERE code = :code ORDER BY c.code")
                    .setString("code", code)
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
        return currencies;
    }

    @Override
    public void removeCurrency(Long id) throws Exception {
        Session session = null;
        try{
            session = HibernateUtil.openSession();
            session.beginTransaction();
            Currency currency = session.load(Currency.class, id);
            session.delete(currency);
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
    public void saveOrUpdateCurrency(Currency currency) throws Exception {
        Session session = null;
        try {
            session = HibernateUtil.openSession();
            session.beginTransaction();
            session.saveOrUpdate(currency);
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
