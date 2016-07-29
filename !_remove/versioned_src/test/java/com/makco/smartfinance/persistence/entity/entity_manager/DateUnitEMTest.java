package com.makco.smartfinance.persistence.entity.entity_manager;

import com.makco.smartfinance.constants.DataBaseConstants;
import com.makco.smartfinance.persistence.entity.DateUnit;
import com.makco.smartfinance.persistence.utils.TestPersistenceManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import javax.persistence.EntityManager;
import java.time.LocalDate;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Date;

import static org.junit.Assert.assertEquals;

/**
 * Created by Makar Kalancha on 2016-03-28.
 */
public class DateUnitEMTest {
    private final static Logger LOG = LogManager.getLogger(DateUnitEMTest.class);
    private static EntityManager em;

    private Date dateToInsert;
    private String dateToParse = "1971-02-06";//"1970-01-01" is used in databaseEngine trigger test
    private DateUnit du;

    @BeforeClass
    public static void setUpClass() throws Exception {
        em = TestPersistenceManager.INSTANCE.getEntityManager();
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() throws Exception {
        du = new DateUnit(LocalDate.of(2016, Month.FEBRUARY, 1));
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void testGetUnitTimeStamp() throws Exception {
        LocalDate fact = du.getUnitTimestamp();
        LocalDate plan = LocalDate.of(2016, Month.FEBRUARY, 1);
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
        long fact = du.getUnitDay();
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
        boolean fact = du.isWeekday();
        boolean plan = true;
        assertEquals(fact, plan);
    }

    @Test
    public void testCRUD() throws Exception{
        testPersist();
    }

    public void testPersist() throws Exception{
        LOG.info("start->testPersist");
        em.getTransaction().begin();
        DateUnit toRemove = em.find(DateUnit.class, 401L);
        if(toRemove != null) {
            em.remove(toRemove);
        }
        em.getTransaction().commit();
        ////////////////////////////////
        em.getTransaction().begin();
        LocalDate localDate = LocalDate.parse(dateToParse, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        DateUnit dateToInsert = new DateUnit(localDate);
        LOG.debug("date to insert: " + dateToInsert);
        em.persist(dateToInsert);
//        em.flush();
        em.getTransaction().commit();
        LOG.debug("INS: cad.getId()=" + dateToInsert.getUnitDay());
        LOG.debug("INS: cad.getCreatedOn()=" + dateToInsert.getCreatedOn());

        assertEquals(new Long(401), dateToInsert.getUnitDay());
        assertEquals(new Integer(6), dateToInsert.getUnitDayOfMonth());
        assertEquals(new Integer(37), dateToInsert.getUnitDayOfYear());
        assertEquals(new Long(13), dateToInsert.getUnitMonth());
        assertEquals(new Integer(2), dateToInsert.getUnitMonthOfYear());
        assertEquals(new Integer(1971), dateToInsert.getUnitYear());
        assertEquals(new Integer(6), dateToInsert.getUnitDayOfWeek());
        assertEquals(new Boolean(false), dateToInsert.isWeekday());
        assertEquals(LocalDate.of(1971, Month.FEBRUARY, 6), dateToInsert.getUnitTimestamp());
        assertEquals(true, dateToInsert.getCreatedOn() != null);
        LOG.info("end->testPersist");
    }

    @Test
    public void testBatchQty() {
        LocalDate start = LocalDate.of(2010, Month.JANUARY, 1);
        LocalDate today = LocalDate.now();
        LocalDate end = today.plus(5, ChronoUnit.YEARS);
        System.out.println("start:" + start.toString());
        System.out.println("today:" + today.toString());
        System.out.println("end:" + end.toString());

        long daysBetween = ChronoUnit.DAYS.between(start, end);
        System.out.println("difference (in days) between start and end: " + daysBetween);
        double batchQty1 = daysBetween / DataBaseConstants.BATCH_SIZE;
        System.out.println("batchQty1: " + batchQty1);
        double batchQty2 = ((double) daysBetween / DataBaseConstants.BATCH_SIZE);
        System.out.println("batchQty2: " + batchQty2);
        long result1 = Math.round(batchQty2);
        System.out.println("result: " + result1);
        double threePointZeroOne = 3.01d;
        long result2 = Math.round(threePointZeroOne);
        System.out.println("round(threePointZeroOne): " + result2);
        double result3 = Math.ceil(threePointZeroOne);
        System.out.println("ceil(threePointZeroOne): " + result3);
        double result4 = Math.floor(threePointZeroOne);
        System.out.println("floor(threePointZeroOne): " + result4);
        long result5 = Math.floorDiv(daysBetween, DataBaseConstants.BATCH_SIZE);
        System.out.println("floorDiv(daysBetween, DataBaseConstants.BATCH_SIZE): " + result5);
        long result6 = Math.floorDiv(daysBetween * (-1), DataBaseConstants.BATCH_SIZE) * (-1);
        System.out.println("floorDiv(daysBetween * (-1), DataBaseConstants.BATCH_SIZE) * (-1): " + result6);
//        Duration diffStartEnd = Duration.between(start, end);
//        System.out.println("difference (in days) between start and end: " + diffStartEnd.toDays());

    }

}