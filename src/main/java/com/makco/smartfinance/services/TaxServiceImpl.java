package com.makco.smartfinance.services;

import com.makco.smartfinance.persistence.dao.TaxDAO;
import com.makco.smartfinance.persistence.dao.TaxDAOImpl;
import com.makco.smartfinance.persistence.entity.Tax;
import com.makco.smartfinance.user_interface.validation.ErrorEnum;
import com.makco.smartfinance.user_interface.validation.RuleSet;
import com.makco.smartfinance.user_interface.validation.rule_sets.TaxRuleSet;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

/**
 * Created by Makar Kalancha on 2016-04-22.
 */
public class TaxServiceImpl implements TaxService {
    private final static Logger LOG = LogManager.getLogger(TaxServiceImpl.class);
    private TaxDAO taxDAO = new TaxDAOImpl();

    @Override
    public Tax getTaxById(Long id) throws Exception {
        Tax tax = null;
        try {
            tax = taxDAO.getTaxById(id);
        } catch (Exception e) {
            throw e;
        }
        return tax;
    }

    @Override
    public List<Tax> taxList() throws Exception {
        List<Tax> taxes = new ArrayList<>();
        try {
            taxes = taxDAO.taxList();
        } catch (Exception e) {
            throw e;
        }
        return taxes;
    }

    @Override
    public List<Tax> taxListWithChildren() throws Exception {
        List<Tax> taxes = new ArrayList<>();
        try {
            taxes = taxDAO.taxListWithAssociations();
        } catch (Exception e) {
            throw e;
        }
        return taxes;
    }

    @Override
    public List<Tax> getTaxByName(String name) throws Exception {
        List<Tax> taxes = new ArrayList<>();
        try {
            taxes = taxDAO.getTaxByName(name);
        } catch (Exception e) {
            throw e;
        }
        return taxes;
    }

    @Override
    public void removeTax(Long id) throws Exception {
        try{
            taxDAO.removeTax(id);
        } catch (Exception e) {
            throw e;
        }
    }

    @Override
    public void saveOrUpdateTax(Tax tax) throws Exception {
        try{
            taxDAO.saveOrUpdateTax(tax);
        } catch (Exception e) {
            throw e;
        }
    }

    @Override
    public EnumSet<ErrorEnum> validate(Tax tax) throws Exception {
        EnumSet<ErrorEnum> errors = EnumSet.noneOf(ErrorEnum.class);
        try {
            RuleSet ruleSet = new TaxRuleSet();
            errors = ruleSet.validate(tax);
        } catch (Exception e) {
            throw e;
        }
        return errors;
    }
}
