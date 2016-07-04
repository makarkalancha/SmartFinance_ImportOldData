package com.makco.smartfinance.persistence.entity;

import com.makco.smartfinance.constants.DataBaseConstants;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.HashSet;

import static org.junit.Assert.*;

/**
 * Created by mcalancea on 29 Jun 2016.
 */
public class TaxTest {

    @Test
    public void testCalculateFormula_1() throws Exception {
        Tax tax = new Tax(
                "TAX1",
                "tax number 1",
                new BigDecimal("0.05"),
                new StringBuilder(DataBaseConstants.TAX_NUMBER_PLACEHOLDER)
                    .append("*(1+")
                    .append(DataBaseConstants.TAX_RATE_PLACEHOLDER)
                    .append(")")
                    .toString(),
                null,
                null,
                new HashSet<>()
        );
        System.out.println(">>>Formula:" + tax.getFormula());
        BigDecimal result = tax.calculateFormula(new BigDecimal("100"));
        assertEquals(new BigDecimal("105.00"), result);
    }
}