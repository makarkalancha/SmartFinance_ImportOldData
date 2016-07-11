package com.makco.smartfinance.services;

import com.makco.smartfinance.persistence.entity.DateUnit;

import java.util.List;

/**
 * Created by Makar Kalancha on 2016-04-21.
 */
public interface DateUnitService {
    List<DateUnit> dateUnitList() throws Exception;
    DateUnit getDateUnitByUnitDate(Long unitDate) throws Exception;

    void addDateUnit(DateUnit dateUnit) throws Exception;
    void addDateUnitList(List<DateUnit> dateUnitList) throws Exception;

    boolean isEmpty() throws Exception;
}
