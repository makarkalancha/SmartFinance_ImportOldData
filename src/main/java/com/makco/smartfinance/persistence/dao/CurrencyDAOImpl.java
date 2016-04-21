package com.makco.smartfinance.persistence.dao;

import com.makco.smartfinance.persistence.entity.Currency;
import com.makco.smartfinance.persistence.utils.HibernateUtil;
import com.makco.smartfinance.user_interface.constants.DialogMessages;
import java.util.ArrayList;
import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;

/**
 * Created by mcalancea on 2016-04-12.
 */
//http://programmers.stackexchange.com/questions/220909/service-layer-vs-dao-why-both
public class CurrencyDAOImpl implements CurrencyDAO {
    private final static Logger LOG = LogManager.getLogger(CurrencyDAOImpl.class);

    @Override
    public synchronized List<Currency> currencyList() {
        List<Currency> list = new ArrayList<>();
        Session session = null;
        try{
            session = HibernateUtil.openSession();
            session.beginTransaction();
            list = session.createQuery("SELECT c FROM Currency c ORDER BY c.code").list();
            session.getTransaction().commit();
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
    public synchronized Currency getCurrencyById(Long id) {
        Session session = null;
        Currency currency = null;
        try{
            session = HibernateUtil.openSession();
            session.beginTransaction();
            currency = session.get(Currency.class, id);
            session.getTransaction().commit();
        } catch (Exception e) {
            session.getTransaction().rollback();
            DialogMessages.showExceptionAlert(e);
        } finally {
            if(session != null){
                session.close();
            }
        }
        return currency;
    }

    @Override
    public synchronized List<Currency> getCurrencyByCode(String code) {
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
            session.getTransaction().rollback();
            DialogMessages.showExceptionAlert(e);
        } finally {
            if(session != null){
                session.close();
            }
        }
        return currencies;
    }

    @Override
    public synchronized void removeCurrency(Long id) {
        Session session = null;
        try{
            session = HibernateUtil.openSession();
            session.beginTransaction();
            Currency currency = session.load(Currency.class, id);
            session.delete(currency);
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

    @Override
    public synchronized void saveOrUpdateCurrency(Currency currency) {
        Session session = null;
        try {
            session = HibernateUtil.openSession();
            session.beginTransaction();
            session.saveOrUpdate(currency);
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
