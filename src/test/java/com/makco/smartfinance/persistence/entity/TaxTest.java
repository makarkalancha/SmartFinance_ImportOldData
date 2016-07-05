package com.makco.smartfinance.persistence.entity;

import com.makco.smartfinance.constants.DataBaseConstants;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

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
                null,
                new HashSet<>()
        );
        System.out.println(">>>Formula:" + tax.getFormula());
        BigDecimal result = tax.calculateFormula(new BigDecimal("100"));
        assertEquals(new BigDecimal("105.00"), result);
    }

    @Test
    public void testDenormalize_1() throws Exception {
        String denormalizedFormulaString = "{NUM}*(1+{TAX1}/100)*(1+{RATE}/100)";

        Tax five = new Tax(
                "TAX5",
                "tax number 5",
                new BigDecimal("5"),
                new StringBuilder(DataBaseConstants.TAX_NUMBER_PLACEHOLDER)
                        .append("*(1+")
                        .append(DataBaseConstants.TAX_RATE_PLACEHOLDER)
                        .append("/100)")
                        .toString(),
                "{NUM}*(1+{RATE}/100)",
                null,
                null,
                new HashSet<>()
        );
        five.seti

        Set<Tax> taxSet = new HashSet<>();
        taxSet.add(five);
        Tax nine = new Tax(
                "TAX9",
                "tax number 9",
                new BigDecimal("9"),
                new StringBuilder(DataBaseConstants.TAX_NUMBER_PLACEHOLDER)
                        .append("*(1+")
                        .append(DataBaseConstants.getTaxChildIdPlaceholder(1))
                        .append("/100)")
                        .append("*(1+")
                        .append(DataBaseConstants.TAX_RATE_PLACEHOLDER)
                        .append("/100)")
                        .toString(),
                denormalizedFormulaString,
                null,
                null,
                taxSet
        );
        nine.denormalizeFormula();
        String denormalizedFormula = nine.getDenormalizedFormula();
        assertEquals("{NUM}*(1+{5}/100)*(1+{9}/100)", denormalizedFormula);

    }
}