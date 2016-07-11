package com.makco.smartfinance.services;

import com.makco.smartfinance.persistence.entity.Tax;
import com.makco.smartfinance.user_interface.validation.ErrorEnum;

import java.util.EnumSet;
import java.util.List;

/**
 * Created by Makar Kalancha on 2016-04-22.
 */
public interface TaxService {
    List<Tax> taxList() throws Exception;
    List<Tax> taxListWithChildren() throws Exception;
    Tax getTaxById(Long id) throws Exception;
    List<Tax> getTaxByName(String name) throws Exception;

    void removeTax(Long id) throws Exception;
    void saveOrUpdateTax(Tax tax) throws Exception;
    EnumSet<ErrorEnum> validate(Tax tax) throws Exception;
}
