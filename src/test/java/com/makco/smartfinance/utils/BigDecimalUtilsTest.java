package com.makco.smartfinance.utils;

import org.junit.Test;

import java.math.BigDecimal;

import static org.junit.Assert.*;

/**
 * Created by mcalancea on 2016-06-09.
 */
public class BigDecimalUtilsTest {

    @Test
    public void testConvertStringToBigDecimal() throws Exception {
        String decimalString = "2 001,01";
//        String decimalString = "2,001.01";
        BigDecimal actualValue = BigDecimalUtils.convertStringToBigDecimal(decimalString);
        BigDecimal expectedValue = new BigDecimal("2001.01");
        expectedValue.setScale(3, BigDecimal.ROUND_HALF_UP);
        assertEquals(expectedValue,actualValue);
    }
}