package com.makco.smartfinance.persistence.utils;

import com.makco.smartfinance.persistence.entity.DateUnit;
import java.time.LocalDate;
import java.time.Month;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * User: Makar Kalancha
 * Date: 20/04/2016
 * Time: 22:05
 */
public class DateUnitUtilTest {
    private LocalDate Jan_1_2000 = LocalDate.of(2000, Month.JANUARY, 1);
    private LocalDate Jan_1_2005 = LocalDate.of(2005, Month.JANUARY, 1);
    private LocalDate Feb_1_2005 = LocalDate.of(2005, Month.FEBRUARY, 1);

    @Test
    public void testGetLocaDateInFutureSinceNow() throws Exception {
        LocalDate resultDate = DateUnitUtil.getLocaDateInFutureSinceDate(Jan_1_2000);
        assertEquals(Jan_1_2005, resultDate);
    }

    @Test
    public void testGetNumberOfBatchesInRange_2() throws Exception {
        LocalDate Apr_9_2000 = LocalDate.of(2000, Month.APRIL, 9);
        assertEquals(2, DateUnitUtil.getNumberOfBatchesInRange(Jan_1_2000, Apr_9_2000));
    }

    @Test
    public void testGetNumberOfBatchesInRange_47() throws Exception {
        //with batch size = 50, qty of days = 2301, 2301 / 50 = 46.02 ~ 47 batches
        LocalDate startDate = LocalDate.of(2010, Month.JANUARY, 01);
        LocalDate endDate = LocalDate.of(2016, Month.APRIL, 20);
        assertEquals(47, DateUnitUtil.getNumberOfBatchesInRange(startDate, endDate));
    }

    @Test
    public void getListOfDateUnitEntitiesManually() throws Exception{
        LocalDate start = Jan_1_2005;
        LocalDate end = Feb_1_2005;

        List<DateUnit> result = new ArrayList<>();
        if(start.compareTo(end) < 0) {
            LocalDate tmp = start;
            while (tmp.compareTo(end) <= 0) {
                DateUnit du = new DateUnit(tmp);
//                System.out.println(du);
                result.add(du);
                tmp = tmp.plus(1, ChronoUnit.DAYS);
            }
        }
        //Jan_1_2005 the same as LocalDate is immutable
        System.out.println("Jan_1_2005 after loop: " + start);
        System.out.println("result: " + result);
        assertEquals(32, result.size());
    }

    @Test
    public void getListOfDateUnitEntities_startGreaterThanEnd() throws Exception{
        List<DateUnit> result = DateUnitUtil.getListOfDateUnitEntities(Feb_1_2005, Jan_1_2005);
        assertEquals(0, result.size());
    }

    @Test
    public void getListOfDateUnitEntities_startEqualsEnd() throws Exception{
        List<DateUnit> result = DateUnitUtil.getListOfDateUnitEntities(Jan_1_2005, Jan_1_2005);
        assertEquals(0, result.size());
    }
}