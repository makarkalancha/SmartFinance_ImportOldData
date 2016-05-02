package com.makco.smartfinance.persistence.dao;

import com.google.common.collect.Lists;
import com.makco.smartfinance.constants.DataBaseConstants;
import com.makco.smartfinance.persistence.dao.dao_implementations.DateUnitDAOImplForTest;
import com.makco.smartfinance.persistence.entity.DateUnit;
import com.makco.smartfinance.persistence.utils.DateUnitUtil;
import com.makco.smartfinance.persistence.utils.TestPersistenceManager;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.temporal.ChronoUnit;
import java.util.List;
import javax.persistence.EntityManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * User: Makar Kalancha
 * Date: 20/04/2016
 * Time: 23:21
 */
public class DateUnitDAOImplTest {
    private static final Logger LOG = LogManager.getLogger(DateUnitDAOImplTest.class);
    private static EntityManager em;
    private DateUnitDAO dateUnitDAO = new DateUnitDAOImplForTest();

    private static String addDUListElapsedTimeByBatch;
    private static String addDUListElapsedTimeAllAtOnce;

    @BeforeClass
    public static void setUpClass() throws Exception {
        em = TestPersistenceManager.INSTANCE.getEntityManager();
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
        LOG.debug(">>>addDUListElapsedTimeByBatch:" + addDUListElapsedTimeByBatch);
        LOG.debug(">>>addDUListElapsedTimeAllAtOnce:" + addDUListElapsedTimeAllAtOnce);
    }

    @Before
    public void setUp() throws Exception {
//        em.getTransaction().begin();
//        em.createQuery("DELETE FROM DateUnit").executeUpdate();
//        em.getTransaction().commit();
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void testAddDateUnit() throws Exception {

    }

    @Test
    public void testAddDateUnitListByBatch_Jan_1_1990__Jan_1_2000() throws Exception {
//        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        LocalDateTime startProcessing;
        LocalDateTime endProcessing;

        LocalDate Jan_01_1980 = LocalDate.of(1990, Month.JANUARY, 1);
        LocalDate Jan_01_1990 = LocalDate.of(2000, Month.JANUARY, 1);
//        LocalDate now = LocalDate.of(2016, Month.APRIL, 20);
//        LocalDate startDate = LocalDate.of(2011, Month.FEBRUARY, 23);
//        LocalDate endDate = DateUnitUtil.getLocaDateInFutureSinceDate(now);
        long batchesQty = DateUnitUtil.getNumberOfBatchesInRange(Jan_01_1980, Jan_01_1990);
        LOG.debug("testAddDateUnitListByBatch->batchesQty: " + batchesQty);
        List<DateUnit> dateUnitList = DateUnitUtil.getListOfDateUnitEntities(Jan_01_1980, Jan_01_1990);
        startProcessing = LocalDateTime.now();
        for(List<DateUnit> batch : Lists.partition(dateUnitList, DataBaseConstants.BATCH_SIZE)) {
            dateUnitDAO.addDateUnitList(batch);
        }
        endProcessing = LocalDateTime.now();

        LocalDateTime tmp = LocalDateTime.from(startProcessing);
        long hours = tmp.until(endProcessing, ChronoUnit.HOURS);
        long minutes = tmp.until(endProcessing, ChronoUnit.MINUTES);
        long seconds = tmp.until(endProcessing, ChronoUnit.SECONDS);
        long millisSeconds = tmp.until(endProcessing, ChronoUnit.MILLIS);
        addDUListElapsedTimeByBatch = hours + ":" + minutes + ":" + seconds + "." + millisSeconds;
        LOG.debug("testAddDateUnitListByBatch->duration (H:M:S.m): " + addDUListElapsedTimeByBatch);

//        long hours1 = Duration.between(startProcessing,endProcessing).toHours();
//        long minutes1 = Duration.between(startProcessing,endProcessing).toMinutes();
//        long seconds1 = (Duration.between(startProcessing,endProcessing).toMillis() / 1_000);
//        long millisSeconds1 = Duration.between(startProcessing,endProcessing).toMillis();
//        LOG.debug("testAddDateUnitListByBatch->duration1 (H:M:S.m): " + hours1 + ":" + minutes1 + ":" + seconds1 + "." + millisSeconds1);

        endProcessing = LocalDateTime.now();
        LOG.debug("testAddDateUnitListByBatch->method end at: " + endProcessing);
    }

    @Test
    public void testAddDateUnitListAllAtOnce_Jan_1_1980__Jan_1_1990() throws Exception {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        LocalDateTime startProcessing;
        LocalDateTime endProcessing;

        LocalDate Jan_01_1980 = LocalDate.of(1980, Month.JANUARY, 1);
        LocalDate Jan_01_1990 = LocalDate.of(1990, Month.JANUARY, 1);
        long batchesQty = DateUnitUtil.getNumberOfBatchesInRange(Jan_01_1980, Jan_01_1990);
        LOG.debug("testAddDateUnitListAllAtOnce->batchesQty: " + batchesQty);
        List<DateUnit> dateUnitList = DateUnitUtil.getListOfDateUnitEntities(Jan_01_1980, Jan_01_1990);

        startProcessing = LocalDateTime.now();

        dateUnitDAO.addDateUnitList(dateUnitList);

        endProcessing = LocalDateTime.now();

        LocalDateTime tmp = LocalDateTime.from(startProcessing);
        long hours = tmp.until(endProcessing, ChronoUnit.HOURS);
        long minutes = tmp.until(endProcessing, ChronoUnit.MINUTES);
        long seconds = tmp.until(endProcessing, ChronoUnit.SECONDS);
        long millisSeconds = tmp.until(endProcessing, ChronoUnit.MILLIS);
        addDUListElapsedTimeAllAtOnce = hours + ":" + minutes + ":" + seconds + "." + millisSeconds;
        LOG.debug("testAddDateUnitListAllAtOnce->duration1 (H:M:S.m): " + addDUListElapsedTimeAllAtOnce);
        endProcessing = LocalDateTime.now();
        LOG.debug("testAddDateUnitListAllAtOnce->method end at: " + endProcessing);
    }

    @Test
    public void testDateUnitList() throws Exception {

    }

    @Test
    public void testGetDateUnitByUnitDate() throws Exception {

    }

    @Test
    public void testIsEmpty() throws Exception {
        boolean result = dateUnitDAO.isEmpty();
        assert (!result);
    }
}