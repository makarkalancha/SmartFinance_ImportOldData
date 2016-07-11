package com.makco.smartfinance.persistence.dao;

import com.makco.smartfinance.persistence.entity.Account;
import com.makco.smartfinance.persistence.utils.HibernateUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.resource.transaction.spi.TransactionStatus;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Makar Kalancha on 2016-06-06.
 */
public class AccountDAOImpl implements AccountDAO {
    private final static Logger LOG = LogManager.getLogger(AccountDAOImpl.class);

    @Override
    public <T extends Account> List<T> accountByType(Class<T> type) throws Exception {
        Session session = null;
        List<T> list = new ArrayList<>();

        try{
            session = HibernateUtil.openSession();
            session.beginTransaction();
            LOG.debug("type.newInstance().getAccountGroupType():" + type.newInstance().getAccountGroupType());
            list = session.createQuery("SELECT a FROM Account a WHERE a.class = :type ORDER BY a.name")
                    .setParameter("type", type.newInstance().getAccountGroupType())
                    .list();
            Collections.sort(list, (Account c1, Account c2) -> c1.getName().toLowerCase().compareTo(c2.getName().toLowerCase()));
            session.getTransaction().commit();

        } catch (Exception e) {
            //hibernate persistence p.257
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
    public void saveOrUpdateAccount(Account account) throws Exception {
        Session session = null;
        try {
            LOG.debug(">>>saveOrUpdateAccount: start");
            session = HibernateUtil.openSession();
            session.beginTransaction();
            session.saveOrUpdate(account);
            session.getTransaction().commit();
            LOG.debug(">>>saveOrUpdateAccount: end");
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
    public Account getAccountById(Long id) throws Exception {
        Session session = null;
        Account account = null;
        try{
            session = HibernateUtil.openSession();
            session.beginTransaction();
            account = (Account) session.get(Account.class, id);
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
        return account;
    }

    @Override
    public void removeAccount(Long id) throws Exception {
        Session session = null;
        try{
            session = HibernateUtil.openSession();
            session.beginTransaction();
            Account account = (Account) session.load(Account.class, id);
            session.delete(account);
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
    public List<Account> getAccountByName(String accountName) throws Exception {
        Session session = null;
        List<Account> list = new ArrayList<>();
        try{
            session = HibernateUtil.openSession();
            session.beginTransaction();
            list = session.createQuery("SELECT a FROM Account a where a.name = :accountName")
                    .setString("accountName", accountName)
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
        return list;
    }
}
