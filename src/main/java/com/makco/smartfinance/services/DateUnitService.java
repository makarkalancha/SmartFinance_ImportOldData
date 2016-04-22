package com.makco.smartfinance.services;

import com.makco.smartfinance.persistence.entity.Currency;
import com.makco.smartfinance.persistence.entity.DateUnit;
import com.makco.smartfinance.user_interface.validation.ErrorEnum;

import java.util.EnumSet;
import java.util.List;

/**
 * Created by mcalancea on 2016-04-21.
 */
public interface DateUnitService {
    List<DateUnit> dateUnitList();
    DateUnit getDateUnitByUnitDate(Long unitDate);

    void addDateUnit(DateUnit dateUnit);
    void addDateUnitList(List<DateUnit> dateUnitList);

    boolean isEmpty();
}
