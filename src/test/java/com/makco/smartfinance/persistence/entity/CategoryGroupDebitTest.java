package com.makco.smartfinance.persistence.entity;

import com.makco.smartfinance.persistence.utils.TestPersistenceManager;
import javax.persistence.EntityManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;

/**
 * Created by mcalancea on 2016-04-25.
 */
public class CategoryGroupDebitTest {
    private final static Logger LOG = LogManager.getLogger(CategoryGroupDebitTest.class);
    private static EntityManager em;

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

}