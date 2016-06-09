package com.makco.smartfinance.utils;

import org.apache.commons.lang3.text.StrBuilder;
import org.junit.Test;

import java.math.BigDecimal;

import static org.junit.Assert.*;

/**
 * Created by mcalancea on 2016-06-09.
 */
public class BigDecimalUtilsTest {

    @Test
    public void testConvertStringToBigDecimal() throws Exception {
        String decimalString = new StringBuilder()
            .append("2")
            .append(BigDecimalUtils.getGroupingSeparator())
            .append("001")
            .append(BigDecimalUtils.getDecimalSeparator())
            .append("01")
            .toString()
            ;
        BigDecimal actualValue = BigDecimalUtils.convertStringToBigDecimal(decimalString);
        BigDecimal expectedValue = new BigDecimal("2001.01");
        expectedValue.setScale(3, BigDecimal.ROUND_HALF_UP);
        assertEquals(expectedValue,actualValue);
    }
}