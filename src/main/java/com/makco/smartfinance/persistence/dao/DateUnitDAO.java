package com.makco.smartfinance.persistence.dao;

import com.makco.smartfinance.persistence.entity.DateUnit;
import java.util.List;

/**
 * Created by mcalancea on 2016-04-19.
 */
//https://howtoprogramwithjava.com/hibernate-creating-data-access-objects-daos/
public interface DateUnitDAO {
    List<DateUnit> dateUnitList();
    DateUnit getDateUnitByUnitDate(Long unitDate);

    void addDateUnit(DateUnit dateUnit);
    void addDateUnitList(List<DateUnit> dateUnit);
}
