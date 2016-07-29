package com.makco.smartfinance.persistence.dao;

import com.makco.smartfinance.persistence.entity.DateUnit;

import java.util.List;

/**
 * Created by Makar Kalancha on 2016-04-19.
 */
//https://howtoprogramwithjava.com/hibernate-creating-data-access-objects-daos/
public interface DateUnitDAO {
    List<DateUnit> dateUnitList() throws Exception;
    DateUnit getDateUnitByUnitDate(Long unitDate) throws Exception;

    void addDateUnit(DateUnit dateUnit) throws Exception;
    void addDateUnitList(List<DateUnit> dateUnit) throws Exception;

    boolean isEmpty() throws Exception;
}
