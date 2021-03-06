package com.makco.smartfinance.user_interface.models;

import com.makco.smartfinance.persistence.entity.Tax;
import com.makco.smartfinance.services.TaxService;
import com.makco.smartfinance.services.TaxServiceImpl;
import com.makco.smartfinance.user_interface.validation.ErrorEnum;
import com.makco.smartfinance.utils.BigDecimalUtils;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collection;
import java.util.EnumSet;

/**
 * Created by Makar Kalancha on 2016-04-05.
 */
public class TaxModel {
    private final static Logger LOG = LogManager.getLogger(TaxModel.class);
    private TaxService taxService = new TaxServiceImpl();
    private ObservableList<Tax> taxes = FXCollections.observableArrayList();
    private Tax pendingTax;

    public TaxModel(){

    }

    public void refreshTax() throws Exception {
        try{
            if (!taxes.isEmpty()) {
                taxes.clear();
            }
            taxes = FXCollections.observableArrayList(taxService.taxListWithChildren());
            LOG.debug("taxes.size: " + taxes.size());
        }catch (Exception e){
            throw e;
        }
    }

    public ObservableList<Tax> getTaxes() throws Exception {
        return taxes;
    }


    public EnumSet<ErrorEnum> savePendingTax(String name, String description, String rateStr, String formula, String denormalizedFormula,
         LocalDate startDate, LocalDate endDate, Collection<Tax> childTaxes) throws Exception {

        EnumSet<ErrorEnum> errors = EnumSet.noneOf(ErrorEnum.class);
        BigDecimal rate = null;
        try {
            rate = BigDecimalUtils.convertStringToBigDecimal(rateStr);
        } catch (Exception e) {
            errors.add(ErrorEnum.TAX_DESC_LGTH);
        }
        try {
            Tax tmpTax;
            if (pendingTax != null) {
                pendingTax.setName(name);
                pendingTax.setDescription(description);
                pendingTax.setRate(rate);
                pendingTax.setFormula(formula);
//                pendingTax.setDenormalizedFormula(denormalizedFormula);
                pendingTax.setStartDate(startDate);
                pendingTax.setEndDate(endDate);
                pendingTax.setChildTaxes(childTaxes);
                tmpTax = pendingTax;
            } else {
                tmpTax = new Tax(name, description, rate, formula, denormalizedFormula, startDate, endDate, childTaxes);
            }

            errors = taxService.validate(tmpTax);
            if (errors.isEmpty()) {
                taxService.saveOrUpdateTax(tmpTax);
                pendingTax = null;
            }
        } catch (Exception e) {
            throw e;
        } finally {
            refreshTax();
        }
        return errors;
    }

    public void deletePendingTax() throws Exception {
        try{
            if (pendingTax != null && pendingTax.getId() != null) {
                taxService.removeTax(pendingTax.getId());
                pendingTax = null;
            }
        } catch (Exception e) {
            throw e;
        }finally {
            refreshTax();
        }
    }

    public Tax getPendingTax() throws Exception {
        return pendingTax;
    }

    public void setPendingTaxProperty(Tax tax) throws Exception {
        pendingTax = tax;
    }
}
