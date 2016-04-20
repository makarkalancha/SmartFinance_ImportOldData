package com.makco.smartfinance.persistence.entity;

import com.makco.smartfinance.persistence.contants.DataBaseConstants;
import com.makco.smartfinance.persistence.utils.TestPersistenceManager;
import java.time.LocalDate;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import javax.persistence.EntityManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Created by mcalancea on 2016-03-28.
 */
public class DateUnitTest {
    private final static Logger LOG = LogManager.getLogger(DateUnitTest.class);
    private static EntityManager em;

    private Date dateToInsert;
    private String dateToParse = "1971-02-06";//"1970-01-01" is used in databaseEngine trigger test

//    private static int MIN = 1;
//    private static int MAX = 100;

    @BeforeClass
    public static void setUpClass() throws Exception {
        em = TestPersistenceManager.INSTANCE.getEntityManager();
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
//        em.close();
//        TestPersistenceManager.INSTANCE.close();
    }

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
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
        ////////////////

        em.getTransaction().begin();
//        Random rand = new Random();
//        int randMonth = (rand.nextInt((12 - 1) + 1) + 1);
//        int randDay = (rand.nextInt((28 - 1) + 1) + 1);
//        String dateToParse = (rand.nextInt((2016 - 2000) + 1) + 2000) +
//                "-" +
//                ((randMonth < 10) ? "0" + randMonth : randMonth) +
//                "-" +
//                ((randDay < 10) ? "0" + randDay : randDay);
        String mess1 = "random date (from 2000-01-01 to 2016-12-28): " + dateToParse;
        System.out.println(mess1);
        LOG.debug(mess1);
        LocalDate localDate = LocalDate.parse(dateToParse, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        DateUnit dateToInsert = new DateUnit(localDate);
        LOG.debug("date to insert: " + dateToInsert);
        em.persist(dateToInsert);
//        em.flush();
        em.getTransaction().commit();
        LOG.debug("INS: cad: .getId()=" + dateToInsert.getUnitDate());
        LOG.debug("INS: cad.getCreatedOn()=" + dateToInsert.getCreatedOn());

        assertEquals(new Long(401), dateToInsert.getUnitDate());
        assertEquals(new Integer(6), dateToInsert.getUnitDateOfMonth());
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
        LocalDate start = LocalDate.of(2010, Month.JANUARY, 01);
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