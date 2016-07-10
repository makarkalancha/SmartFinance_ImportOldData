package com.makco.smartfinance.persistence.dao;

import com.makco.smartfinance.persistence.entity.Tax;

import java.util.List;

/**
 * Created by mcalancea on 2016-06-08.
 */
public interface TaxDAO {
    List<Tax> taxList() throws Exception;
    List<Tax> taxListWithAssociations() throws Exception;
    Tax getTaxById(Long id) throws Exception;
    List<Tax> getTaxByName(String name) throws Exception;

    void removeTax(Long id) throws Exception;
    void saveOrUpdateTax(Tax tax) throws Exception;
}
