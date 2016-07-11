package com.makco.smartfinance.services;

import com.makco.smartfinance.persistence.dao.DateUnitDAO;
import com.makco.smartfinance.persistence.dao.DateUnitDAOImpl;
import com.makco.smartfinance.persistence.entity.DateUnit;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Makar Kalancha on 2016-04-21.
 */
public class DateUnitServiceImpl implements DateUnitService{
    private final static Logger LOG = LogManager.getLogger(DateUnitServiceImpl.class);
    private DateUnitDAO dateUnitDAO = new DateUnitDAOImpl();
    
    @Override
    public List<DateUnit> dateUnitList() throws Exception {
        List<DateUnit> dateUnitList = new ArrayList<>();
        try {
            dateUnitList = dateUnitDAO.dateUnitList();
        } catch (Exception e) {
            throw e;
        }
        return dateUnitList;
    }

    @Override
    public DateUnit getDateUnitByUnitDate(Long unitDate) throws Exception {
        DateUnit dateUnit = null;
        try {
            dateUnit = dateUnitDAO.getDateUnitByUnitDate(unitDate);
        } catch (Exception e) {
            throw e;
        }
        return dateUnit;
    }

    @Override
    public void addDateUnit(DateUnit dateUnit) throws Exception {
        try{
            dateUnitDAO.addDateUnit(dateUnit);
        } catch (Exception e) {
            throw e;
        }
    }

    @Override
    public void addDateUnitList(List<DateUnit> dateUnitList) throws Exception {
        try{
            dateUnitDAO.addDateUnitList(dateUnitList);
        } catch (Exception e) {
            throw e;
        }
    }

    @Override
    public boolean isEmpty() throws Exception {
        boolean result = true;
        try{
            result = dateUnitDAO.isEmpty();
        } catch (Exception e) {
            throw e;
        }
        return result;
    }
}
