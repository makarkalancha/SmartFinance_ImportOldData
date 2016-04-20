package com.makco.smartfinance.persistence.entity;

import com.makco.smartfinance.persistence.utils.TestPersistenceManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Random;

import static org.junit.Assert.assertEquals;

/**
 * Created by mcalancea on 2016-03-28.
 */
public class DateUnitTest {
    private final static Logger LOG = LogManager.getLogger(DateUnitTest.class);
    private static EntityManager em;

    private static int MIN = 1;
    private static int MAX = 100;

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
        Random rand = new Random();
        int randMonth = (rand.nextInt((12 - 1) + 1) + 1);
        int randDay = (rand.nextInt((28 - 1) + 1) + 1);
        String dateToParse = (rand.nextInt((2016 - 2000) + 1) + 2000) +
                "-" +
                ((randMonth < 10) ? "0" + randMonth : randMonth) +
                "-" +
                ((randDay < 10) ? "0" + randDay : randDay);
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

        assertEquals(true, dateToInsert.getCreatedOn() != null);
        LOG.info("end->testPersist");
    }

}