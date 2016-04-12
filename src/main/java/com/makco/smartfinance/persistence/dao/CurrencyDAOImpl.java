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
    public void addCurrency(Currency currency) {
        try {
            Session session = HibernateUtil.openSession();
            session.beginTransaction();
            session.save(currency);
            session.getTransaction().commit();
            session.close();
        } catch (Exception e) {
            DialogMessages.showExceptionAlert(e);
        }
    }

    @Override
    public List<Currency> currencyList() {
        List<Currency> list = new ArrayList<>();
        try{
            Session session = HibernateUtil.openSession();
            session.beginTransaction();
            list = session.createQuery("FROM Currency AS c ORDER BY c.code").list();
            session.getTransaction().commit();
            session.close();
        } catch (Exception e) {
            DialogMessages.showExceptionAlert(e);
        }
        return list;
    }

    @Override
    public Currency getCurrencyById(Long id) {
        Currency currency = null;
        try{
            Session session = HibernateUtil.openSession();
            session.beginTransaction();
            currency = session.get(Currency.class, id);
            session.getTransaction().commit();
            session.close();
        } catch (Exception e) {
            DialogMessages.showExceptionAlert(e);
        }
        return currency;
    }

    @Override
    public List<Currency> getCurrencyByCode(String code) {
        List<Currency> currencies = new ArrayList<>();
        try {
            Session session = HibernateUtil.openSession();
            session.beginTransaction();
            currencies = session.createQuery("FROM Currency AS c WHERE code = :code ORDER BY c.code")
                    .setString("code", code)
                    .list();
            session.getTransaction().commit();
            session.close();
        } catch (Exception e) {
            DialogMessages.showExceptionAlert(e);
        }
        return currencies;
    }

    @Override
    public void removeCurrency(Long id) {
        try{
            Session session = HibernateUtil.openSession();
            session.beginTransaction();
            Currency currency = session.load(Currency.class, id);
            session.delete(currency);
            session.getTransaction().commit();
            session.close();
        } catch (Exception e) {
            DialogMessages.showExceptionAlert(e);
        }
    }

    @Override
    public void saveOrUpdateCurrency(Currency currency) {
        try {
            Session session = HibernateUtil.openSession();
            session.beginTransaction();
            session.saveOrUpdate(currency);
            session.getTransaction().commit();
            session.close();
        } catch (Exception e) {
            DialogMessages.showExceptionAlert(e);
        }
    }

    @Override
    public void updateCurrency(Currency currency) {
        try{
            Session session = HibernateUtil.openSession();
            session.beginTransaction();
            session.update(currency);
            session.getTransaction().commit();
            session.close();
        } catch (Exception e) {
            DialogMessages.showExceptionAlert(e);
        }
    }
}
