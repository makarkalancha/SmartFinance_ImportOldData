package com.makco.smartfinance.utils;

import com.makco.smartfinance.user_interface.constants.UserInterfaceConstants;
import org.junit.Test;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.util.Date;

import static org.junit.Assert.*;

/**
 * Created by mcalancea on 2016-06-09.
 */
public class DateUtilsTest {

    @Test
    public void testConvertLocalDateToUtilDate() throws Exception {
        LocalDate localDate = LocalDate.of(2015, Month.APRIL, 3);
        Date actualResult = DateUtils.convertLocalDateToUtilDate(localDate);
        Date expectedResult = UserInterfaceConstants.DATE_FORMAT.parse("2015-4-3");
        assertEquals(expectedResult, actualResult);
    }

    @Test
    public void testConvertLocalDateToUtilDate_null() throws Exception {
        LocalDate localDate = null;
        Date actualResult = DateUtils.convertLocalDateToUtilDate(localDate);
        Date expectedResult = null;
        assertEquals(expectedResult, actualResult);
    }

    @Test
    public void testConvertUtilDateToLocalDateTime() throws Exception {
        Date date = UserInterfaceConstants.DATE_TIME_FORMAT.parse("2015-4-3 23:01:03");
        LocalDateTime actualResult = DateUtils.convertUtilDateToLocalDateTime(date);
        LocalDateTime expectedResult = LocalDateTime.of(2015, Month.APRIL, 3, 23, 1, 3);
        assertEquals(expectedResult, actualResult);
    }

    @Test
    public void testConvertUtilDateToLocalDateTime_null() throws Exception {
        Date date = null;
        LocalDateTime actualResult = DateUtils.convertUtilDateToLocalDateTime(date);
        LocalDateTime expectedResult = null;
        assertEquals(expectedResult, actualResult);
    }

    @Test
    public void testConvertUtilDateToLocalDate() throws Exception {
        Date date = UserInterfaceConstants.DATE_FORMAT.parse("2015-4-3");
        LocalDate actualResult = DateUtils.convertUtilDateToLocalDate(date);
        LocalDate expectedResult = LocalDate.of(2015, Month.APRIL, 3);
        assertEquals(expectedResult, actualResult);
    }

    @Test
    public void testConvertUtilDateToLocalDate_null() throws Exception {
        Date date = null;
        LocalDate actualResult = DateUtils.convertUtilDateToLocalDate(date);
        LocalDate expectedResult = null;
        assertEquals(expectedResult, actualResult);
    }

    @Test
    public void testConvertUtilDateToLocalTime() throws Exception {
        Date date = UserInterfaceConstants.DATE_TIME_FORMAT.parse("2015-4-3 23:01:03");
        LocalTime actualResult = DateUtils.convertUtilDateToLocalTime(date);
        LocalTime expectedResult = LocalTime.of(23, 1, 3);
        assertEquals(expectedResult, actualResult);
    }

    @Test
    public void testConvertUtilDateToLocalTime_null() throws Exception {
        Date date = null;
        LocalTime actualResult = DateUtils.convertUtilDateToLocalTime(date);
        LocalTime expectedResult = null;
        assertEquals(expectedResult, actualResult);
    }
}