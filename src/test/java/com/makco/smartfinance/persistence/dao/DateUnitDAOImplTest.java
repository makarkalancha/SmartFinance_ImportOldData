package com.makco.smartfinance.persistence.dao;

import com.makco.smartfinance.persistence.utils.DateUnitUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Test;

import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.temporal.ChronoUnit;

import static org.junit.Assert.*;

/**
 * User: Makar Kalancha
 * Date: 20/04/2016
 * Time: 23:21
 */
public class DateUnitDAOImplTest {
    private static final Logger LOG = LogManager.getLogger(DateUnitDAOImplTest.class);

    @Test
    public void testAddDateUnit() throws Exception {

    }

    @Test
    public void testAddDateUnitListByBatch() throws Exception {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        LocalDateTime start;
        LocalDateTime end;

        LocalDate now = LocalDate.of(2016, Month.APRIL, 20);
        LocalDate startDate = LocalDate.of(2011, Month.FEBRUARY, 23);
        LocalDate endDate = DateUnitUtil.getLocaDateInFutureSinceDate(now);
        long batchesQty = DateUnitUtil.getNumberOfBatchesInRange(startDate, endDate);
        DateUnitDAO dateUnitDAO = new DateUnitDAOImpl();
        start = LocalDateTime.now();
//        dateUnitDAO.addDateUnitList();

        end = LocalDateTime.now();
        LOG.debug("testAddDateUnitListByBatch->batch end: " + end);
        LocalDateTime tmp = LocalDateTime.from(start);
        long hours = tmp.until(end, ChronoUnit.HOURS);
        long minutes = tmp.until(end, ChronoUnit.MINUTES);
        long seconds = tmp.until(end, ChronoUnit.SECONDS);
        long millisSeconds = tmp.until(end, ChronoUnit.MILLIS);
        LOG.debug("testAddDateUnitListByBatch->duration (H:M:S.m): " + hours + ":" + minutes + ":" + seconds + "." + millisSeconds);

        long hours1 = Duration.between(start,end).toHours();
        long minutes1 = Duration.between(start,end).toMinutes();
        long seconds1 = (Duration.between(start,end).toMillis() / 1_000);
        long millisSeconds1 = Duration.between(start,end).toMillis();
        LOG.debug("testAddDateUnitListByBatch->duration1 (H:M:S.m): " + hours1 + ":" + minutes1 + ":" + seconds1 + "." + millisSeconds1);


        end = LocalDateTime.now();
        LOG.debug("testAddDateUnitListByBatch->method end at: " + end);
    }

    @Test
    public void testAddDateUnitListAllAtOnce() throws Exception {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        LocalDateTime start;
        LocalDateTime end;

        LocalDate now = LocalDate.of(2016, Month.APRIL, 20);
        LocalDate startDate = LocalDate.of(2011, Month.FEBRUARY, 23);
        LocalDate endDate = DateUnitUtil.getLocaDateInFutureSinceDate(now);
        DateUnitDAO dateUnitDAO = new DateUnitDAOImpl();
        start = LocalDateTime.now();
//        dateUnitDAO.addDateUnitList();

        end = LocalDateTime.now();
        LOG.debug("testAddDateUnitListAllAtOnce->batch end: " + end);
        LocalDateTime tmp = LocalDateTime.from(start);
        long hours = tmp.until(end, ChronoUnit.HOURS);
        long minutes = tmp.until(end, ChronoUnit.MINUTES);
        long seconds = tmp.until(end, ChronoUnit.SECONDS);
        long millisSeconds = tmp.until(end, ChronoUnit.MILLIS);
        LOG.debug("testAddDateUnitListAllAtOnce->duration (H:M:S.m): " + hours + ":" + minutes + ":" + seconds + "." + millisSeconds);

        long hours1 = Duration.between(start,end).toHours();
        long minutes1 = Duration.between(start,end).toMinutes();
        long seconds1 = (Duration.between(start,end).toMillis() / 1_000);
        long millisSeconds1 = Duration.between(start,end).toMillis();
        LOG.debug("testAddDateUnitListAllAtOnce->duration1 (H:M:S.m): " + hours1 + ":" + minutes1 + ":" + seconds1 + "." + millisSeconds1);


        end = LocalDateTime.now();
        LOG.debug("testAddDateUnitListAllAtOnce->method end at: " + end);
    }

    @Test
    public void testDateUnitList() throws Exception {

    }

    @Test
    public void testGetDateUnitByUnitDate() throws Exception {

    }
}