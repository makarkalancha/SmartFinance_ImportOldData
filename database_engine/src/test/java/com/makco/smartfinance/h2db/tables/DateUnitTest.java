package com.makco.smartfinance.h2db.tables;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.makco.smartfinance.h2db.tables.DateUnit;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Created by mcalancea on 2016-02-26.
 */
public class DateUnitTest {
    private DateUnit du;

    @Before
    public void setUp() throws Exception {
        du = new DateUnit(new SimpleDateFormat("yyyy-MM-dd").parse("2016-02-01"));
    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void testGetUnitTimeStamp() throws Exception {
        Date fact = du.getUnitTimeStamp();
        Date plan = new SimpleDateFormat("yyyy-MM-dd").parse("2016-02-01");
        assertEquals(fact, plan);
    }

    @Test
    public void testGetUnitYear() throws Exception {
        int fact = du.getUnitYear();
        int plan = 2016;
        assertEquals(fact, plan);
    }

    @Test
    public void testGetUnitMonthOfYear() throws Exception {
        int fact = du.getUnitMonthOfYear();
        int plan = 2;
        assertEquals(fact, plan);
    }

    @Test
    public void testGetUnitMonth() throws Exception {
        long fact = du.getUnitMonth();
        long plan = 553L;
        assertEquals(fact, plan);
    }

    @Test
    public void testGetUnitDate() throws Exception {
        long fact = du.getUnitDate();
        long plan = 16832L;
        assertEquals(fact, plan);
    }

    @Test
    public void testGetUnitDayOfWeek() throws Exception {
        int fact = du.getUnitDayOfWeek();
        int plan = 1;
        assertEquals(fact, plan);
    }

    @Test
    public void testGetWeekDay() throws Exception {
        boolean fact = du.getWeekDay();
        boolean plan = true;
        assertEquals(fact, plan);
    }
}