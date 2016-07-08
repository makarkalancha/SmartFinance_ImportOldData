package com.makco.smartfinance.persistence.entity;

import com.makco.smartfinance.constants.DataBaseConstants;
import com.makco.smartfinance.persistence.entity.session.Tax_v1;
import com.makco.smartfinance.utils.BigDecimalCalculation;
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
        BigDecimal result = BigDecimalCalculation.calculateFormulaRPN(tax.getDenormalizedFormula(), new BigDecimal("100"));
        assertEquals(new BigDecimal("105.00"), result);
    }

    @Test
    public void testDenormalize_1() throws Exception {
        Tax_v1 five = new Tax_v1(
                "TAX5",
                "tax number 5",
                new BigDecimal("5"),
                new StringBuilder(DataBaseConstants.TAX_NUMBER_PLACEHOLDER)
                        .append("*(1+")
                        .append(DataBaseConstants.TAX_RATE_PLACEHOLDER)
                        .append("/100)")
                        .toString(),
                null,
                null,
                null,
                new HashSet<>()
        );
        five.setId(5L);

        Set<Tax_v1> taxSet = new HashSet<>();
        taxSet.add(five);
        Tax_v1 nine = new Tax_v1(
                "TAX9",
                "tax number 9",
                new BigDecimal("9"),
                new StringBuilder(DataBaseConstants.TAX_NUMBER_PLACEHOLDER)
                        .append("*(1+")
                        .append(DataBaseConstants.getTaxChildIdPlaceholder(five.getId()))
                        .append("/100)")
                        .append("*(1+")
                        .append(DataBaseConstants.TAX_RATE_PLACEHOLDER)
                        .append("/100)")
                        .toString(),
                null,
                null,
                null,
                taxSet
        );
        nine.setId(9L);
        String denormalizedFormula = nine.getDenormalizedFormula();
        assertEquals("{NUM}*(1+5/100)*(1+9/100)", denormalizedFormula);

    }
}