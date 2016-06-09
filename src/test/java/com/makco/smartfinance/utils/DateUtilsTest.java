package com.makco.smartfinance.utils;

import com.makco.smartfinance.user_interface.constants.UserInterfaceConstants;
import org.junit.Test;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
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
    public void testConvertUtilDateToLocalDateTime() throws Exception {
//TODO
    }

    @Test
    public void testConvertUtilDateToLocalDate() throws Exception {
//TODO
    }

    @Test
    public void testConvertUtilDateToLocalTime() throws Exception {
//TODO
    }
}