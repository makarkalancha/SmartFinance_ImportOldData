package com.makco.smartfinance.persistence.dao;

import com.makco.smartfinance.persistence.contants.DataBaseConstants;
import com.makco.smartfinance.persistence.entity.DateUnit;
import com.makco.smartfinance.persistence.utils.HibernateUtil;
import com.makco.smartfinance.user_interface.constants.DialogMessages;
import java.util.ArrayList;
import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;

/**
 * Created by mcalancea on 2016-04-19.
 */
//http://programmers.stackexchange.com/questions/220909/service-layer-vs-dao-why-both
public class DateUnitDAOImpl implements DateUnitDAO {
    private final static Logger LOG = LogManager.getLogger(DateUnitDAOImpl.class);

    @Override
    public void addDateUnit(DateUnit dateUnit) {
        Session session = null;
        try {
            session = HibernateUtil.openSession();
            session.beginTransaction();
            session.save(dateUnit);
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
    public void addDateUnitList(List<DateUnit> dateUnits) {
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
                }
            }

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
    public List<DateUnit> dateUnitList() {
        List<DateUnit> list = new ArrayList<>();
        Session session = null;
        try{
            session = HibernateUtil.openSession();
            session.beginTransaction();
            list = session.createQuery("SELECT du FROM DateUnit du ORDER BY du.unitDate").list();
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
    public DateUnit getDateUnitByUnitDate(Long unitDate) {
        Session session = null;
        DateUnit dateUnit = null;
        try{
            session = HibernateUtil.openSession();
            session.beginTransaction();
            dateUnit = session.get(DateUnit.class, unitDate);
            session.getTransaction().commit();
        } catch (Exception e) {
            session.getTransaction().rollback();
            DialogMessages.showExceptionAlert(e);
        } finally {
            if(session != null){
                session.close();
            }
        }
        return dateUnit;
    }
}
