package com.makco.smartfinance.persistence.utils;

import org.junit.Test;

import java.time.LocalDate;
import java.time.Month;

import static org.junit.Assert.*;

/**
 * User: Makar Kalancha
 * Date: 20/04/2016
 * Time: 22:05
 */
public class DateUnitUtilTest {
    private LocalDate initialDate = LocalDate.of(2000, Month.JANUARY, 1);
    @Test
    public void testGetLocaDateInFutureSinceNow() throws Exception {
        LocalDate resultDate = DateUnitUtil.getLocaDateInFutureSinceDate(initialDate);
        assertEquals(LocalDate.of(2005, Month.JANUARY, 1), resultDate);
    }

    @Test
    public void testGetNumberOfBatchesInRange_2() throws Exception {
        LocalDate startDate = initialDate;
        LocalDate endDate = LocalDate.of(2000, Month.APRIL, 9);
        assertEquals(2, DateUnitUtil.getNumberOfBatchesInRange(startDate, endDate));
    }

    @Test
    public void testGetNumberOfBatchesInRange_47() throws Exception {
        //with batch size = 50, qty of days = 2301, 2301 / 50 = 46.02 ~ 47 batches
        LocalDate startDate = LocalDate.of(2010, Month.JANUARY, 01);
        LocalDate endDate = LocalDate.of(2016, Month.APRIL, 20);
        assertEquals(47, DateUnitUtil.getNumberOfBatchesInRange(startDate, endDate));
    }
}