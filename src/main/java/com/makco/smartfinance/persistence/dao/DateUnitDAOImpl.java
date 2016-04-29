package com.makco.smartfinance.persistence.dao;

import com.makco.smartfinance.constants.DataBaseConstants;
import com.makco.smartfinance.persistence.entity.DateUnit;
import com.makco.smartfinance.persistence.utils.HibernateUtil;
import java.util.ArrayList;
import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.resource.transaction.spi.TransactionStatus;

/**
 * Created by mcalancea on 2016-04-19.
 */
//http://programmers.stackexchange.com/questions/220909/service-layer-vs-dao-why-both
public class DateUnitDAOImpl implements DateUnitDAO {
    private final static Logger LOG = LogManager.getLogger(DateUnitDAOImpl.class);

    @Override
    public void addDateUnit(DateUnit dateUnit) throws Exception {
        Session session = null;
        try {
            session = HibernateUtil.openSession();
            session.beginTransaction();
            session.save(dateUnit);
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
    public void addDateUnitList(List<DateUnit> dateUnits) throws Exception {
        //https://docs.jboss.org/hibernate/orm/3.3/reference/en/html/batch.html
        Session session = null;
        try {
            session = HibernateUtil.openSession();
            session.beginTransaction();

            for (int i = 0; i < dateUnits.size(); i++) {
                session.save(dateUnits.get(i));
                if(i % DataBaseConstants.BATCH_SIZE == 0){
                    session.flush();
                    session.clear();
                    Thread.sleep(50);
                }
            }

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
    public List<DateUnit> dateUnitList() throws Exception {
        List<DateUnit> list = new ArrayList<>();
        Session session = null;
        try{
            session = HibernateUtil.openSession();
            session.beginTransaction();
            list = session.createQuery("SELECT du FROM DateUnit du ORDER BY du.unitDate").list();
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
    public DateUnit getDateUnitByUnitDate(Long unitDate) throws Exception {
        Session session = null;
        DateUnit dateUnit = null;
        try{
            session = HibernateUtil.openSession();
            session.beginTransaction();
            dateUnit = session.get(DateUnit.class, unitDate);
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
        return dateUnit;
    }

    @Override
    public boolean isEmpty() throws Exception {
        Session session = null;
        boolean isEmpty = false;
        try{
            session = HibernateUtil.openSession();
            session.beginTransaction();
            isEmpty = session.createQuery("SELECT 1 FROM DateUnit").setMaxResults(1).list().isEmpty();
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
        return isEmpty;
    }
}
